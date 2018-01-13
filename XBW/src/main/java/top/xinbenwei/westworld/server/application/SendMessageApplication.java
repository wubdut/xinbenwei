package top.xinbenwei.westworld.server.application;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.realsight.westworld.tsp.lib.redis.RedisUtil;

import ch.qos.logback.classic.Logger;
import redis.clients.jedis.Jedis;
import top.xinbenwei.westworld.server.lib.WinXinUtil;
import top.xinbenwei.westworld.server.lib.config.RedisConfig;
import top.xinbenwei.westworld.server.lib.config.ThreadConfig;

public class SendMessageApplication implements Runnable{
	
	private static final Logger logger = (Logger) LoggerFactory.getLogger(SendMessageApplication.class);
	
	private boolean stopflag = false;
	private static final String LIST_KEY = "SendMessage";
	private final RedisUtil ru;
	private final int corePoolSize;
	private final int maximumPoolSize;
	private final long keepAliveTime;
	private final long sleepMillTime;
	private static Thread thread = null;
	
	public SendMessageApplication() {
		this.ru = new RedisUtil(RedisConfig.redisURL, 
				RedisConfig.port, 
				RedisConfig.timeOut, 
				RedisConfig.redisPassword);
		
		this.corePoolSize = ThreadConfig.corePoolSize;
		this.maximumPoolSize = ThreadConfig.maximumPoolSize;
		this.keepAliveTime = ThreadConfig.keepAliveTime;
		this.sleepMillTime = ThreadConfig.sleepMillTime;
	}
	
	private class Send implements Runnable{
		
		private final JsonObject message;
		
		public Send(JsonObject message) {
			this.message = message;
		}
		
		@Override 
		public void run() {
			logger.info(" Send " + message);
			List<String> mobiles = new ArrayList<String>();
			for (int i = 0; i < message.get("mobiles").getAsJsonArray().size(); i++) {
				mobiles.add(message.get("mobiles").getAsJsonArray().get(i).getAsString());
			}
			WinXinUtil.SendMessage2Mobiles(mobiles, message.get("message").getAsString());
			return ;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
		while(!stopflag) {
			try {
				Jedis jedis = ru.getJedis();
				String json = jedis.lpop(LIST_KEY);
				if (json == null) {
					logger.info("Task SendMessage is null.");
					break;
				}
				jedis.close();
				executor.execute(new Send(new Gson().fromJson(json, new TypeToken<JsonObject>() {
					private static final long serialVersionUID = -699231746661823833L;}.getType())));
				logger.info(json);
				Thread.sleep(sleepMillTime);
			} catch (ArrayIndexOutOfBoundsException | InterruptedException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
		}
	}
	
	public synchronized void status(boolean stopflag) {
		this.stopflag = stopflag;
		if (stopflag) {
			return ;
		} else {
			if (thread==null || !thread.isAlive()){
				thread = new Thread(this);
				thread.start();
			}
		}
	}
}
