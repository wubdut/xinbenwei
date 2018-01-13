package top.xinbenwei.westworld.server.lib.config;

public class ThreadConfig {
	public static int corePoolSize = 5;
	public static int maximumPoolSize = 10;
	public static long keepAliveTime = 2000;
	public static long sleepMillTime = 10000;

	public static void setCorePoolSize(int corePoolSize) {
		ThreadConfig.corePoolSize = corePoolSize;
	}

	public static void setMaximumPoolSize(int maximumPoolSize) {
		ThreadConfig.maximumPoolSize = maximumPoolSize;
	}

	public static void setKeepAliveTime(long keepAliveTime) {
		ThreadConfig.keepAliveTime = keepAliveTime;
	}

	public static void setSleepMillTime(long sleepMillTime) {
		ThreadConfig.sleepMillTime = sleepMillTime;
	}
}
