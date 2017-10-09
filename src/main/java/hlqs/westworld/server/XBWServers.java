package hlqs.westworld.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

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
	private String REDIS_PASSWORD = "";
	private static Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
	
	static {
		logger.setLevel(Level.WARN);
	}
	
	public void run() {
		initialize();
		AssociateApplication aa = new AssociateApplication(REDIS_URL, REDIS_PORT, REDIS_TIMEOUT, REDIS_PASSWORD);
		RecommendApplication ra = new RecommendApplication(REDIS_URL, REDIS_PORT, REDIS_TIMEOUT, REDIS_PASSWORD);
		UpdateApplication ua = new UpdateApplication();
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
	}
	
	public static void main(String[] args) throws IOException {
		new XBWServers().run();
	}
}
