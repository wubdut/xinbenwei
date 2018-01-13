package top.xinbenwei.westworld.server.lib;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import ch.qos.logback.classic.Logger;
import top.xinbenwei.westworld.server.lib.config.CorpConfig;
import top.xinbenwei.westworld.server.lib.config.ThreadConfig;

public class WinXinUtil {
	
	private static Logger logger = (Logger) LoggerFactory.getLogger(WinXinUtil.class);
	
	private static final long user_list_mill = 1000 * 60 * 60;
	private static final long error_interval_mill = 5000;
	private static JsonArray user_list = null;
	
	private static final long access_token_mill = 600000;
	private static String access_token = "ACCESS_TOKEN";
	
	private static Thread AccessToken = getAccessTokenThread();
	private static Thread UserList = getUserListThread();
	
	private static final long send_interval_mill = 5000;
	private static final int send_attempts = 10;
	
	private static final int touser_limit = 900;
	private static final int mobile2usersid_attempts = 5;
	
	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(ThreadConfig.corePoolSize, 
			ThreadConfig.maximumPoolSize, 
			ThreadConfig.keepAliveTime, 
			TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(5));
	
	private static Thread getUserListThread() {
		return new Thread() {
	        public void run() {
	        	while(true) {
	        		String user_list_url = String.format("%s/cgi-bin/user/list?access_token=%s&department_id=%s&fetch_child=1",
	        				CorpConfig.corpurl,
	        				access_token,
	        				CorpConfig.allid);
	        		logger.info("User List URL : " + user_list_url);
	        		try {
	        			String json_str = Util.HttpGET(user_list_url);
	        			@SuppressWarnings("serial")
						JsonObject json = new Gson().fromJson(json_str, new TypeToken<JsonObject>() {}.getType());
	        			if (json.get("errmsg").getAsString().equals("ok")) {
	        				user_list = json.get("userlist").getAsJsonArray();
	        				logger.info("User List : " + user_list);
	        			} else {
	        				logger.error("GET User List error. errcode = " + json.get("errcode"));
	        				try {
								Thread.sleep(error_interval_mill);
							} catch (InterruptedException ie) {}
	        				continue;
	        			}
	        			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("GET User List error.");
						try {
							Thread.sleep(error_interval_mill);
						} catch (InterruptedException ie) {}
						continue;
					}
	        		try {
						Thread.sleep(user_list_mill);
					} catch (InterruptedException e) {}
	        	}
	        }
        };
    };
	
