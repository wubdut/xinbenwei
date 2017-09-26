
public class Main {

	public static void main(String[] args) throws Exception {
		RedisWriter redisw = new RedisWriter("stock_zset","stock_hash");
		Stock s = new Stock();
		s.setAction(Action.BUY);
		s.setScore(2.3);
		s.setContext("qweqweqweqwe");
		s.setStockId("00001");
		redisw.updateStockData(s);
		redisw.close();
	}

}
