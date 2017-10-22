package hlqs.westworld.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import hlqs.westworld.server.application.RecommendApplication;
import hlqs.westworld.server.application.UpdateApplication;
import hlqs.westworld.server.lib.config.RedisConfig;
import hlqs.westworld.server.lib.config.ThreadConfig;

public class XBWServers {
	private boolean UPDATE_FLAG = false;
	private boolean RECOMMEND_FLAG = false;
	private double DISPOSITION = 2.1;
	private long TERMINATE_TIMESTAMP = 1496246400000L;
	private String RECOMMEND_TIME = "14:30-15:00";
	private String TIME_ZONE = "GMT+8:00";
	private String RECOMMEND_DAYS_OF_WEEK = "2,3,4,5,6";
	private int EPOCH = 10;
	private static Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	
	static {
		logger.setLevel(Level.WARN);
	}
	
	public void run() {
		initialize();
		
		RecommendApplication ra = new RecommendApplication(TIME_ZONE, RECOMMEND_DAYS_OF_WEEK, RECOMMEND_TIME);
		UpdateApplication ua = new UpdateApplication(DISPOSITION, TERMINATE_TIMESTAMP, EPOCH);
		
		while(true){
			if (this.UPDATE_FLAG) ua.status(false);
			if (this.RECOMMEND_FLAG) ra.status(false);
			
			try {
				Thread.sleep(1000L * 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void runR(String stockid) {
		Calendar now_cal = Calendar.getInstance();
		System.out.println(now_cal.getTime() + " UpdateApplication " + stockid);
		
		initialize();
		RecommendApplication ra = new RecommendApplication(TIME_ZONE, RECOMMEND_DAYS_OF_WEEK, RECOMMEND_TIME);
		try {
			ra.recommend(stockid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runU(String stockid) {
		Calendar now_cal = Calendar.getInstance();
		System.out.println(now_cal.getTime() + " RecommendApplication " + stockid);
		
		initialize();
		UpdateApplication ua = new UpdateApplication(DISPOSITION, TERMINATE_TIMESTAMP, EPOCH);
		try {
			ua.update(stockid);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initialize() {
		Path root = Paths.get(System.getProperty("user.dir")).getParent();
		Path propertyPath = Paths.get(root.toString(), 
				"config",
				"xbw.properties");
		
        Properties property = new Properties();
        try {
			property.load(new FileInputStream(propertyPath.toFile()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (property.containsKey("redis_url")){
        	RedisConfig.setRedisURL(property.getProperty("redis_url"));
        }
        
        if (property.containsKey("redis_port")){
        	RedisConfig.setPort(Integer.parseInt(property.getProperty("redis_port")));
        }
        
        if (property.containsKey("redis_timeout")){
        	RedisConfig.setTimeOut(Integer.parseInt(property.getProperty("redis_timeout")));
        }
        
        if (property.containsKey("redis_password")){
        	RedisConfig.setRedisPassword(property.getProperty("redis_password"));
        }
        
        if (property.containsKey("update_flag")) {
        	UPDATE_FLAG = property.getProperty("update_flag").toLowerCase().equals("true");
        }

        if (property.containsKey("recommend_flag")) {
        	RECOMMEND_FLAG = property.getProperty("recommend_flag").toLowerCase().equals("true");
        }
        
        if (property.containsKey("disposition")) {
        	DISPOSITION = Double.parseDouble(property.getProperty("disposition"));
        }
        
        if (property.containsKey("terminate_timestamp")) {
        	TERMINATE_TIMESTAMP = Long.parseLong(property.getProperty("terminate_timestamp"));
        }
        
        if (property.containsKey("recommend_days_of_week")) {
        	RECOMMEND_DAYS_OF_WEEK = property.getProperty("recommend_days_of_week");
        }
        
        if (property.containsKey("time_zone")) {
        	TIME_ZONE = property.getProperty("time_zone");
        }
        
        if (property.containsKey("recommend_time")) {
        	RECOMMEND_TIME = property.getProperty("recommend_time");
        }
        
        if (property.containsKey("epoch")) {
        	EPOCH = Integer.parseInt(property.getProperty("epoch"));
        }
        
        if (property.containsKey("core_pool_size")) {
        	ThreadConfig.setCorePoolSize(Integer.parseInt(property.getProperty("core_pool_size")));
        }
        
        if (property.containsKey("maximum_pool_size")) {
        	ThreadConfig.setMaximumPoolSize(Integer.parseInt(property.getProperty("maximum_pool_size")));
        }
        
        if (property.containsKey("keep_alive_time")) {
        	ThreadConfig.setKeepAliveTime(Integer.parseInt(property.getProperty("keep_alive_time")));
        }
        
        if (property.containsKey("sleep_mill_time")) {
        	ThreadConfig.setSleepMillTime(Integer.parseInt(property.getProperty("sleep_mill_time")));
        }
	}
	
	public static void main(String[] args) throws IOException {
        final Options options = new Options();
		final CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        options.addOption(new Option("u", true, "update stockid model"));
        options.addOption(new Option("r", true, "recommended stockid result"));
        options.addOption(new Option("h", false, "help"));
        
        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            throw new IOException("parser command line error", e);
        }
        
        if (cmd.hasOption("h")) {
        	System.out.println("Usage: xbw.jar [cmd [arg]]\n");
        	for (Option option : options.getOptions()) {
        		System.out.println(option);
        	}
        	return ;
        }
        
        if (cmd.hasOption("u") && cmd.hasOption("r")) {
        	throw new IOException("parser command line error, There can only be one for '-u' and '-r'");
        } else if (cmd.hasOption("u")) {
        	String stockid = cmd.getOptionValue("u");
        	new XBWServers().runU(stockid);
        } else if (cmd.hasOption("r")) {
        	String stockid = cmd.getOptionValue("r");
        	new XBWServers().runR(stockid);
        } else {
        	new XBWServers().run();
        }
	}
}
