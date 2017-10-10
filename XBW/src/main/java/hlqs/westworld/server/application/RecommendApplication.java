package hlqs.westworld.server.application;

import java.util.Calendar;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.redis.RedisUtil;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.Triple;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import ch.qos.logback.classic.Logger;
import hlqs.westworld.server.lib.Util;
import hlqs.westworld.server.response.RecommendData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RecommendApplication extends Thread{
	private boolean stopflag = false;
	private static Logger logger = (Logger) LoggerFactory.getLogger(RecommendApplication.class);
	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private static final String HSET_KEY = "recommend";
	private static final String ZSET_KEY = "recommend_action";
	private final RedisUtil ru;
	
	public RecommendApplication(String host, int port, int timeOut, String password) {
		ru = new RedisUtil(host, port, timeOut, password);
	}
	
	private String recommend(String stock_id) throws Exception {
		Double today_price = new StockData().price(stock_id);
		OnlineStockStrategyAPI ossAPI = Util.open(stock_id);
		if (ossAPI == null) return "error";
		DoubleSeries data = (new StockData()).history_data(stock_id);
		for (int i = 0; i < data.size(); i++) {
			ossAPI.respond(data.get(i).getItem(), data.get(i).getInstant());
		}
		Triple<String, Double, Double> triple = ossAPI.respond(today_price, System.currentTimeMillis());
		
		String context = new Gson().toJson(new RecommendData(
				stock_id, 
				triple.getFirst(), 
				triple.getSecond(), 
				today_price));
		Jedis jedis = ru.getJedis();
		Transaction transaction = jedis.multi();
		transaction.hset(HSET_KEY, stock_id, context);
		transaction.zadd(ZSET_KEY, triple.getFirst().equals("a")?1.0:-1.0, stock_id);
		transaction.exec();
		jedis.close();
		
		return context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stopflag) {
			for (int i = 0; i < mSeries.size(); i+=1) {
				Calendar now_cal = Calendar.getInstance();
				System.out.println(now_cal.getTime() + " RecommendApplication " + mSeries.getValue("stock-id", i));
				try {
					String info = recommend(mSeries.getValue("stock-id", i));
					System.out.println(mSeries.getValue("stock-id", i) + " RecommendApplication result is " + info);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					Thread.sleep(1000L * 6);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					logger.error(e.getMessage());
				}
			}
			
		}
	}
	
	public synchronized void status(boolean stopflag) {
		this.stopflag = stopflag;
		if (stopflag) {
			return ;
		} else {
			if (!this.isAlive()){
				this.start();
			}
		}
	}
	
}
