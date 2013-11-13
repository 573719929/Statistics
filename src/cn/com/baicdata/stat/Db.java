package cn.com.baicdata.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Db {
	private static final byte BID = 0x20; // 32
	private static final byte BIDRES = 0x30; // 48
	private static final byte CREATIVE = 0x38; // 56
	private static final byte SHOW = 0x3c; // 60
	private static final byte CLICK = 0x3e; // 62
	private int readafter = 0;
	private int saveafter = 0;
	private String url = "jdbc:mysql://112.124.46.78:33966/adp?useUnicode=true&characterEncoding=UTF-8&useFastDateParsing=false";
	private String user = "baicdata";
	private String password = "j8verXE8WZnDNXPV";
	private Connection conn = null;
	private HashMap<String, Byte> cache;
	private HashMap<String, String[]> adcache;
	private HashMap<String, String> hostcache;

	public Db(int readafter, int saveafter) {
		this.readafter = readafter;
		this.saveafter = saveafter;
		this.cache = new HashMap<String, Byte>();
		this.adcache = new HashMap<String, String[]>();
		this.hostcache = new HashMap<String, String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.conn = DriverManager.getConnection(this.url, this.user,
					this.password);
		} catch (SQLException e) {
			e.printStackTrace();
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
						if (segments.length == 2) {
							this.cache.put(segments[0],
									Byte.valueOf(segments[1]));
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

		for (FileWriter fw : lf) {
			for (String pushid : this.cache.keySet()) {
				try {
					fw.write(String.format("%s\t%d\n", pushid,
							this.cache.get(pushid)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public String getHost(String id) {
		if (this.hostcache.containsKey(id)) {
			return this.hostcache.get(id);
		} else {
			return "";
		}
	}

	public void HOST(String id, String host, String adspace) {
		this.hostcache.put(id, String.format("%s,%s", host, adspace));
	}

	private void set(String pushid, byte status) {
		this.cache.put(pushid, status);
	}

	public void BID(String pushid) {
		this.set(pushid, Db.BID);
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
				&& this.cache.get(pushid) == Db.BID;
		return r;
	}

	public boolean isValidCreative(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid) == Db.BIDRES;
		return r;
	}

	public boolean isValidShow(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid) == Db.CREATIVE;
		return r;
	}

	public boolean isValidClick(String pushid) {
		boolean r = this.cache.containsKey(pushid)
				&& this.cache.get(pushid) == Db.SHOW;
		return r;
	}
}
