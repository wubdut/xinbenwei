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
import hlqs.westworld.server.application.AssociateApplication;
import hlqs.westworld.server.application.RecommendApplication;
import hlqs.westworld.server.application.UpdateApplication;

public class XBWServers {
	private String REDIS_URL = null;
	private int REDIS_PORT = -1;
	private int REDIS_TIMEOUT = 6000;
	private boolean UPDATE_FLAG = false;
	private boolean ASSOCIATE_FLAG = false;
	private boolean RECOMMEND_FLAG = false;
	private double DISPOSITION = 2.0;
	private String REDIS_PASSWORD = "";
	private static Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	
	static {
		logger.setLevel(Level.WARN);
	}
	
	public void run() {
		initialize();
		AssociateApplication aa = new AssociateApplication(REDIS_URL, REDIS_PORT, REDIS_TIMEOUT, REDIS_PASSWORD);
		RecommendApplication ra = new RecommendApplication(REDIS_URL, REDIS_PORT, REDIS_TIMEOUT, REDIS_PASSWORD);
		UpdateApplication ua = new UpdateApplication(DISPOSITION);
		while(true){
			if (this.UPDATE_FLAG) ua.status(false);
			if (this.RECOMMEND_FLAG) ra.status(false);
			if (this.ASSOCIATE_FLAG) aa.status(false);
			
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
		RecommendApplication ra = new RecommendApplication(REDIS_URL, REDIS_PORT, REDIS_TIMEOUT, REDIS_PASSWORD);
		try {
			String info = ra.recommend(stockid);
			System.out.println(stockid + " RecommendApplication result is " + info);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void runU(String stockid) {
		Calendar now_cal = Calendar.getInstance();
		System.out.println(now_cal.getTime() + " RecommendApplication " + stockid);
		
		initialize();
		UpdateApplication ua = new UpdateApplication(DISPOSITION);
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
        	REDIS_URL = property.getProperty("redis_url");
        }
        
        if (property.containsKey("redis_port")){
        	REDIS_PORT = Integer.parseInt(property.getProperty("redis_port"));
        }
        
        if (property.containsKey("redis_timeout")){
        	REDIS_TIMEOUT = Integer.parseInt(property.getProperty("redis_timeout"));
        }
        
        if (property.containsKey("redis_password")){
        	REDIS_PASSWORD = property.getProperty("redis_password");
        }
        
        if (property.containsKey("update_flag")) {
        	UPDATE_FLAG = property.getProperty("update_flag").toLowerCase().equals("true");
        }
        
        if (property.containsKey("associate_flag")) {
        	ASSOCIATE_FLAG = property.getProperty("associate_flag").toLowerCase().equals("true");
        }

        if (property.containsKey("recommend_flag")) {
        	RECOMMEND_FLAG = property.getProperty("recommend_flag").toLowerCase().equals("true");
        }
        
        if (property.containsKey("disposition")) {
        	DISPOSITION = Double.parseDouble(property.getProperty("disposition"));
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
