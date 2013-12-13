package cn.com.baicdata.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class Statd {

	// public static final String COLLECTION = "DETAIL";
	public static final String P = "P";
	public static final String S = "S";
	public static final String C = "C";
	public static final String HourDetail = "HourDetail";
	public static final String AreaDetail = "AreaDetail";
	public static final String SourceDetail = "SourceDetail";
	public static final String HostDetail = "HostDetail";
	public static final String AdspaceDetail = "AdspaceDetail";

	public static final String KeyFormatter = "%s,%s,%s";

	public static final String DATABASE = "StatV3";

	public static final String RBID = " rtb_bid";
	public static final String RBIDRES = " rtb_bidres";
	public static final String RCREATIVE = " rtb_creative";
	public static final String RSHOW = " rtb_show";
	public static final String RCLICK = " rtb_click";

	public static final int TNOTHING = -1;
	public static final int TBID = 0;
	public static final int TBIDRES = 1;
	public static final int TCREATIVE = 2;
	public static final int TSHOW = 3;
	public static final int TCLICK = 4;

	public static final String USERID = "USERID";
	public static final String AREA = "AREA";
	public static final String DAY = "DAY";
	public static final String HOUR = "HOUR";
	public static final String SOURCE = "SOURCE";
	public static final String HOST = "HOST";
	public static final String ADSPACE = "ADSPACE";
	public static final String PLANID = "PLANID";
	public static final String GROUPID = "GROUPID";
	public static final String ADID = "ADID";
	public static final String STUFFID = "STUFFID";

	public static final String BID = "bid";
	public static final String BIDRES = "bidres";
	public static final String CREATIVE = "push";
	public static final String SHOW = "show";
	public static final String CLICK = "click";
	public static final String COST = "cost";
	public static final String SELFCOST = "selfcost";

	public static final String SEPARATOR = "\u0001";

	private static final String ZERO = "0";

	public static void TAG(String Message) {
		System.err.println(String.format("%s %s", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss SS").format(new Date()), Message));
	}

	public static void NTAG(String Message) {
		System.err.println(String.format("%s %s", System.nanoTime(), Message));
	}

	public static String getday(String datetime) {
		String[] s = datetime.substring(0, 10).split("-");
		return s[0] + s[1] + s[2];
	}

	public static String gethour(String datetime) {
		return datetime.substring(11, 13);
	}

	public static String ltrim(String str) {
		int i = 0;
		while (i < str.length() && str.charAt(i) == '0')
			++i;
		return (i == str.length()) ? Statd.ZERO : str.substring(i);
	}

	/**
	 * @param line
	 * @param d
	 * @param HourData
	 * @param args
	 */
	public static void parseLine(String line, Db d,
			HashMap<String, double[]> HourData,
			HashMap<String, double[]> AreaData,
			HashMap<String, double[]> HostData,
			HashMap<String, double[]> AdspaceData,
			HashMap<String, double[]> SourceData,
			HashMap<String, int[]> AdsData, HashSet<String> errorBidres,
			HashSet<String> errorShow, HashSet<String> errorClick) {
		String HourKey = null, AreaKey = null, HostKey = null, AdspaceKey = null, SourceKey = null, AdsKey = null;
		try {
			double cost = -1.0d, fee = 0.0d;
			String[] segments = line.split(Statd.SEPARATOR);
			int Type = Statd.TNOTHING, len = segments.length, Userid = 0;
			if (len > 4) {
				if (len >= 33 && segments[0].equals(Statd.RBID)) {
					if (true) {
						Type = Statd.TBID;
//						System.out.println("BID found:"+segments[7]);
						d.BID(segments[7], segments[22], segments[24],
								segments[32], String
										.format("%s|%s|%sX%s|%s", segments[21],
												segments[33], segments[26],
												segments[27], segments[2]));
						// into = view_type, view_location, size, rtb_source
						String adid = ltrim(segments[6]), day = getday(segments[4]);
						HourKey = String.format(Statd.KeyFormatter, adid, day,
								gethour(segments[4]));
						AreaKey = String.format(Statd.KeyFormatter, adid, day,
								segments[10]);
						HostKey = String.format(Statd.KeyFormatter, adid, day,
								segments[22]);
						AdspaceKey = String.format(Statd.KeyFormatter, adid,
								day, segments[24]);
						SourceKey = String.format(Statd.KeyFormatter, adid,
								day, segments[2]);
					}
				} else if (len >= 11 && segments[0].equals(Statd.RBIDRES)) {
					d.SYNC();
					try {
						if (d.isValidBidres(segments[7])) {
							String ha[] = d.getHost(segments[7]).split(",");
//							System.out.println("BIDRES found:"+segments[7]);
							float[] info = d.getOwner(Integer
									.parseInt(ltrim(segments[6])));
//							System.out.println(info);System.out.println(String.format("%f,%f,%f,%f", info[0], info[1], info[2], info[3]));
							Userid = (int) info[0];
							int usertype = (int) info[1];
							
							cost = Double.parseDouble(segments[9]);
							double bidprice = d.GetBidprice(segments[7]);
							if (cost > 10 || cost < 0) {
								cost = fee = 0.0d;
							} else {

								switch (usertype) {
								case 1:
									if (bidprice >= cost) {
										float rate[] = d.GetRate(Userid);
										fee = (cost + Math.min(cost,
												(bidprice - cost) * rate[0]))
												* (1 + rate[1]);
									} else {
										fee = cost;
									}
									break;
								case 2:
									fee = info[2];
									break;
								case 3:
									fee = 0;
									break;
								case 0:
									fee = cost;
									break;
								default:
									break;
								}

								String adid = ltrim(segments[6]), day = getday(segments[4]);
								HourKey = String.format(Statd.KeyFormatter,
										adid, day, gethour(segments[4]));
								AreaKey = String.format(Statd.KeyFormatter,
										adid, day, segments[10]);

								HostKey = String.format(Statd.KeyFormatter,
										adid, day, ha[0]);
								AdspaceKey = String.format(Statd.KeyFormatter,
										adid, day, ha[1]);
								SourceKey = String.format(Statd.KeyFormatter,
										adid, day, segments[2]);
								// host, view_type, view_location, size,
								// rtb_source
								AdsKey = String.format("%s|%s", ha[0], ha[2]);
								
								d.BIDRES(segments[7]);
								Type = Statd.TBIDRES;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						errorBidres.add(line);
					}
				} else if (len >= 11 && segments[0].equals(Statd.RCREATIVE)) {
					d.SYNC();
					if (d.isValidCreative(segments[8])) {
//						System.out.println("CREATIVE found:"+segments[8]);
						String ha[] = d.getHost(segments[8]).split(",");
						Type = Statd.TCREATIVE;
						d.CREATIVE(segments[8]);
						String adid = ltrim(segments[6]), day = getday(segments[4]);
						HourKey = String.format(Statd.KeyFormatter, adid, day,
								gethour(segments[4]));
						AreaKey = String.format(Statd.KeyFormatter, adid, day,
								segments[10]);
						HostKey = String.format(Statd.KeyFormatter, adid, day,
								ha[0]);
						AdspaceKey = String.format(Statd.KeyFormatter, adid,
								day, ha[1]);
						SourceKey = String.format(Statd.KeyFormatter, adid,
								day, segments[2]);
					}
				} else if (len >= 10 && segments[0].equals(Statd.RSHOW)) {
					d.SYNC();
					try {
						if (d.isValidShow(segments[7])) {
//							System.out.println("SHOW found:"+segments[7]);
							String ha[] = d.getHost(segments[7]).split(",");
							Type = Statd.TSHOW;
							d.SHOW(segments[7]);
							String adid = ltrim(segments[6]), day = getday(segments[4]);
							HourKey = String.format(Statd.KeyFormatter, adid,
									day, gethour(segments[4]));
							AreaKey = String.format(Statd.KeyFormatter, adid,
									day, segments[9]);
							HostKey = String.format(Statd.KeyFormatter, adid,
									day, ha[0]);
							AdspaceKey = String.format(Statd.KeyFormatter,
									adid, day, ha[1]);
							SourceKey = String.format(Statd.KeyFormatter, adid,
									day, segments[2]);
							// host, view_type, view_location, size, rtb_source
							AdsKey = String.format("%s|%s", ha[0], ha[2]);
						}
					} catch (Exception e) {
						errorShow.add(line);
					}
				} else if (len >= 10 && segments[0].equals(Statd.RCLICK)) {
					d.SYNC();
					try {
						if (d.isValidClick(segments[7])) {
//							System.out.println("CLICK found:"+segments[7]);
							String ha[] = d.getHost(segments[7]).split(",");
							Type = Statd.TCLICK;
							d.CLICK(segments[7]);
							String adid = ltrim(segments[6]), day = getday(segments[4]);
							float[] info = d.getOwner(Integer
									.parseInt(ltrim(segments[6])));
							if ((int)info[1] == 3) {
								fee = info[3];
							} else {
								fee = 0;
							}
							HourKey = String.format(Statd.KeyFormatter, adid,
									day, gethour(segments[4]));
							AreaKey = String.format(Statd.KeyFormatter, adid,
									day, segments[9]);
							HostKey = String.format(Statd.KeyFormatter, adid,
									day, ha[0]);
							AdspaceKey = String.format(Statd.KeyFormatter,
									adid, day, ha[1]);
							SourceKey = String.format(Statd.KeyFormatter, adid,
									day, segments[2]);
							// host, view_type, view_location, size, rtb_source
							AdsKey = String.format("%s|%s", ha[0], ha[2]);
						}
					} catch (Exception e) {
						errorClick.add(line);
					}
				}
			}
			if (Type != Statd.TNOTHING) {
				int x = -1;
				if (Type == Statd.TBIDRES) {
					x = 0;
				} else if (Type == Statd.TSHOW) {
					x = 1;
				} else if (Type == Statd.TCLICK) {
					x = 2;
				}
				int y[] = null;
				if (x >= 0) {
					try {
						y = AdsData.get(AdsKey);
						if (y[0] == 0)
							;
					} catch (Exception e) {
						y = new int[] { 0, 0, 0 };
						AdsData.put(AdsKey, y);
					}
					++y[x];
				}

				double t[] = null;
				if (!HourData.containsKey(HourKey))
					HourData.put(HourKey, (t = new double[] { 0, 0, 0, 0, 0, 0,
							0 }));
				else
					t = HourData.get(HourKey);
				if (Type == Statd.TBIDRES) {
					t[5] += fee;
					t[6] += cost;
				}
				if (Type == Statd.TCLICK) {
					t[5] += fee;
				}
				++t[Type];
				if (!AreaData.containsKey(AreaKey))
					AreaData.put(AreaKey, (t = new double[] { 0, 0, 0, 0, 0, 0,
							0 }));
				else
					t = AreaData.get(AreaKey);
				if (Type == Statd.TBIDRES) {
					t[5] += fee;
					t[6] += cost;
				}
				if (Type == Statd.TCLICK) {
					t[5] += fee;
				}
				++t[Type];
				if (!SourceData.containsKey(SourceKey))
					SourceData.put(SourceKey, (t = new double[] { 0, 0, 0, 0,
							0, 0, 0 }));
				else
					t = SourceData.get(SourceKey);
				if (Type == Statd.TBIDRES) {
					t[5] += fee;
					t[6] += cost;
				}
				if (Type == Statd.TCLICK) {
					t[5] += fee;
				}
				++t[Type];
				if (!HostData.containsKey(HostKey))
					HostData.put(HostKey, (t = new double[] { 0, 0, 0, 0, 0, 0,
							0 }));
				else
					t = HostData.get(HostKey);
				if (Type == Statd.TBIDRES) {
					t[5] += fee;
					t[6] += cost;
				}
				if (Type == Statd.TCLICK) {
					t[5] += fee;
				}
				++t[Type];
				if (!AdspaceData.containsKey(AdspaceKey))
					AdspaceData.put(AdspaceKey, (t = new double[] { 0, 0, 0, 0,
							0, 0, 0 }));
				else
					t = AdspaceData.get(AdspaceKey);
				if (Type == Statd.TBIDRES) {
					t[5] += fee;
					t[6] += cost;
				}
				if (Type == Statd.TCLICK) {
					t[5] += fee;
				}
				++t[Type];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Options options = new Options();
		options.addOption("f", "file", true, "log file, required");
		options.addOption("a", "adp", true, "adp server, required");
		options.addOption("s", "stat", true, "statistics server, required");
		options.addOption("c", "cache", true, "cache server, required");

		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (cl == null || !cl.hasOption("f") || !cl.hasOption("adp")
				|| !cl.hasOption("stat") || !cl.hasOption("cache")) {
			new HelpFormatter().printHelp(
					"java -jar Statd.jar [options] -f [logs]", options);
		} else {
			String mysql = cl.getOptionValue("adp"), mongoh = cl
					.getOptionValue("stat"), redis = cl.getOptionValue("cache");
			Db d = new Db(mysql, mongoh, redis);
			HashMap<String, double[]> data = new HashMap<String, double[]>();
			HashMap<String, double[]> HourData = new HashMap<String, double[]>();
			HashMap<String, double[]> AreaData = new HashMap<String, double[]>();
			HashMap<String, double[]> HostData = new HashMap<String, double[]>();
			HashMap<String, double[]> AdspaceData = new HashMap<String, double[]>();
			HashMap<String, double[]> SourceData = new HashMap<String, double[]>();
			HashMap<String, int[]> AdsData = new HashMap<String, int[]>();
			TAG("Start read log ...");
			HashSet<String> errorShow = new HashSet<String>();
			HashSet<String> errorClick = new HashSet<String>();
			HashSet<String> errorBidres = new HashSet<String>();
			for (String f : cl.getOptionValue("f").split(",")) {
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
							parseLine(line, d, HourData, AreaData, HostData,
									AdspaceData, SourceData, AdsData,
									errorBidres, errorShow, errorClick);
							try {
								line = br.readLine();
							} catch (IOException e) {
								line = null;
								e.printStackTrace();
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
			d.SYNC();
			HashSet<String> errorBidres2 = new HashSet<String>();
			for (String line : errorBidres) {
				parseLine(line, d, HourData, AreaData, HostData, AdspaceData,
						SourceData, AdsData, errorBidres2, errorShow,
						errorClick);
			}
			HashSet<String> errorShow2 = new HashSet<String>();
			for (String line : errorShow) {
				parseLine(line, d, HourData, AreaData, HostData, AdspaceData,
						SourceData, AdsData, errorBidres, errorShow2,
						errorClick);
			}
			HashSet<String> errorClick2 = new HashSet<String>();
			for (String line : errorClick) {
				parseLine(line, d, HourData, AreaData, HostData, AdspaceData,
						SourceData, AdsData, errorBidres, errorShow,
						errorClick2);
			}
			System.gc();
			d.SYNC();
			TAG("Read complete !");
			System.gc();
			Mongo mongo = null;
			try {
				mongo = new Mongo(mongoh, 12331);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			if (mongo != null || true) {
				TAG("Write mongo ...");
				DBCollection col = mongo.getDB(Statd.DATABASE).getCollection(
						Statd.HourDetail);
				BasicDBObject query = new BasicDBObject(), document = new BasicDBObject(), r1 = new BasicDBObject();
				int userid = 0, planid = 0;
				double userplancost = 0;
				for (String r : HourData.keySet()) {
					try {
						double cn[] = HourData.get(r);
						// System.out.println(r + ":" + Arrays.toString(cn));
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						r1.put(Statd.COST, (userplancost = cn[5] / 1000));
						r1.put(Statd.SELFCOST, cn[6] / 1000);
						document.put("$inc", r1);
						query.clear();
						query.put("_id", r);
						String seg[] = r.split(",");
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.HOUR, Integer.parseInt(seg[2]));
						int[] info = d.GetAdInfo(Integer.parseInt(seg[0]));
						query.put(Statd.USERID, (userid = info[1]));
						query.put(Statd.PLANID, (planid = info[2]));
						query.put(Statd.ADID, info[0]);
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.STUFFID, info[4]);
						d.CutDown(userid, planid, userplancost);
						col.update(query, document, true, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				col = mongo.getDB(Statd.DATABASE).getCollection(
						Statd.AreaDetail);
				for (String r : AreaData.keySet()) {
					try {
						double cn[] = AreaData.get(r);
						// System.out.println(r + ":" + Arrays.toString(cn));
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						r1.put(Statd.COST, (userplancost = cn[5] / 1000));
						r1.put(Statd.SELFCOST, cn[6] / 1000);
						document.put("$inc", r1);
						query.clear();
						query.put("_id", r);
						String seg[] = r.split(",");
						int[] info = d.GetAdInfo(Integer.parseInt(seg[0]));
						query.put(Statd.ADID, info[0]);
						query.put(Statd.USERID, info[1]);
						query.put(Statd.PLANID, info[2]);
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.STUFFID, info[4]);
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.AREA, Integer.parseInt(seg[2]));
						col.update(query, document, true, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				col = mongo.getDB(Statd.DATABASE).getCollection(
						Statd.SourceDetail);
				for (String r : SourceData.keySet()) {
					try {
						double cn[] = SourceData.get(r);
						// System.out.println(r + ":" + Arrays.toString(cn));
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						r1.put(Statd.COST, (userplancost = cn[5] / 1000));
						r1.put(Statd.SELFCOST, cn[6] / 1000);
						document.put("$inc", r1);
						query.clear();
						query.put("_id", r);
						String seg[] = r.split(",");
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.SOURCE, seg[2]);
						int[] info = d.GetAdInfo(Integer.parseInt(seg[0]));
						query.put(Statd.ADID, info[0]);
						query.put(Statd.USERID, info[1]);
						query.put(Statd.PLANID, info[2]);
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.STUFFID, info[4]);
						col.update(query, document, true, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				col = mongo.getDB(Statd.DATABASE).getCollection(
						Statd.HostDetail);
				for (String r : HostData.keySet()) {
					try {
						double cn[] = HostData.get(r);
						// System.out.println(r + ":" + Arrays.toString(cn));
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						r1.put(Statd.COST, (userplancost = cn[5] / 1000));
						r1.put(Statd.SELFCOST, cn[6] / 1000);
						document.put("$inc", r1);
						query.clear();
						query.put("_id", r);
						String seg[] = r.split(",");
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.HOST, seg[2]);
						int[] info = d.GetAdInfo(Integer.parseInt(seg[0]));
						query.put(Statd.ADID, info[0]);
						query.put(Statd.USERID, info[1]);
						query.put(Statd.PLANID, info[2]);
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.STUFFID, info[4]);
						col.update(query, document, true, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				col = mongo.getDB(Statd.DATABASE).getCollection(
						Statd.AdspaceDetail);
				for (String r : AdspaceData.keySet()) {
					try {
						double cn[] = AdspaceData.get(r);
						// System.out.println(r + ":" + Arrays.toString(cn));
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						r1.put(Statd.COST, (userplancost = cn[5] / 1000));
						r1.put(Statd.SELFCOST, cn[6] / 1000);
						document.put("$inc", r1);
						query.clear();
						query.put("_id", r);
						String seg[] = r.split(",");
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.ADSPACE, seg[2]);
						int[] info = d.GetAdInfo(Integer.parseInt(seg[0]));
						query.put(Statd.ADID, info[0]);
						query.put(Statd.USERID, info[1]);
						query.put(Statd.PLANID, info[2]);
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.STUFFID, info[4]);
						col.update(query, document, true, false);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				int[] c = new int[] { 0, 0, 0 };
				for (String r : AdsData.keySet()) {
					c = AdsData.get(r);
					d.j2.hincrBy(r, Statd.P, c[0]);
					d.j2.hincrBy(r, Statd.S, c[1]);
					d.j2.hincrBy(r, Statd.C, c[2]);
				}

				TAG("Write complete !");
				data.clear();
				System.gc();
				TAG("Settle start ...");
				d.processCutDown();
				TAG("Settle complete !");

				mongo.close();
			}
			System.out.println();
		}
	}

}
