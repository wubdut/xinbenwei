package hlqs.westworld.server.lib;

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

import com.realsight.westworld.tsp.api.OnlineStockStrategyAPI;

public class Util {
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
}
