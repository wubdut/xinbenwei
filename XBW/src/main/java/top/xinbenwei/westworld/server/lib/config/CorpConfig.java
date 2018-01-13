package top.xinbenwei.westworld.server.lib.config;

public class CorpConfig {
	public static String corpid = "corpid";
	public static String corpsecret = "corpsecret";
	public static String agentid = "agentid";
	public static String corpurl = "https://corpurl.com";
	public static String vipid = "department_id";
	public static String allid = "department_id";

	
	public static void setCorpid(String corpid) {
		CorpConfig.corpid = corpid;
	}
	
	public static void setCorpsecret(String corpsecret) {
		CorpConfig.corpsecret = corpsecret;
	}
	
	public static void setAgentid(String agentid) {
		CorpConfig.agentid = agentid;
	}
	
	public static void setCorpurl(String corpurl) {
		CorpConfig.corpurl = corpurl;
	}
	
	public static void setVipid(String departmentid) {
		CorpConfig.vipid = departmentid;
	}
	
	public static void setAllid(String departmentid) {
		CorpConfig.allid = departmentid;
	}
}
