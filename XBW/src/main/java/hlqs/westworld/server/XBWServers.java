package hlqs.westworld.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

import hlqs.westworld.server.application.RecommendApplication;
import hlqs.westworld.server.application.UpdateApplication;
import hlqs.westworld.server.lib.config.RedisConfig;
import hlqs.westworld.server.lib.config.ThreadConfig;

public class XBWServers {
	private boolean UPDATE_FLAG = false;
	private boolean RECOMMEND_FLAG = false;
	private double DISPOSITION = 2.1;
	private double C = 0.1;
	private double ATTENUATION = 0.5;
	private int TRANSACTION_PERIOD = 5;
	private long TERMINATE_TIMESTAMP = 1496246400000L;
	private String RECOMMEND_TIME = "14:30-15:00";
	private String TIME_ZONE = "GMT+8:00";
	private String RECOMMEND_DAYS_OF_WEEK = "2,3,4,5,6";
	private int EPOCH = 10;
	
	private static final Logger logger = Logger.getLogger(XBWServers.class);
	
	public void run() {
		initialize();
		
		RecommendApplication ra = new RecommendApplication(TIME_ZONE, RECOMMEND_DAYS_OF_WEEK, RECOMMEND_TIME);
		UpdateApplication ua = new UpdateApplication(DISPOSITION, ATTENUATION, C, TRANSACTION_PERIOD, TERMINATE_TIMESTAMP, EPOCH);
		
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
	
	public void initialize() {
		
		logger.info("Start Initialize.");
		
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
        	RedisConfig.setRedisURL(property.getProperty("redis_url").trim());
        }
		logger.info("CONFIG : redis_url = " + RedisConfig.redisURL);
        
        if (property.containsKey("redis_port")){
        	RedisConfig.setPort(Integer.parseInt(property.getProperty("redis_port").trim()));
        }
		logger.info("CONFIG : redis_port = " + RedisConfig.port);
        
        if (property.containsKey("redis_timeout")){
        	RedisConfig.setTimeOut(Integer.parseInt(property.getProperty("redis_timeout").trim()));
        }
		logger.info("CONFIG : redis_timeout = " + RedisConfig.timeOut);
        
        if (property.containsKey("redis_password")){
        	RedisConfig.setRedisPassword(property.getProperty("redis_password").trim());
        }
		logger.info("CONFIG : redis_password = " + RedisConfig.redisPassword);
        
        if (property.containsKey("update_flag")) {
        	UPDATE_FLAG = property.getProperty("update_flag").trim().toLowerCase().equals("true");
        }
		logger.info("CONFIG : update_flag = " + UPDATE_FLAG);

        if (property.containsKey("recommend_flag")) {
        	RECOMMEND_FLAG = property.getProperty("recommend_flag").trim().toLowerCase().equals("true");
        }
		logger.info("CONFIG : recommend_flag = " + RECOMMEND_FLAG);
        
        if (property.containsKey("disposition")) {
        	DISPOSITION = Double.parseDouble(property.getProperty("disposition").trim());
        }
		logger.info("CONFIG : disposition = " + DISPOSITION);
        
        if (property.containsKey("attenuation")) {
        	ATTENUATION = Double.parseDouble(property.getProperty("attenuation").trim());
        }
		logger.info("CONFIG : attenuation = " + ATTENUATION);
        
        if (property.containsKey("C")) {
        	C = Double.parseDouble(property.getProperty("C").trim());
        }
		logger.info("CONFIG : C = " + C);
        
        if (property.containsKey("transaction_period")) {
        	TRANSACTION_PERIOD = Integer.parseInt(property.getProperty("transaction_period").trim());
        }
		logger.info("CONFIG : transaction_period = " + TRANSACTION_PERIOD);
        
        if (property.containsKey("terminate_timestamp")) {
        	TERMINATE_TIMESTAMP = Long.parseLong(property.getProperty("terminate_timestamp").trim());
        }
		logger.info("CONFIG : terminate_timestamp = " + TERMINATE_TIMESTAMP);
        
        if (property.containsKey("recommend_days_of_week")) {
        	RECOMMEND_DAYS_OF_WEEK = property.getProperty("recommend_days_of_week").trim();
        }
		logger.info("CONFIG : recommend_days_of_week = " + RECOMMEND_DAYS_OF_WEEK);
        
        if (property.containsKey("time_zone")) {
        	TIME_ZONE = property.getProperty("time_zone").trim();
        }
		logger.info("CONFIG : time_zone = " + TIME_ZONE);
        
        if (property.containsKey("recommend_time")) {
        	RECOMMEND_TIME = property.getProperty("recommend_time").trim();
        }
		logger.info("CONFIG : recommend_time = " + RECOMMEND_TIME);
        
        if (property.containsKey("epoch")) {
        	EPOCH = Integer.parseInt(property.getProperty("epoch").trim());
        }
		logger.info("CONFIG : epoch = " + EPOCH);
        
        if (property.containsKey("core_pool_size")) {
        	ThreadConfig.setCorePoolSize(Integer.parseInt(property.getProperty("core_pool_size").trim()));
        }
		logger.info("CONFIG : core_pool_size = " + ThreadConfig.corePoolSize);
        
        if (property.containsKey("maximum_pool_size")) {
        	ThreadConfig.setMaximumPoolSize(Integer.parseInt(property.getProperty("maximum_pool_size").trim()));
        }
		logger.info("CONFIG : maximum_pool_size = " + ThreadConfig.maximumPoolSize);
        
        if (property.containsKey("keep_alive_time")) {
        	ThreadConfig.setKeepAliveTime(Integer.parseInt(property.getProperty("keep_alive_time").trim()));
        }
		logger.info("CONFIG : keep_alive_time = " + ThreadConfig.keepAliveTime);
        
        if (property.containsKey("sleep_mill_time")) {
        	ThreadConfig.setSleepMillTime(Integer.parseInt(property.getProperty("sleep_mill_time").trim()));
        }
		logger.info("CONFIG : sleep_mill_time = " + ThreadConfig.sleepMillTime);
	}
	
	public static void main(String[] args) throws IOException {
		new XBWServers().run();
		
//        final Options options = new Options();
//		final CommandLineParser parser = new DefaultParser();
//        CommandLine cmd = null;
//        options.addOption(new Option("u", true, "update stockid model"));
//        options.addOption(new Option("r", true, "recommended stockid result"));
//        options.addOption(new Option("h", false, "help"));
//        
//        try {
//            cmd = parser.parse(options, args);
//        } catch (final ParseException e) {
//            throw new IOException("parser command line error", e);
//        }
//        
//        if (cmd.hasOption("h")) {
//        	System.out.println("Usage: xbw.jar [cmd [arg]]\n");
//        	for (Option option : options.getOptions()) {
//        		System.out.println(option);
//        	}
//        	return ;
//        }
//        
//        if (cmd.hasOption("u") && cmd.hasOption("r")) {
//        	throw new IOException("parser command line error, There can only be one for '-u' and '-r'");
//        } else if (cmd.hasOption("u")) {
//        	String stockid = cmd.getOptionValue("u");
//        	new XBWServers().runU(stockid);
//        } else if (cmd.hasOption("r")) {
//        	String stockid = cmd.getOptionValue("r");
//        	new XBWServers().runR(stockid);
//        } else {
//        	new XBWServers().run();
//        }
	}
}
