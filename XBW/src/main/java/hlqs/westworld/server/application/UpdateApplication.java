package hlqs.westworld.server.application;

import java.util.Calendar;

import org.slf4j.LoggerFactory;

import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;
import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.series.MultipleStringSeries;
import com.realsight.westworld.tsp.lib.util.data.StockData;

import ch.qos.logback.classic.Logger;
import hlqs.westworld.server.lib.Util;

public class UpdateApplication extends Thread{
	private boolean stopflag = false;
	private static Logger logger = (Logger) LoggerFactory.getLogger(UpdateApplication.class);
	private final double epoch = 5;
	private final double DISPOSITION;
	
	public UpdateApplication(double DISPOSITION) {
		this.DISPOSITION = DISPOSITION;
	}
	
	public void update(String stockid) throws Exception {
		DoubleSeries data = (new StockData()).history_data(stockid);
		OnlineStockStrategyAPI ossAPI = new OnlineStockStrategyAPI(DISPOSITION);
		for (int i = 0; i+1 < data.size(); i++) {
			ossAPI.createStates(data.get(i).getItem(), data.get(i+1).getItem());
		}
		ossAPI.sleep();
		for (int e = 0; e < epoch; e++) {
			for (int i = 0; i+1 < data.size(); i++) {
				ossAPI.train(data.get(i).getItem(), data.get(i+1).getItem());
			}
			ossAPI.sleep();
		}
		Util.save(stockid, ossAPI);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MultipleStringSeries res = new StockData().stockidset();
		for (int i = 0; i < res.size() && stopflag; i+=1) {
			Calendar now_cal = Calendar.getInstance();
			System.out.println(now_cal.getTime() + " UpdateApplication " + res.getValue("stock-id", i));
			if (!Util.modifyAccess(res.getValue("stock-id", i))) continue;
			try {
				update(res.getValue("stock-id", i));
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
