package hlqs.westworld.server.lib.config;

public class RedisConfig {
	public static String redisURL = "localhost";
	public static int port = 6379;
	public static int timeOut = 60000;
	public static String redisPassword = "myRedis";
	
	public static void setRedisURL(String redisURL) {
		RedisConfig.redisURL = redisURL;
	}
	public static void setPort(int port) {
		RedisConfig.port = port;
	}
	public static void setTimeOut(int timeOut) {
		RedisConfig.timeOut = timeOut;
	}
	public static void setRedisPassword(String redisPassword) {
		RedisConfig.redisPassword = redisPassword;
	}
}
