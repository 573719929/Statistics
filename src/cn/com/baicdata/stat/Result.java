package cn.com.baicdata.stat;

public class Result {
	public String record = null;
	public String type = null;
	public double cost = 0.0d;
	public double fee = 0.0d;
	public Result(String r, String t, double cost, double fee) {
		this.record = r;
		this.type = t;
		this.cost = cost;
		this.fee = fee;
	}
}
