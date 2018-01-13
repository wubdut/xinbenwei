package top.xinbenwei.westworld.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import top.xinbenwei.westworld.server.application.RecommendApplication;
import top.xinbenwei.westworld.server.application.SendMessageApplication;
import top.xinbenwei.westworld.server.application.UpdateApplication;
import top.xinbenwei.westworld.server.lib.WinXinUtil;
import top.xinbenwei.westworld.server.lib.config.CorpConfig;
import top.xinbenwei.westworld.server.lib.config.RedisConfig;
import top.xinbenwei.westworld.server.lib.config.ThreadConfig;
import ch.qos.logback.classic.Level;

public class XBWServers {
	private boolean UPDATE_FLAG = false;
	private boolean RECOMMEND_FLAG = false;
	private boolean WEIXIN_FLAG = false;
	private double DISPOSITION = 2.1;
	private double C = 0.1;
	private double ATTENUATION = 0.5;
	private int TRANSACTION_PERIOD = 5;
	private long TERMINATE_TIMESTAMP = 1496246400000L;
	private String RECOMMEND_TIME = "14:30-15:00";
	private String TIME_ZONE = "GMT+8:00";
	private String RECOMMEND_DAYS_OF_WEEK = "2,3,4,5,6";
	private int EPOCH = 10;

	private static Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

	static {
		Path root = Paths.get(System.getProperty("user.dir")).getParent();
		Path configPath = Paths.get(root.toString(), "config", "logback.xml");
	    LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

	    if (configPath.toFile().exists() 
	    		&& configPath.toFile().isFile() 
	    		&& configPath.toFile().canRead()) {
			JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(lc);
            lc.reset();
            try {
				configurator.doConfigure(configPath.toString());
			} catch (JoranException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	    }
		logger.setLevel(Level.INFO);
	}

	public void run() {
		initialize();

		SendMessageApplication sma = new SendMessageApplication();
		RecommendApplication ra = new RecommendApplication(TIME_ZONE, RECOMMEND_DAYS_OF_WEEK, RECOMMEND_TIME);
		UpdateApplication ua = new UpdateApplication(DISPOSITION, ATTENUATION, C, TRANSACTION_PERIOD,
				TERMINATE_TIMESTAMP, EPOCH);

		while (true) {
			if (this.UPDATE_FLAG)
				ua.status(false);
			if (this.RECOMMEND_FLAG)
				ra.status(false);
			if (this.WEIXIN_FLAG)
				sma.status(false);
			try {
				Thread.sleep(1000L * 60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void initialize() {

		logger.info("INITIALIZE");

		Path root = Paths.get(System.getProperty("user.dir")).getParent();
		Path propertyPath = Paths.get(root.toString(), "config", "xbw.properties");

		Properties property = new Properties();
		try {
			property.load(new FileInputStream(propertyPath.toFile()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (property.containsKey("redis_url")) {
			RedisConfig.setRedisURL(property.getProperty("redis_url").trim());
		}
		logger.info("CONFIG : redis_url = " + RedisConfig.redisURL);

		if (property.containsKey("redis_port")) {
			RedisConfig.setPort(Integer.parseInt(property.getProperty("redis_port").trim()));
		}
		logger.info("CONFIG : redis_port = " + RedisConfig.port);

		if (property.containsKey("redis_timeout")) {
			RedisConfig.setTimeOut(Integer.parseInt(property.getProperty("redis_timeout").trim()));
		}
		logger.info("CONFIG : redis_timeout = " + RedisConfig.timeOut);

		if (property.containsKey("redis_password")) {
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
		
		if (property.containsKey("weixin_flag")) {
			WEIXIN_FLAG = property.getProperty("weixin_flag").trim().toLowerCase().equals("true");
		}
		logger.info("CONFIG : weixin_flag = " + WEIXIN_FLAG);

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
		
		if (property.containsKey("corpid")) {
			CorpConfig.setCorpid(property.getProperty("corpid").trim());
		}
		logger.info("CONFIG : corpid = " + CorpConfig.corpid);
		
		if (property.containsKey("corpsecret")) {
			CorpConfig.setCorpsecret(property.getProperty("corpsecret").trim());
		}
		logger.info("CONFIG : corpsecret = " + CorpConfig.corpsecret);
		
		if (property.containsKey("agentid")) {
			CorpConfig.setAgentid(property.getProperty("agentid").trim());
		}
		logger.info("CONFIG : agentid = " + CorpConfig.agentid);
		
		if (property.containsKey("corpurl")) {
			CorpConfig.setCorpurl(property.getProperty("corpurl").trim());
		}
		logger.info("CONFIG : corpurl = " + CorpConfig.corpurl);
		
		if (property.containsKey("vipid")) {
			CorpConfig.setVipid(property.getProperty("vipid").trim());
		}
		logger.info("CONFIG : vipid = " + CorpConfig.vipid);
		
		if (property.containsKey("allid")) {
			CorpConfig.setAllid(property.getProperty("allid").trim());
		}
		logger.info("CONFIG : allid = " + CorpConfig.allid);
		
		WinXinUtil.runAccessToken();
		logger.info("CONFIG : Run AccessToken.");
		
		WinXinUtil.runUserList();
		logger.info("CONFIG : Run UserList.");
	}

	public static void main(String[] args) throws IOException {
		new XBWServers().run();
	}
}
