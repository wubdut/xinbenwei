package hlqs.westworld.server.application;

import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import hlqs.westworld.server.lib.Util;

public class UpdateApplication extends Thread{
	
	private static final MultipleStringSeries mSeries = new StockData().stockidset();
	private boolean stopflag = false;
	private final double epoch = 5;
	private final double DISPOSITION;
	private final long TERMINATE_TIMESTAMP;
	
	private static final int corePoolSize = 5;
	private static final int maximumPoolSize = 10;
	private static final long keepAliveTime = 2000;
	private static final long sleepMillTime = 10000;
	
	public UpdateApplication(double DISPOSITION, long TERMINATE_TIMESTAMP) {
		this.DISPOSITION = DISPOSITION;
		this.TERMINATE_TIMESTAMP = TERMINATE_TIMESTAMP;
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
				DoubleSeries data = (new StockData()).history_data(stockid);
				OnlineStockStrategyAPI ossAPI = new OnlineStockStrategyAPI(DISPOSITION);
				for (int i = 0; i+1 < data.size(); i++) {
					ossAPI.createStates(data.get(i).getItem(), data.get(i+1).getItem());
				}
				ossAPI.sleep();
				for (int e = 0; e < epoch; e++) {
					for (int i = 0; i+1 < data.size(); i++) {
						if (data.get(i).getInstant() >= TERMINATE_TIMESTAMP) continue;
						ossAPI.train(data.get(i).getItem(), data.get(i+1).getItem());
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
			if (!this.isAlive()){
				this.start();
			}
		}
	}
	
}
