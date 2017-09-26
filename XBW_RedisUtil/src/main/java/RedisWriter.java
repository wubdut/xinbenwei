
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/*
 * 向red is中写数据
 * 
 */
public class RedisWriter implements AutoCloseable {

	private String zsetName;
	private String hashName;
	private MyJedisPool jedisPool ;
	public RedisWriter(String zsetName,String hashName){
		this.zsetName = zsetName;
		this.hashName = hashName;
		jedisPool= new MyJedisPool("39.108.214.220",6379,6000,"myRedis");
	}
	
	public String getZsetName() {
		return zsetName;
	}

	public void setZsetName(String zsetName) {
		this.zsetName = zsetName;
	}

	public String getHashName() {
		return hashName;
	}

	public void setHashName(String hashName) {
		this.hashName = hashName;
	}
	
	void updateStockData(Stock stock) {
			Jedis jedis = this.jedisPool.getJedis();
			Transaction t = jedis.multi();
			double score = stock.getAction().getValue()*(10000000)+stock.getScore();
			t.zadd(this.zsetName, score,stock.getStockId());
			t.hset(hashName, stock.getStockId(), stock.getContext());
			t.exec();
	}

	public void close() throws Exception {
		
	}
}