	private static Thread getAccessTokenThread() {
		return new Thread() {
	        public void run() {
	        	while(true) {
	        		String token_url = String.format("%s/cgi-bin/gettoken?corpid=%s&corpsecret=%s",
	        				CorpConfig.corpurl,
	        				CorpConfig.corpid, 
	        				CorpConfig.corpsecret);
	        		logger.info("Access Token URL : " + token_url);
	        		try {
	        			String json_str = Util.HttpGET(token_url);
	        			@SuppressWarnings("serial")
	        			JsonObject json = new Gson().fromJson(json_str, new TypeToken<JsonObject>() {}.getType());
	        			if (json.get("errmsg").getAsString().equals("ok")) {
	        				access_token = json.get("access_token").getAsString();
	        				logger.info("Access Token : " + access_token);
	        			} else {
	        				logger.error("GET Access Token error. errcode = " + json.get("errcode"));
	        			}
	        			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.error("GET Access Token error.");
					}
	        		try {
						Thread.sleep(access_token_mill);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
        };
    };
	
    private static Thread getSendMessage2UsersThread(List<String> usersid, String message) {
		return new Thread() {
	        public void run() {
	        	List<String> tousers = new ArrayList<String>();
	        	
	        	int sz = 0;
	        	StringBuffer touser = new StringBuffer();
	        	for (String userid : usersid) {
	        		if (touser.length() > 0) {
	        			touser.append("|");
	        		}
	        		touser.append(userid);
	        		sz += 1;
	        		if (sz >= touser_limit) {
	        			tousers.add(touser.toString());
	        			sz = 0;
	        			touser = new StringBuffer();
	        		}
	        	}
	        	if (sz > 0) {
	        		tousers.add(touser.toString());
	        		sz = 0;
        			touser = new StringBuffer();
	        	}
	        	
        		for (String users : tousers) {
        			for (int i = 0; i < send_attempts; i++) {
		        		String send_url = String.format("%s/cgi-bin/message/send?access_token=%s",
		        				CorpConfig.corpurl,
		        				access_token);
		        		Map<String, Object> params = new HashMap<String, Object>();
		        		Map<String, String> text = new HashMap<String, String>();
		        		text.put("content", message);
		        		params.put("touser", users);
	        			params.put("msgtype", "text");
	        			params.put("agentid", CorpConfig.agentid);
	        			params.put("text", text);
	        			params.put("safe", "1");
		        		logger.info("SEND URL : " + send_url + ", params : " + new Gson().toJson(params));
		        		try {
		        			String json_str = Util.HttpPOST(send_url, params);
		        			@SuppressWarnings("serial")
		        			JsonObject json = new Gson().fromJson(json_str, new TypeToken<JsonObject>() {}.getType());
		        			if (json.get("errmsg").getAsString().equals("ok")) {
		        				logger.info("SEND TO " + users + " : " + message + ", SUCCESS");
		        				break;
		        			} else {
		        				logger.error("SEND TO " + users + " : " + message + " error, attempts " + (i+1) +". errcode = " + json.get("errcode"));
		        			}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.info("SEND TO " + users + " : " + message + " error, attempts " + (i+1) +". ");
						}
		        		try {
							Thread.sleep(send_interval_mill);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	        		}
	        	}
	        }
        };
    };
        
    private static Thread getSendMessage2AllUsersThread(String message) {
		return new Thread() {
	        public void run() {
	        	for (int i = 0; i < send_attempts; i++) {
	        		String send_url = String.format("%s/cgi-bin/message/send?access_token=%s",
	        				CorpConfig.corpurl,
	        				access_token);
	        		Map<String, Object> params = new HashMap<String, Object>();
	        		Map<String, String> text = new HashMap<String, String>();
	        		text.put("content", message);
	        		params.put("touser", "@all");
        			params.put("msgtype", "text");
        			params.put("agentid", CorpConfig.agentid);
        			params.put("text", text);
        			params.put("safe", "1");
	        		logger.info("SEND URL : " + send_url + ", params : " + new Gson().toJson(params));
	        		try {
	        			String json_str = Util.HttpPOST(send_url, params);
	        			@SuppressWarnings("serial")
	        			JsonObject json = new Gson().fromJson(json_str, new TypeToken<JsonObject>() {}.getType());
	        			if (json.get("errmsg").getAsString().equals("ok")) {
	        				logger.info("SEND TO All Users : " + message + " SUCCESS");
	        				break;
	        			} else {
	        				logger.error("SEND TO All Users : " + message + " error, attempts " + (i+1) +". errcode = " + json.get("errcode"));
	        			}
	        			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.info("SEND TO All Users : " + message + " error, attempts " + (i+1) +". ");
						e.printStackTrace();
					}
	        		try {
						Thread.sleep(send_interval_mill);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
        };
    };
    
    private static Thread getSendMessage2VIPUsersThread(String message) {
		return new Thread() {
	        public void run() {
	        	for (int i = 0; i < send_attempts; i++) {
	        		String send_url = String.format("%s/cgi-bin/message/send?access_token=%s",
	        				CorpConfig.corpurl,
	        				access_token);
	        		Map<String, Object> params = new HashMap<String, Object>();
	        		Map<String, String> text = new HashMap<String, String>();
	        		text.put("content", message);
	        		params.put("toparty", CorpConfig.vipid);
        			params.put("msgtype", "text");
        			params.put("agentid", CorpConfig.agentid);
        			params.put("text", text);
        			params.put("safe", "1");
	        		logger.info("SEND URL : " + send_url + ", params : " + new Gson().toJson(params));
	        		try {
	        			String json_str = Util.HttpPOST(send_url, params);
	        			@SuppressWarnings("serial")
	        			JsonObject json = new Gson().fromJson(json_str, new TypeToken<JsonObject>() {}.getType());
	        			if (json.get("errmsg").getAsString().equals("ok")) {
	        				logger.info("SEND TO VIP : " + message + " SUCCESS");
	        				break;
	        			} else {
	        				logger.error("SEND TO VIP : " + message + " error, attempts " + (i+1) +". errcode = " + json.get("errcode"));
	        			}
	        			
					} catch (IOException e) {
						// TODO Auto-generated catch block
						logger.info("SEND  TO VIP : " + message + " error, attempts " + (i+1) +". ");
						e.printStackTrace();
					}
	        		try {
						Thread.sleep(send_interval_mill);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
        };
    };
    
    public static List<String> mobile2Usersid(String mobile) throws RuntimeException {
    	if (user_list == null) {
    		logger.error("user_list is null");
    		throw new RuntimeException("user_list is null");
    	}
    	List<String> usersid = new ArrayList<String>();
    	for ( int i = 0; i < user_list.size(); i++) {
    		JsonObject json = user_list.get(i).getAsJsonObject();
    		if ( !json.has("mobile") ) {
    			logger.error(json.get("userid") + " don't has mobile attribute.");
    			continue;
    		}
    		if ( json.get("mobile").getAsString().equals(mobile) ) {
    			usersid.add(json.get("userid").getAsString());
    		}
    	}
    	logger.info("Mobile = " + mobile + ", usersid = " + usersid);
    	return usersid;
    }
    
	public static void runAccessToken() {
		if (!AccessToken.isAlive()) {
			AccessToken = getAccessTokenThread();
			AccessToken.start();
		}
	}
	
	public static void runUserList() {
		if (!UserList.isAlive()) {
			UserList = getUserListThread();
			UserList.start();
		}
	}
	
	public static void SendMessage2Users(List<String> usersid, String message) {
		runAccessToken();
		runUserList();
				
		Thread send = getSendMessage2UsersThread(usersid, message);
		executor.execute(send);
	}
	
	public static void SendMessage2User(String userid, String message) {
		runAccessToken();
		runUserList();
		
		List<String> usersid = new ArrayList<String>();
		usersid.add(userid);
		SendMessage2Users(usersid, message);
	}
	
	public static void SendMessage2Mobiles(List<String> mobiles, String message) {
		runAccessToken();
		runUserList();
		
		List<String> usersid = new ArrayList<String>();
		for (String mobile : mobiles) {
			for (int i = 0; i < mobile2usersid_attempts; i++){
				try {
					usersid.addAll(mobile2Usersid(mobile));
					break;
				} catch (RuntimeException e) {
					try {
						Thread.sleep(error_interval_mill);
					} catch (InterruptedException ie) {}
				};
			}
		}
		SendMessage2Users(usersid, message);
	}
	
	public static void SendMessage2Mobile(String mobile, String message) {
		runAccessToken();
		runUserList();

		List<String> mobiles = new ArrayList<String>();
		mobiles.add(mobile);
		SendMessage2Mobiles(mobiles, message);
	}
	
	public static void sendMessage2AllUsers(String message) {
		runAccessToken();
		runUserList();
		
		Thread send = getSendMessage2AllUsersThread(message);
		executor.execute(send);
	}
	
	public static void sendMessage2VIPUsers(String message) {
		runAccessToken();
		runUserList();
		
		Thread send = getSendMessage2VIPUsersThread(message);
		executor.execute(send);
	}
}
