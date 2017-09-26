import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MyJedisPool {
	private JedisPool jedisPool;
	private JedisPoolConfig config = new JedisPoolConfig();
	private final String host;
	private final int port;
	private final int timeOut;
	private String password;
	public MyJedisPool(String host,int port,int timeOut,String password){
		config.setMaxTotal(5);
		config.setMaxIdle(2);
		config.setMaxWaitMillis(6000);//6s
		config.setTestOnReturn(true);
		config.setTestOnBorrow(true);
		this.host = host;
		this.port = port;
		this.timeOut = timeOut;
		this.password = password;
		this.jedisPool = new JedisPool(this.config,this.host,this.port,this.timeOut,this.password);
	}

	
	public String getPassword() {
		return password;
	}

	public Jedis getJedis(){
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
}
