package cn.com.baicdata.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.adp.java.AdPlan;
import com.adp.java.PlanStatus;
import com.adp.java.ReportFormService;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Db {
	private static final byte BID = 0x20; // 32
	private static final byte BIDRES = 0x30; // 48
	private static final byte CREATIVE = 0x38; // 56
	private static final byte SHOW = 0x3c; // 60
	private static final byte CLICK = 0x3e; // 62
	private int readafter = 0;
	private int saveafter = 0;
	private String url = "jdbc:mysql://%s:33966/adp?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false";
	private String user = "baicdata";
	private String password = "j8verXE8WZnDNXPV";
	private Connection conn = null;
	private HashMap<String, CacheNode> cache; // 推送状态缓存 push_id => status, host,
												// ad_space, bid_price
	private HashMap<String, String[]> adcache; // 广告信息缓存
	private HashMap<Integer, Double> UserCD;
	private HashMap<Integer, Double> PlanCD;
	private String mongohost;
	private String mysqlhost;

	public Db(int readafter, int saveafter, String host1, String host2) {
		this.readafter = readafter;
		this.saveafter = saveafter;
		this.cache = new HashMap<String, CacheNode>();
		this.adcache = new HashMap<String, String[]>();
		this.UserCD = new HashMap<Integer, Double>();
		this.PlanCD = new HashMap<Integer, Double>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.conn = DriverManager.getConnection(
					String.format(this.url, host1), this.user, this.password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.mysqlhost = host1;
		this.mongohost = host2;
	}

	public double GetBidprice(String pushid) {
		if (this.cache.containsKey(pushid)) {
			return this.cache.get(pushid).getBidprice();
		} else {
			return 0;
		}
	}

	public String[] GetAdInfo(String id) {
		if (this.adcache.containsKey(id)) {
			return this.adcache.get(id);
		} else {
			int i = 0;
			String a = "";
			for (i = 0; i < id.length() && id.charAt(i) == '0'; i++)
				;
			if (i == id.length())
				a = "0";
			else
				a = id.substring(i);
			String adid = "";
			for (i = 0; i < a.length(); i++)
				if (a.charAt(i) <= '9' && a.charAt(i) >= '0')
					adid += a.charAt(i);
			if (adid.equals(""))
				adid = "0";
			String[] ret = { adid, "", "", "", "" }; // adid, uid, plan_id,
														// group_id, stuffid
			try {
				String sql = String
						.format("select a.adid as adid, a.uid as uid, a.group_id as group_id, a.plan_id as plan_id, b.stuff_id as stuffid from adp_ad_info a left join adp_stuff_info b on a.adid = b.adid where a.adid=%s limit 1",
								adid);
				Statement statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					ret[1] = rs.getString("uid");
					ret[2] = rs.getString("plan_id");
					ret[3] = rs.getString("group_id");
					ret[4] = rs.getString("stuffid");
				}
				rs.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.adcache.put(id, ret);
			return ret;
		}
	}

	public void read(String filenames[]) {
		for (String f : filenames) {
			File fp = new File(f);
			if (fp.exists()) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new InputStreamReader(
							new FileInputStream(fp)));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				if (br != null) {
					String line = null;
					try {
						line = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
					while (line != null) {
						String[] segments = line.split("\t");
						if (segments.length == 5) {
							this.cache.put(
									segments[0],
									new CacheNode(Byte.valueOf(segments[1]),
											segments[2], segments[3], Float
													.parseFloat(segments[4])));
						}
						try {
							line = br.readLine();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void save(String filenames[]) {
		List<FileWriter> lf = new ArrayList<FileWriter>();
		for (String f : filenames) {
			try {
				lf.add(new FileWriter(f));
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		for (String pushid : this.cache.keySet()) {
			CacheNode cn = this.cache.get(pushid);
			for (FileWriter fw : lf) {
				try {
					fw.write(String.format("%s\t%d\t%s\t%s\t%.10f\n", pushid,
							cn.getStatus(), cn.getHost(), cn.getAdspace(),
							cn.getBidprice()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		for (FileWriter fw : lf) {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getHost(String id) {
		if (this.cache.containsKey(id)) {
			CacheNode cn = this.cache.get(id);
			return String.format("%s,%s", cn.getHost(), cn.getAdspace());
		} else {
			return ",";
		}
	}

	public void CutDown(int userid, int planid, double cost) {
		if (this.UserCD.containsKey(userid)) {
			this.UserCD.put(userid, this.UserCD.get(userid) + cost);
		} else {
			this.UserCD.put(userid, cost);
		}
		if (this.PlanCD.containsKey(planid)) {
			this.PlanCD.put(planid, this.PlanCD.get(planid) + cost);
		} else {
			this.PlanCD.put(planid, cost);
		}
	}

	public float GetY(String userid) {
		return (float) 0.04;
	}

	public float GetX(String userid) {
		return (float) 0.3;
	}

	private void set(String pushid, byte status) {
		if (!this.cache.containsKey(pushid)) {
			this.cache.put(pushid, new CacheNode(status, "", "", (float) 0.0));
		}
		this.cache.get(pushid).setStatus(status);
	}

	public void BID(String pushid, String host, String adspace, String bidprice) {
		this.set(pushid, Db.BID);
		if (!this.cache.containsKey(pushid)) {
			this.cache.put(pushid, new CacheNode(Db.BID, host, adspace,
					(float) 0.0));
		} else {
			CacheNode r = this.cache.get(pushid);
			r.setHost(host);
			r.setAdspace(adspace);
			try {
				r.setBidprice(Float.parseFloat(bidprice));
			} catch (Exception e) {
				;
			}
		}
	}

	public void BIDRES(String pushid) {
		this.set(pushid, Db.BIDRES);
	}

	public void CREATIVE(String pushid) {
		this.set(pushid, Db.CREATIVE);
	}

	public void SHOW(String pushid) {
		this.set(pushid, Db.SHOW);
	}

	public void CLICK(String pushid) {
		this.set(pushid, Db.CLICK);
	}

	public boolean isValidBid(String pushid) {
		boolean r = !this.cache.containsKey(pushid);
		return r;
	}

	public boolean isValidBidres(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid).getStatus() == Db.BID;
		return r;
	}

	public boolean isValidCreative(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid).getStatus() == Db.BIDRES;
		return r;
	}

	public boolean isValidShow(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid).getStatus() == Db.CREATIVE;
		return r;
	}

	public boolean isValidClick(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid).getStatus() == Db.SHOW;
		return r;
	}

	public void processCutDown() {
		// TODO Auto-generated method stub
		Mongo mongo = null;
		try {
			mongo = new Mongo(this.mongohost, 33458);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		if (mongo != null) {
			for (int userid : this.UserCD.keySet()) {
				System.out.println("userid: "+userid);
				System.out.println("charge: "+this.UserCD.get(userid));
				double account = this.getAccount(userid);
				System.out.println("account before: "+account);
				this.CutDownUser(userid, this.UserCD.get(userid));
				account = this.getAccount(userid);
				System.out.println("account after: "+account);
				
				
				if ( account <= 0) {
					this.StopAllPlan(userid);
					System.out.println("Stop all <uid:"+userid+">");
				} else {
					System.out.println("Do nothing");
				}
				System.out.println();
			}
			System.out.println();
			for (int planid : this.PlanCD.keySet()) {
				System.out.println("planid: "+planid);
				double budget = this.getBudget(planid);
				System.out.println("budget: "+budget);
				
				double daycost = this.getDayCost(mongo, planid);
				
				System.out.println("daycost: "+daycost);
				if (budget >= 0 && daycost > budget) {
					this.StopAPlan(planid);
					System.out.println("Stop a plan <pid:"+planid+">");
				} else {
					System.out.println("do nothing");
				}
				System.out.println();
			}
		}
		mongo.close();
	}

	private double getBudget(int pid) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "select budget from adp_plan_info where plan_id=" + pid;
		System.out.println("["+sql+"]");
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		double budget = -1;
		try {
			while (rs.next())
				budget = rs.getFloat("budget");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return budget;
	}

	private double getAccount(int uid) {
		String sql = "select account from adp_user_info where uid=" + uid;
		Statement statement = null;
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		double account = -1;
		try {
			while (rs.next())
				account = rs.getFloat("account");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return account;
	}

	public double getDayCost(Mongo mongo, int pid) {
		double ret = -1;
		Date d = new Date();
		String today = new SimpleDateFormat("yyyyMMdd").format(d);
		DBCollection col = mongo.getDB("StatV2").getCollection("Detail");
		DBObject query = new BasicDBObject();
		query.put("Planid", pid);
		query.put("Day", Integer.parseInt(today));
		DBObject key = new BasicDBObject();
		key.put("cost", true);
		System.out.println(query);
		System.out.println(key);
		DBCursor b = col.find(query, key);
		ret = 0;
		while (b.hasNext()) {
			DBObject t = b.next();
			Object coco = t.get("cost");
			ret += Double.parseDouble(coco.toString());
		}
		return ret;
	}

	private void StopAPlan(int pid) {
		TTransport transport = null;
		transport = new TSocket(this.mysqlhost, 9098);
		TProtocol protocol = new TBinaryProtocol(transport);
		ReportFormService.Client client = new ReportFormService.Client(protocol);
		try {
			transport.open();
		} catch (TTransportException e1) {
			e1.printStackTrace();
		}
		try {
			System.out.println("stop plan:" + pid + "("
					+ client.updateAdPlanStatus(pid, PlanStatus.STOPPED) + ")");
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (TException e1) {
			e1.printStackTrace();
		}
		transport.close();
	}

	private void StopAllPlan(int uid) {
		TTransport transport = null;
		transport = new TSocket(this.mysqlhost, 9098);
		TProtocol protocol = new TBinaryProtocol(transport);
		ReportFormService.Client client = new ReportFormService.Client(protocol);
		try {
			transport.open();
		} catch (TTransportException e1) {
			e1.printStackTrace();
		}
		ArrayList<String> pids = new ArrayList<String>();
		try {
			List<AdPlan> a = client.getAdPlansByUid(uid);
			for (AdPlan ap : a) {
				pids.add(String.valueOf(ap.plan_id));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		}
		for (String pid : pids) {
			try {
				System.out.println("frozen plan:"
						+ pid
						+ "("
						+ client.updateAdPlanStatus(Integer.parseInt(pid),
								PlanStatus.STOPPED) + ")");
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (TException e1) {
				e1.printStackTrace();
			}
		}
		transport.close();

	}

	private void CutDownUser(int uid, Double charge) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
			String sql = "update adp_user_info set account=account-" + String.format("%s", charge) + " where uid=" + uid;
			System.out.println("["+sql+"]");
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
