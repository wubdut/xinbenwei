package hlqs.westworld.server.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.redis.RedisUtil;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.Entry;
import com.realsight.westworld.tsp.lib.util.Triple;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import ch.qos.logback.classic.Logger;
import hlqs.westworld.server.lib.Util;
import hlqs.westworld.server.response.AssociateData;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

public class AssociateApplication extends Thread{
	private boolean stopflag = false;
	private static Logger logger = (Logger) LoggerFactory.getLogger(AssociateApplication.class);
	private static final int MIN_INTERVAL = 10;
	private static final int OBSERVER_LEN = 10;
	private static final int RESULT_SIZE = 30;
	private static final String HSET_KEY = "associate";
	private final RedisUtil ru;
	
	public AssociateApplication(String host, int port, int timeOut, String password) {
		ru = new RedisUtil(host, port, timeOut, password);
	}
	
	private String associate(String stock_id) throws Exception {
		List<Triple<Long, Double, Double>> timestamps = new ArrayList<Triple<Long, Double, Double>>();
		int upnum = 0;
		int downnum = 0;
		if (stock_id==null || stock_id.equals("")){
			return "error stockid";
		} else {
			Double today_price = new StockData().price(stock_id);
			OnlineStockStrategyAPI ossAPI = Util.open(stock_id);
			if (ossAPI==null || today_price==null)
				return "error";
			ossAPI.respond(today_price, System.currentTimeMillis());
			List<Entry<Double, Long>> entrys = ossAPI.associate(ossAPI.getActiveNeuros());
			DoubleSeries series = ossAPI.getSeries();
			Collections.sort(entrys);
			Collections.reverse(entrys);
			for (Entry<Double, Long> entry : entrys) {
				if (timestamps.size() > RESULT_SIZE) {
					break;
				}
				int index = series.timestamp2Index(entry.getSecond());
				if (index+MIN_INTERVAL >= series.size()) continue;
				if (index-OBSERVER_LEN < 0) continue;
				double lv = (series.get(index+1).getItem()-series.get(index).getItem())/series.get(index).getItem()*100.0;
				timestamps.add(new Triple<Long, Double, Double>(series.get(index).getInstant(), entry.getFirst(), lv));
				if (series.get(index+1).getItem() > series.get(index).getItem()) {
					upnum += 1;
				} else {
					downnum += 1;
				}
			}
		}
		AssociateData ret = new AssociateData(stock_id, upnum, downnum, timestamps);
		
		String context = new Gson().toJson(ret);
		Jedis jedis = ru.getJedis();
		Transaction transaction = jedis.multi();
		transaction.hset(HSET_KEY, stock_id, context);
		transaction.exec();
		jedis.close();
		
		return context;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(!stopflag) {
			MultipleStringSeries res = new StockData().stockidset();
			for (int i = 0; i < res.size(); i+=1) {
				Calendar now_cal = Calendar.getInstance();
				System.out.println(now_cal.getTime() + " AssociateApplication " + res.getValue("stock-id", i));
				try {
					String info = associate(res.getValue("stock-id", i));
					System.out.println(res.getValue("stock-id", i) + " AssociateApplication result is " + info);
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
