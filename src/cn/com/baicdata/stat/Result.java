package cn.com.baicdata.stat;

public class Result {
	public Record record = null;
	public String type = null;
	public double cost = 0.0d;

	public Result(Record r, String t, double cost) {
		this.record = r;
		this.type = t;
		this.cost = cost;
	}
}
