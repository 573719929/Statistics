package cn.com.baicdata.stat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.adp.java.AdPlan;
import com.adp.java.PlanStatus;
import com.adp.java.ReportFormService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Db {

	private static final String BID = "\u0020";
	private static final String BIDRES = "\u0030";
	private static final String CREATIVE = "\u0038";
	private static final String SHOW = "\u003c";
	private static final String CLICK = "\003e";
	
	private static final byte _B_BID = '\u0020'; 	 //'\u0020'; 	// 0000 0000 0010 0000
	private static final byte _B_BIDRES = '\u0010';	 //'\u0030';	// 0000 0000 0001 0000
	private static final byte _B_CREATIVE = '\u0008';//'\u0038'; 	// 0000 0000 0000 1000
	private static final byte _B_SHOW = '\u0004';	 //'\u003c'; 	// 0000 0000 0000 0100
	private static final byte _B_CLICK = '\u0002';	 //'\u003e'; 	// 0000 0000 0000 0010

	private String url = "jdbc:mysql://%s:33966/adp?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false";
	private String user = "baicdata";
	private String password = "j8verXE8WZnDNXPV";
	private Connection conn = null;
	// æŽ¨é€�çŠ¶æ€�ç¼“å­˜ push_id => status, host, ad_space, bid_price
	private HashMap<Integer, int[]> adcache; // å¹¿å‘Šä¿¡æ�¯ç¼“å­˜
	private HashMap<Integer, Double> UserCD;
	private HashMap<Integer, Double> PlanCD;
	private HashMap<Integer, float[]> RateCache;

	private HashMap<Integer, float[]> OwnerCache;
	private String mongohost;
	private String mysqlhost;
	public Jedis jedis;
	public Jedis j2;
	private Pipeline pipeline;

	public Db(String host1, String host2, String redis) {
		this.adcache = new HashMap<Integer, int[]>();
		this.UserCD = new HashMap<Integer, Double>();
		this.PlanCD = new HashMap<Integer, Double>();
		this.RateCache = new HashMap<Integer, float[]>();
		this.OwnerCache = new HashMap<Integer, float[]>();

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
		this.jedis = new Jedis(redis, 61933);
		this.j2 = new Jedis(redis, 61934);
		this.pipeline = this.jedis.pipelined();
		this.mysqlhost = host1;
		this.mongohost = host2;
	}

	public float GetBidprice(String pushid) {
		try {
			return Float.parseFloat(this.jedis.lrange(pushid, 3, 3).get(0));
		} catch (Exception e) {
			return -1;
		}
	}

	public float[] getOwner(int adid) {
		if(this.OwnerCache.containsKey(adid)) {
			return this.OwnerCache.get(adid);
		} else {
			Statement statement = null;
			ResultSet rs = null;
			try {
				int uid = -1, utype = -1;
				float cpm = -1, cpc = -1;
				statement = this.conn.createStatement();
				String sql = String.format(
						"select a.uid as id,charge_type,cpm_charge,cpc_charge from adp_ad_info a left join adp_user_info b on a.uid=b.uid where adid=%d limit 1",
						adid);
				rs = statement.executeQuery(sql);
				while (rs.next()) {
					uid = rs.getInt("id");
					utype = rs.getInt("charge_type");
					cpm = rs.getFloat("cpm_charge");
					cpc = rs.getFloat("cpc_charge");
				}
				rs.close();
				statement.close();
				float[] rett = new float[]{uid, utype, cpm, cpc};
				this.OwnerCache.put(adid, rett);
				return rett;
			} catch (SQLException e1) {
//				 e1.printStackTrace();
				return new float[]{-1, -1, -1, -1};
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e1) {
//						 e1.printStackTrace();
					}
				}
				if (statement != null) {
					try {
						statement.close();
					} catch (SQLException e1) {
//						 e1.printStackTrace();
					}
				}
			}
		}
	}

	public int[] GetAdInfo(int adid) {
		int[] ret = this.adcache.get(adid);
		if (ret != null) {
			return ret;
		} else {
			ret = new int[] { adid, 0, 0, 0, 0 }; // adid, uid, plan_id,
													// group_id, stuffid
			try {
				String sql = String
						.format("select a.adid as adid, a.uid as uid, a.group_id as group_id, a.plan_id as plan_id, b.stuff_id as stuffid from adp_ad_info a left join adp_stuff_info b on a.adid = b.adid where a.adid=%s limit 1",
								adid);
				Statement statement = this.conn.createStatement();
				ResultSet rs = statement.executeQuery(sql);
				while (rs.next()) {
					ret[1] = rs.getInt("uid");
					ret[2] = rs.getInt("plan_id");
					ret[3] = rs.getInt("group_id");
					ret[4] = rs.getInt("stuffid");
				}
				rs.close();
				statement.close();
			} catch (SQLException e1) {
//				 e1.printStackTrace();
			}
			this.adcache.put(adid, ret);
			return ret;
		}
	}

	public String getHost(String id) {
		try {
//			System.out.println(id);
			List<String> r2 = this.jedis.lrange(id, 0, 5);
			String[] r = new String[5];
			for (int i = 0; i < 5; i++) {
				r[i] = r2.get(i);
			}
			return String.format("%s,%s,%s", r[1], r[2], r[4]);
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}

	public void CutDown(int userid, int planid, double cost) {
		try {
			this.UserCD.put(userid, this.UserCD.get(userid) + cost);
		} catch (Exception e) {
			this.UserCD.put(userid, cost);
		}
		try {
			this.PlanCD.put(planid, this.PlanCD.get(planid) + cost);
		} catch (Exception e) {
			this.PlanCD.put(planid, cost);
		}
	}

	public float[] GetRate(int userid) {
		float ret[] = this.RateCache.get(userid);
		if (ret != null) {
			return ret;
		} else {
			Statement statement = null;
			try {
				statement = conn.createStatement();
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			String sql = "select diffrate, supportfee from adp_user_info where uid="
					+ userid + " limit 1";
			ResultSet rs = null;
			try {
				rs = statement.executeQuery(sql);
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			try {
				while (rs.next()) {
					ret = new float[] { rs.getFloat("diffrate"),
							rs.getFloat("supportfee") };
				}
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			try {
				statement.close();
			} catch (SQLException e1) {
				// e1.printStackTrace();
			}
			if (ret == null) {
				return new float[] { 0, 0 };
			} else {
				this.RateCache.put(userid, ret);
				return ret;
			}
		}
	}

	public void BID(String pushid, String host, String adspace, String bidprice, String info) {
		try {
			// host, view_type, view_location, size, rtb_source
			// into = view_type, view_location, size, rtb_source
			this.pipeline.del(pushid);
			this.pipeline.rpush(pushid, Db.BID);
			this.pipeline.rpush(pushid, host);
			this.pipeline.rpush(pushid, adspace);
			this.pipeline.rpush(pushid, bidprice);
			this.pipeline.rpush(pushid, info);
			this.pipeline.expire(pushid, 360);
//			System.out.println(pushid + " Saved");
//			this.SYNC();
//			System.out.println(this.jedis.lrange(pushid, 0, 99));
//			System.out.println(Arrays.toString(this.jedis.lrange(pushid, 0, 99).toArray()));
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public void SYNC() {
		try {
			this.pipeline.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.pipeline = this.jedis.pipelined();
	}

	public void BIDRES(String pushid) {
		try {
			this.jedis.expire(pushid, 360);
			String c =this.jedis.lpop(pushid);
			this.jedis.lpush(pushid, new String(new byte[]{(byte) (Db._B_BIDRES | c.getBytes()[0])}));
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public void CREATIVE(String pushid) {
		try {
			this.jedis.expire(pushid, 360);
			String c =this.jedis.lpop(pushid);
			this.jedis.lpush(pushid, new String(new byte[]{(byte) (Db._B_CREATIVE | c.getBytes()[0])}));
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public void SHOW(String pushid) {
		try {
			this.jedis.expire(pushid, 360);
			String c =this.jedis.lpop(pushid);
			this.jedis.lpush(pushid, new String(new byte[]{(byte) (Db._B_SHOW | c.getBytes()[0])}));
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public void CLICK(String pushid) {
		try {
			this.jedis.expire(pushid, 10);
			String c =this.jedis.lpop(pushid);
			this.jedis.lpush(pushid, new String(new byte[]{(byte) (Db._B_CLICK | c.getBytes()[0])}));
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	public boolean isValidBid(String pushid) {
		return !this.jedis.exists(pushid);
	}

	public boolean isValidBidres(String pushid) {
		try {
			byte c = this.jedis.lrange(pushid, 0, 1).get(0).getBytes()[0];
			return (c & Db._B_BIDRES) == 0;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean isValidCreative(String pushid) {
		try {
			byte c = this.jedis.lrange(pushid, 0, 1).get(0).getBytes()[0];
			return (c & Db._B_CREATIVE) == 0;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean isValidShow(String pushid) {
		try {
			byte c = this.jedis.lrange(pushid, 0, 1).get(0).getBytes()[0];
			return (c & Db._B_SHOW) == 0;
		} catch (Exception e) {
			return true;
		}
	}

	public boolean isValidClick(String pushid) {
		try {
			byte c = this.jedis.lrange(pushid, 0, 1).get(0).getBytes()[0];
			return (c & Db._B_CLICK) == 0;
		} catch (Exception e) {
			return true;
		}
	}

	public void processCutDown() {
		// TODO Auto-generated method stub
		Mongo mongo = null;
		try {
			mongo = new Mongo(this.mongohost, 12331);
		} catch (UnknownHostException e) {
			// e.printStackTrace();
		}
		if (mongo != null) {
			for (int userid : this.UserCD.keySet()) {
				this.CutDownUser(userid, this.UserCD.get(userid));
				double account = this.getAccount(userid);
				if (account <= 0) {
					this.StopAllPlan(userid);
					// System.out.println("Stop all <uid:" + userid + ">");
				}
			}
			for (int planid : this.PlanCD.keySet()) {
				double budget = this.getBudget(planid), daycost = this
						.getDayCost(mongo, planid);
				if (budget >= 0 && daycost > budget) {
					this.StopAPlan(planid);
					// System.out.println("Stop a plan <pid:" + planid + ">");
				}
			}
		}
		mongo.close();
	}

	private double getBudget(int pid) {
		Statement statement = null;
		try {
			statement = conn.createStatement();
		} catch (SQLException e1) {
			// e1.printStackTrace();
		}
		String sql = "select budget from adp_plan_info where plan_id=" + pid;
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e1) {
			// e1.printStackTrace();
		}
		double budget = -1;
		try {
			while (rs.next())
				budget = rs.getFloat("budget");
		} catch (SQLException e) {
			// e.printStackTrace();          /* Exception in thread "main" java.lang.AbstractMethodError: java.lang.Exception.printStackTrace()V */
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return budget;
	}

	private double getAccount(int uid) {
		String sql = "select account from adp_user_info where uid=" + uid + " limit 1";
		Statement statement = null;
		try {
			statement = conn.createStatement();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		ResultSet rs = null;
		try {
			rs = statement.executeQuery(sql);
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		double account = -1;
		try {
			while (rs.next())
				account = rs.getFloat("account");
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		try {
			rs.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		return account;
	}

	public double getDayCost(Mongo mongo, int pid) {
		Date d = new Date();
		String today = new SimpleDateFormat("yyyyMMdd").format(d);
		DBCollection col = mongo.getDB(Statd.DATABASE).getCollection(
				Statd.HourDetail);
		DBObject query = new BasicDBObject();
		query.put(Statd.PLANID, pid);
		query.put(Statd.DAY, Integer.parseInt(today));
		DBObject key = new BasicDBObject();
		key.put(Statd.COST, true);
		DBCursor b = col.find(query, key);
		double ret = 0;
		while (b.hasNext())
			ret += Double.parseDouble(b.next().get(Statd.COST).toString());
		return ret;
	}

	private void StopAPlan(int pid) {
		TTransport transport = null;
		transport = new TSocket(this.mysqlhost, 9098);
		TProtocol protocol = new TBinaryProtocol(transport);
		ReportFormService.Client client = new ReportFormService.Client(protocol);
		try {
			transport.open();
		} catch (TTransportException e) {
			// e.printStackTrace();
		}
		try {
			client.updateAdPlanStatus(pid, PlanStatus.NOBUDGET);
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} catch (TException e) {
			// e.printStackTrace();
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
		} catch (TTransportException e) {
			// e.printStackTrace();
		}
		ArrayList<String> pids = new ArrayList<String>();
		try {
			List<AdPlan> a = client.getAdPlansByUid(uid);
			for (AdPlan ap : a)
				pids.add(String.valueOf(ap.plan_id));
		} catch (NumberFormatException e) {
			// e.printStackTrace();
		} catch (TException e) {
			// e.printStackTrace();
		}
		for (String pid : pids) {
			try {
				client.updateAdPlanStatus(Integer.parseInt(pid),
						PlanStatus.NOBUDGET);
			} catch (NumberFormatException e) {
				// e.printStackTrace();
			} catch (TException e) {
				// e.printStackTrace();
			}
		}
		transport.close();

	}

	private void CutDownUser(int uid, Double charge) {
		Statement statement = null;
		try {
			statement = this.conn.createStatement();
			String sql = String
					.format("update adp_user_info set account=account-%.10f where uid=%d",
							charge, uid);
			statement.executeUpdate(sql);
			statement.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
	}

	/**
	 * åŽ‹ç¼©å­—ç¬¦ä¸²ä¸º byte[] å‚¨å­˜å�¯ä»¥ä½¿ç”¨new sun.misc.BASE64Encoder().encodeBuffer(byte[] b)æ–¹æ³•
	 * ä¿�å­˜ä¸ºå­—ç¬¦ä¸²
	 * 
	 * @param str
	 *            åŽ‹ç¼©å‰�çš„æ–‡æœ¬
	 * @return
	 */
	public static final byte[] compress(String str) {
		if (str == null)
			return null;

		byte[] compressed;
		ByteArrayOutputStream out = null;
		ZipOutputStream zout = null;

		try {
			out = new ByteArrayOutputStream();
			zout = new ZipOutputStream(out);
			zout.putNextEntry(new ZipEntry("0"));
			zout.write(str.getBytes());
			zout.closeEntry();
			compressed = out.toByteArray();
		} catch (IOException e) {
			compressed = null;
		} finally {
			if (zout != null) {
				try {
					zout.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return compressed;
	}

	/**
	 * å°†åŽ‹ç¼©å�Žçš„ byte[] æ•°æ�®è§£åŽ‹ç¼©
	 * 
	 * @param compressed
	 *            åŽ‹ç¼©å�Žçš„ byte[] æ•°æ�®
	 * @return è§£åŽ‹å�Žçš„å­—ç¬¦ä¸²
	 */
	public static final String decompress(byte[] compressed) {
		if (compressed == null)
			return null;

		ByteArrayOutputStream out = null;
		ByteArrayInputStream in = null;
		ZipInputStream zin = null;
		String decompressed;
		try {
			out = new ByteArrayOutputStream();
			in = new ByteArrayInputStream(compressed);
			zin = new ZipInputStream(in);
			byte[] buffer = new byte[1024];
			int offset = -1;
			while ((offset = zin.read(buffer)) != -1) {
				out.write(buffer, 0, offset);
			}
			decompressed = out.toString();
		} catch (IOException e) {
			decompressed = null;
		} finally {
			if (zin != null) {
				try {
					zin.close();
				} catch (IOException e) {
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}

		return decompressed;
	}
}
