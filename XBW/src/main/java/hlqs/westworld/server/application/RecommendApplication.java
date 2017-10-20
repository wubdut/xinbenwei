package hlqs.westworld.server.application;

import java.io.IOException;
import java.util.Calendar;
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
import hlqs.westworld.server.response.RecommendData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class RecommendApplication extends Thread{
	private boolean stopflag = false;
	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private static final String HSET_KEY = "recommend";
	private static final String ZSET_KEY = "recommend_action";
	private static final String LIST_KEY = "recommend_list";
	private final RedisUtil ru;
	private static final int corePoolSize = 5;
	private static final int maximumPoolSize = 10;
	private static final long keepAliveTime = 2000;
	private static final long sleepMillTime = 10000;
	private final int startHour;
	private final int startMinute;
	private final int endHour;
	private final int endMinute;
	
	public RecommendApplication(String host, int port, int timeOut, String password, String RECOMMEND_TIME) {
		ru = new RedisUtil(host, port, timeOut, password);
		String[] time = RECOMMEND_TIME.split("-");
		startHour = Integer.parseInt(time[0].split(":")[0]);
		startMinute = Integer.parseInt(time[0].split(":")[1]);
		endHour = Integer.parseInt(time[1].split(":")[0]);
		endMinute = Integer.parseInt(time[1].split(":")[1]);
	}
	
	public void recommend(String stockid) {
		new Thread(new Recommend(stockid)).start();
	}
	
	private class Recommend implements Runnable{
		
		private final String stockid;
		
		public Recommend(String stockid) {
			this.stockid = stockid;
		}
		
		private boolean checkTime(Calendar cal) {
			if (cal.get(Calendar.HOUR) < startHour) return false;
			if (cal.get(Calendar.HOUR) > endHour) return false;
			if (cal.get(Calendar.HOUR)==startHour && cal.get(Calendar.MINUTE)<startMinute) return false;
			if (cal.get(Calendar.HOUR)==endHour && cal.get(Calendar.MINUTE)>endMinute) return false;
			return true;
		}
		
		@Override 
		public void run() {
			Calendar now_cal = Calendar.getInstance();
			System.out.println(now_cal.getTime() + " RecommendApplication " + stockid);
			if (!checkTime(now_cal)) {
				System.out.println(stockid + " RecommendApplication time error");
				return ;
			}
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
				transaction.hset(HSET_KEY, stockid, context);
				transaction.zadd(ZSET_KEY, timestamp, stockid);
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
		
	@Override
	public void run() {
		// TODO Auto-generated method stub

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, 
				maximumPoolSize, 
				keepAliveTime, 
				TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

		for (int i = 0; i < mSeries.size() && !stopflag;) {
			if (executor.getPoolSize() >= maximumPoolSize) {
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
			if (!this.isAlive()){
				this.start();
			}
		}
	}
	
}
