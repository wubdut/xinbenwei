package hlqs.westworld.server.response;

@SuppressWarnings("unused")
public class RecommendData {
	private final String stockid;
	private final String action;
	private final Double score;
	private final Double price;
	
	public RecommendData(String stockid, String action, Double score, Double price) {
		this.stockid = stockid;
		this.action = action;
		this.score = score;
		this.price = price;
	}
}
