package top.xinbenwei.westworld.server.lib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;

public class Util {
	
	private static final String charset = "utf-8";  
	private static Map<String, Object> locks = new HashMap<String, Object>();
	private static Path root = Paths.get(System.getProperty("user.dir"), "model");
	static {
		if (!root.getParent().toFile().exists()){
			root.getParent().toFile().mkdirs();
		}
	}
	
	public static boolean modifyAccess(String stock_id) {
		Path mode_path = Paths.get(root.toString(), stock_id);
		if (!root.toFile().exists()){
			root.toFile().mkdirs();
		}
		return !mode_path.toFile().exists();
	}
			
	public static void save(String stock_id, OnlineStockStrategyAPI ossAPI) throws FileNotFoundException, IOException {
		Object lock = locks.getOrDefault(stock_id, new Object());
		Path mode_path = Paths.get(root.toString(), stock_id);
		if (!root.getParent().toFile().exists()){
			root.getParent().toFile().mkdirs();
		}
		synchronized(lock){
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(mode_path.toFile()));
			out.writeObject(ossAPI);
			out.close();
        }
	}
	
	public static OnlineStockStrategyAPI open(String stock_id) throws FileNotFoundException, IOException, ClassNotFoundException {
		Object lock = locks.getOrDefault(stock_id, new Object());
		Path mode_path = Paths.get(root.toString(), stock_id);
		if (!root.getParent().toFile().exists()){
			root.getParent().toFile().mkdirs();
		}
		OnlineStockStrategyAPI ossAPI = null;
		if (!mode_path.toFile().exists())
			return null;
		synchronized(lock){
			ObjectInputStream in = new ObjectInputStream(
					new FileInputStream(mode_path.toFile()));
			ossAPI = (OnlineStockStrategyAPI) in.readObject();
			in.close();
        }
		return ossAPI;
	}
	
	public static String HttpGET(String url) throws ClientProtocolException, IOException {
		CloseableHttpClient client =  HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		CloseableHttpResponse resp = client.execute(get);
		
		if (resp.getStatusLine().getStatusCode() != 200)
			return null;
		
		HttpEntity entity = resp.getEntity();
		Scanner sin = new Scanner(EntityUtils.toString(entity));
		entity.getContent();
		
		StringBuffer res = new StringBuffer();
		while(sin.hasNext()) {
			res.append(sin.nextLine());
		}
		
		sin.close();
		resp.close();
		client.close();
		
		return res.toString();
	}
	
	public static String HttpPOST(String url, Map<String, Object> params) throws ClientProtocolException, IOException {
		return HttpPOST(url, params, charset);
	}
	
	public static String HttpPOST(String url, Map<String, Object> params, String charset) throws ClientProtocolException, IOException {
		CloseableHttpClient client =  HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(url);
        StringEntity se = new StringEntity(new Gson().toJson(params), charset);
        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        post.setEntity(se);
        CloseableHttpResponse resp = client.execute(post);	
		if (resp.getStatusLine().getStatusCode() != 200)
			return null;
		
		HttpEntity entity = resp.getEntity();
		Scanner sin = new Scanner(EntityUtils.toString(entity));
		entity.getContent();
		
		StringBuffer res = new StringBuffer();
		while(sin.hasNext()) {
			res.append(sin.nextLine());
		}
		
		sin.close();
		resp.close();
		client.close();
		
		return res.toString();
	}
}
