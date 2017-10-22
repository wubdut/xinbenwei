package hlqs.westworld.server.application;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.redis.RedisUtil;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.Triple;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import hlqs.westworld.server.lib.Util;
import hlqs.westworld.server.lib.config.RedisConfig;
import hlqs.westworld.server.lib.config.ThreadConfig;
import hlqs.westworld.server.response.RecommendData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RecommendApplication implements Runnable{
	private boolean stopflag = false;
	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private static final String LIST_KEY = "recommend_list";
	private final RedisUtil ru;
	private final int corePoolSize;
	private final int maximumPoolSize;
	private final long keepAliveTime;
	private final long sleepMillTime;
	private final Set<Integer> days_of_week;
	private final int startHour;
	private final int startMinute;
	private final int endHour;
	private final int endMinute;
	private final String timeZone;
	private static Thread thread = null;
	
	public RecommendApplication(String TIME_ZONE, String RECOMMEND_DAYS_OF_WEEK, String RECOMMEND_TIME) {
		this.ru = new RedisUtil(RedisConfig.redisURL, 
				RedisConfig.port, 
				RedisConfig.timeOut, 
				RedisConfig.redisPassword);
		
		this.timeZone = TIME_ZONE;
		
		this.days_of_week = new HashSet<Integer>();
		for (String day : RECOMMEND_DAYS_OF_WEEK.split(",")) {
			days_of_week.add(Integer.parseInt(day.trim()));
		}
		
		String[] time = RECOMMEND_TIME.split("-");
		this.startHour = Integer.parseInt(time[0].split(":")[0]);
		this.startMinute = Integer.parseInt(time[0].split(":")[1]);
		this.endHour = Integer.parseInt(time[1].split(":")[0]);
		this.endMinute = Integer.parseInt(time[1].split(":")[1]);
		
		this.corePoolSize = ThreadConfig.corePoolSize;
		this.maximumPoolSize = ThreadConfig.maximumPoolSize;
		this.keepAliveTime = ThreadConfig.keepAliveTime;
		this.sleepMillTime = ThreadConfig.sleepMillTime;
	}
	
	public void recommend(String stockid) {
		new Thread(new Recommend(stockid)).start();
	}
	
	private class Recommend implements Runnable{
		
		private final String stockid;
		
		public Recommend(String stockid) {
			this.stockid = stockid;
		}
		
		@Override 
		public void run() {
			Calendar now_cal = Calendar.getInstance();
			System.out.println(now_cal.getTime() + " RecommendApplication " + stockid);
			
			try {
				Double today_price = new StockData().price(stockid);
				OnlineStockStrategyAPI ossAPI = Util.open(stockid);
				if (ossAPI == null) {
					System.out.println(stockid + " RecommendApplication result is error");
					return ;
				}
				DoubleSeries data = (new StockData()).history_data(stockid);
				for (int i = 0; i < data.size(); i++) {
					ossAPI.respond(data.get(i).getItem(), data.get(i).getInstant());
				}
				long timestamp = System.currentTimeMillis();
				Triple<String, Double, Double> triple = ossAPI.respond(today_price, timestamp);
				String context = new Gson().toJson(new RecommendData(
						stockid, 
						triple.getSecond(), 
						today_price,
						timestamp));
				if (triple.getFirst().equals("b")) {
					System.out.println(stockid + " RecommendApplication result is not recommend");
					return ;
				}
				Jedis jedis = ru.getJedis();
				Transaction transaction = jedis.multi();
				transaction.rpush(LIST_KEY, context);
				transaction.exec();
				jedis.close();
				System.out.println(stockid + " RecommendApplication result is " + context);
			} catch (ArrayIndexOutOfBoundsException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return ;
		}
	}
	
	private boolean checkTime(Calendar cal) {
		cal.setTimeZone(TimeZone.getTimeZone(timeZone));
		if (!days_of_week.contains(cal.get(Calendar.DAY_OF_WEEK))) return false;
		if (cal.get(Calendar.HOUR_OF_DAY) < startHour) return false;
		if (cal.get(Calendar.HOUR_OF_DAY) > endHour) return false;
		if (cal.get(Calendar.HOUR_OF_DAY)==startHour && cal.get(Calendar.MINUTE)<startMinute) return false;
		if (cal.get(Calendar.HOUR_OF_DAY)==endHour && cal.get(Calendar.MINUTE)>endMinute) return false;
		return true;
	}
		
	@Override
	public void run() {
		// TODO Auto-generated method stub

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

		for (int i = 0; i < mSeries.size() && !stopflag;) {
			Calendar now_cal = Calendar.getInstance();
			if (executor.getPoolSize()>=maximumPoolSize || !checkTime(now_cal)) {
				try {
					Thread.sleep(sleepMillTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			String stockid = mSeries.getValue("stock-id", i);
			executor.execute(new Recommend(stockid));
			i += 1;
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
