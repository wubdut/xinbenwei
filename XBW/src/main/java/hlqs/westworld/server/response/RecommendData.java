package hlqs.westworld.server.response;

@SuppressWarnings("unused")
public class RecommendData {
	private final String stockid;
	private final Double score;
	private final Double price;
	private final long time;
	
	public RecommendData(String stockid, Double score, Double price, long time) {
		this.stockid = stockid;
		this.score = score;
		this.price = price;
		this.time = time;
	}
}
