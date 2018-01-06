package hlqs.westworld.server.application;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.series.MultipleDoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import hlqs.westworld.server.lib.Util;
import hlqs.westworld.server.lib.config.ThreadConfig;

public class UpdateApplication extends Thread{
	
	private static final Logger logger = Logger.getLogger(UpdateApplication.class);

	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private boolean stopflag = false;
	private final int EPOCH;
	private final double DISPOSITION;
	private final double C;
	private final double ATTENUATION;
	private final int TRANSACTION_PERIOD;
	private final long TERMINATE_TIMESTAMP;
	
	private final int corePoolSize;
	private final int maximumPoolSize;
	private final long keepAliveTime;
	private final long sleepMillTime;
	private static Thread thread = null;
	
	public UpdateApplication(double DISPOSITION, 
			double ATTENUATION, 
			double C, 
			int TRANSACTION_PERIOD, 
			long TERMINATE_TIMESTAMP, 
			int EPOCH) {
		this.DISPOSITION = DISPOSITION;
		this.C = C;
		this.ATTENUATION = ATTENUATION;
		this.TRANSACTION_PERIOD = TRANSACTION_PERIOD;
		this.TERMINATE_TIMESTAMP = TERMINATE_TIMESTAMP;
		this.EPOCH = EPOCH;
		
		this.corePoolSize = ThreadConfig.corePoolSize;
		this.maximumPoolSize = ThreadConfig.maximumPoolSize;
		this.keepAliveTime = ThreadConfig.keepAliveTime;
		this.sleepMillTime = ThreadConfig.sleepMillTime;
	}
	
	private class Update implements Runnable {

		private final String stockid;
		private final double disposition;
		
		public Update(String stockid, double disposition) {
			this.stockid = stockid;
			this.disposition = disposition;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!Util.modifyAccess(stockid)) return ;
			try {
				logger.info("UpdateApplication " + stockid);
				MultipleDoubleSeries data = (new StockData()).history_data(stockid);
				OnlineStockStrategyAPI ossAPI = new OnlineStockStrategyAPI(disposition, ATTENUATION, C);
				for (int i = 0; i+TRANSACTION_PERIOD < data.size(); i++) {
					double today = (data.getValue("close", i));
					double[] tomorrows = new double[TRANSACTION_PERIOD];
					for (int j = 0; j < TRANSACTION_PERIOD; ++ j) {
						tomorrows[j] = data.getValue("close", i+j+1);
					}
					ossAPI.createStates(today, tomorrows);
				}
				ossAPI.sleep();
				for (int e = 0; e < EPOCH; e++) {
					for (int i = 0; i+TRANSACTION_PERIOD < data.size(); i++) {
						if (data.get(i).getInstant() >= TERMINATE_TIMESTAMP) continue;
						double today = (data.getValue("close", i));
						double[] tomorrows = new double[TRANSACTION_PERIOD];
						for (int j = 0; j < TRANSACTION_PERIOD; ++ j) {
							tomorrows[j] = data.getValue("close", i+j+1);
						}
						ossAPI.train(today, tomorrows);
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
                new ArrayBlockingQueue<Runnable>(maximumPoolSize));
		
		for (int i = 0; i < mSeries.size() && !stopflag; ) {
			String stockid = mSeries.getValue("stock-id", i);
			
			double disposition = DISPOSITION;
			if (mSeries.getValue("disposition", i) != null){
				try {
					disposition = Double.parseDouble(mSeries.getValue("disposition", i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				i += 1;
				continue;
			}
			if (executor.getPoolSize() >= maximumPoolSize) {
				try {
					Thread.sleep(sleepMillTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				continue;
			}
			executor.execute(new Update(stockid, disposition));
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
