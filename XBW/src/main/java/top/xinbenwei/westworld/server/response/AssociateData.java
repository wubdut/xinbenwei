package top.xinbenwei.westworld.server.response;

import java.util.List;

import com.realsight.westworld.tsp.lib.util.Triple;

public class AssociateData {
	private final String stockid;
	private final Integer upnum;
	private final Integer downnum;
	private final List<Triple<Long, Double, Double>> timestamps;
	
	public AssociateData(String stockid, Integer upnum, Integer downnum, List<Triple<Long, Double, Double>> timestamps) {
		this.stockid = stockid;
		this.upnum = upnum;
		this.downnum = downnum;
		this.timestamps = timestamps;
	}

	public String getStockid() {
		return stockid;
	}

	public List<Triple<Long, Double, Double>> getTimestamps() {
		return timestamps;
	}

	public Integer getUpnum() {
		return upnum;
	}

	public Integer getDownnum() {
		return downnum;
	}
}
