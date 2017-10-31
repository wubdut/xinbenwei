package hlqs.westworld.server.application;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.series.MultipleDoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import hlqs.westworld.server.lib.Util;
import hlqs.westworld.server.lib.config.ThreadConfig;

public class UpdateApplication extends Thread{
	
	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private boolean stopflag = false;
	private final int EPOCH;
	private final double DISPOSITION;
	private final long TERMINATE_TIMESTAMP;
	
	private final int corePoolSize;
	private final int maximumPoolSize;
	private final long keepAliveTime;
	private final long sleepMillTime;
	private static Thread thread = null;
	
	public UpdateApplication(double DISPOSITION, long TERMINATE_TIMESTAMP, int EPOCH) {
		this.DISPOSITION = DISPOSITION;
		this.TERMINATE_TIMESTAMP = TERMINATE_TIMESTAMP;
		this.EPOCH = EPOCH;
		
		this.corePoolSize = ThreadConfig.corePoolSize;
		this.maximumPoolSize = ThreadConfig.maximumPoolSize;
		this.keepAliveTime = ThreadConfig.keepAliveTime;
		this.sleepMillTime = ThreadConfig.sleepMillTime;
	}
	
	public void update(String stockid) {
		new Thread(new Update(stockid)).start();
	}
	
	private class Update implements Runnable {

		private final String stockid;
		
		public Update(String stockid) {
			this.stockid = stockid;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!Util.modifyAccess(stockid)) return ;
			try {
				Calendar now_cal = Calendar.getInstance();
				System.out.println(now_cal.getTime() + " UpdateApplication " + stockid);
				MultipleDoubleSeries data = (new StockData()).history_data(stockid);
				OnlineStockStrategyAPI ossAPI = new OnlineStockStrategyAPI(DISPOSITION);
				for (int i = 0; i+5 < data.size(); i++) {
					Double today = (data.getValue("close", i));
					Double tomorrow_1 = (data.getValue("close", i+1));
					Double tomorrow_2 = (data.getValue("close", i+2));
					Double tomorrow_3 = (data.getValue("close", i+3));
					Double tomorrow_4 = (data.getValue("close", i+4));
					Double tomorrow_5 = (data.getValue("close", i+5));
					ossAPI.createStates(today, tomorrow_1, tomorrow_2, tomorrow_3, tomorrow_4, tomorrow_5);
				}
				ossAPI.sleep();
				for (int e = 0; e < EPOCH; e++) {
					for (int i = 0; i+5 < data.size(); i++) {
						if (data.get(i).getInstant() >= TERMINATE_TIMESTAMP) continue;
						Double today = (data.getValue("close", i));
						Double tomorrow_1 = (data.getValue("close", i+1));
						Double tomorrow_2 = (data.getValue("close", i+2));
						Double tomorrow_3 = (data.getValue("close", i+3));
						Double tomorrow_4 = (data.getValue("close", i+4));
						Double tomorrow_5 = (data.getValue("close", i+5));
						ossAPI.train(today, tomorrow_1, tomorrow_2, tomorrow_3, tomorrow_4, tomorrow_5);
					}
					ossAPI.sleep();
				}
				Util.save(stockid, ossAPI);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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
			String stockid = mSeries.getValue("stock-id", i);
			if (executor.getPoolSize() >= maximumPoolSize) {
				try {
					Thread.sleep(sleepMillTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			executor.execute(new Update(stockid));
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
