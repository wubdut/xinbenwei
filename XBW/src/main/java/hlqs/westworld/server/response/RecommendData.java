package hlqs.westworld.server.response;

@SuppressWarnings("unused")
public class RecommendData {
	private final String stockId;
	private final Double score;
	private final Double priceRec;
	private final long timeRec;
	
	public RecommendData(String stockId, Double score, Double priceRec, long timeRec) {
		this.stockId = stockId;
		this.score = score;
		this.priceRec = priceRec;
		this.timeRec = timeRec;
	}
}
