package hlqs.westworld.server.response;

import com.realsight.westworld.tsp.lib.series.DoubleSeries;
import com.realsight.westworld.tsp.lib.util.Triple;

public class ShowData {
	private String name;
	private Double price;
	private Long timestamp;
	
	private DoubleSeries data;
	
	public ShowData(Triple<String, Long, Double> triple, DoubleSeries data) {
		this.name = triple.getFirst();
		this.data = data;
		this.price = triple.getThird();
		this.timestamp = triple.getSecond();
	}
	
	public ShowData(String name, Long timestamp, Double price, DoubleSeries data) {
		this.name = name;
		this.data = data;
		this.price = price;
		this.timestamp = timestamp;
	}

	public String getName() {
		return name;
	}

	public DoubleSeries getData() {
		return data;
	}

	public Double getPrice() {
		return price;
	}

	public Long getTimestamp() {
		return timestamp;
	}
}
