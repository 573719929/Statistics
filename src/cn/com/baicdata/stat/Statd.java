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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class Statd {

	public static final String COLLECTION = "DETAIL";
	public static final String DATABASE = "STAT";

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

	public static final String USERID = "U";
	public static final String AREAID = "A";
	public static final String DAY = "D";
	public static final String HOUR = "H";
	public static final String SOURCE = "S";
	public static final String HOST = "M";
	public static final String ADSPACE = "W";
	public static final String PLANID = "P";
	public static final String GROUPID = "G";
	public static final String ADID = "I";
	public static final String STUFFID = "F";

	public static final String BID = "b";
	public static final String BIDRES = "r";
	public static final String CREATIVE = "p";
	public static final String SHOW = "s";
	public static final String CLICK = "c";
	public static final String COST = "o";
	public static final String SELFCOST = "t";

	public static final String SEPARATOR = "\u0001";
	private static final String KEYFORMAT = "%s,%s,%s,%s,%s,%s,%s";

	private static final String ZERO = "0";

	public static void TAG(String Message) {
		System.out.println(String.format("%s %s", new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss SS").format(new Date()), Message));
	}

	public static void NTAG(String Message) {
		System.out.println(String.format("%s %s", System.nanoTime(), Message));
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
	 * @param args
	 */
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
			TAG("Start read log ...");
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
							try {
								String Record = null;
								double cost = -1.0d, fee = 0.0d;
								String[] segments = line.split(Statd.SEPARATOR);
								int Type = Statd.TNOTHING, len = segments.length, Adid = 0, Userid = 0;
								if (len > 4) {
									if (len >= 33
											&& segments[0].equals(Statd.RBID)) {
										if (true) {
											Type = Statd.TBID;
											d.BID(segments[7], segments[22],
													segments[24], segments[32]);
											Record = String.format(
													Statd.KEYFORMAT,
													segments[10],
													getday(segments[4]),
													gethour(segments[4]),
													segments[2], segments[22],
													segments[24],
													ltrim(segments[6]));
										}
									} else if (len >= 11
											&& segments[0]
													.equals(Statd.RBIDRES)) {
										d.SYNC();
										if (d.isValidBidres(segments[7])) {
											String ha[] = d
													.getHost(segments[7])
													.split(",");
											Userid = d
													.getOwner((Adid = Integer
															.parseInt(ltrim(segments[6]))));
											try {
												cost = Double
														.parseDouble(segments[9]);
												double bidprice = d
														.GetBidprice(segments[7]);
												if (cost > 10 || cost < 0) {
													cost = fee = 0.0d;
												} else {
													if (bidprice >= cost) {
														float rate[] = d
																.GetRate(Userid);
														fee = (cost + Math
																.min(cost,
																		(bidprice - cost)
																				* rate[0]))
																* (1 + rate[1]);
													} else {
														fee = cost;
													}
													Type = Statd.TBIDRES;
													d.BIDRES(segments[7]);
													Record = String
															.format("%s,%s,%s,%s,%s,%s,%d",
																	segments[10],
																	getday(segments[4]),
																	gethour(segments[4]),
																	segments[2],
																	ha[0],
																	ha[1], Adid);
												}
											} catch (Exception e) {
												 e.printStackTrace();
											}
										}
									} else if (len >= 11
											&& segments[0]
													.equals(Statd.RCREATIVE)) {
										d.SYNC();
										if (d.isValidCreative(segments[8])) {
											String ha[] = d
													.getHost(segments[8])
													.split(",");
											Type = Statd.TCREATIVE;
											d.CREATIVE(segments[8]);
											Record = String.format(
													Statd.KEYFORMAT,
													segments[10],
													getday(segments[4]),
													gethour(segments[4]),
													segments[2], ha[0], ha[1],
													ltrim(segments[6]));
										}
									} else if (len >= 10
											&& segments[0].equals(Statd.RSHOW)) {
										d.SYNC();
										if (d.isValidShow(segments[7])) {
											String ha[] = d
													.getHost(segments[7])
													.split(",");
											Type = Statd.TSHOW;
											d.SHOW(segments[7]);
											Record = String.format(
													Statd.KEYFORMAT,
													segments[9],
													getday(segments[4]),
													gethour(segments[4]),
													segments[2], ha[0], ha[1],
													ltrim(segments[6]));
										}
									} else if (len >= 10
											&& segments[0].equals(Statd.RCLICK)) {
										d.SYNC();
										if (d.isValidClick(segments[7])) {
											String ha[] = d
													.getHost(segments[7])
													.split(",");
											Type = Statd.TCLICK;
											d.CLICK(segments[7]);
											Record = String.format(
													Statd.KEYFORMAT,
													segments[9],
													getday(segments[4]),
													gethour(segments[4]),
													segments[2], ha[0], ha[1],
													ltrim(segments[6]));
										}
									}
								}
								if (Type != Statd.TNOTHING) {
									double t[] = null;
									if (!data.containsKey(Record)) {
										data.put(Record, (t = new double[] { 0,
												0, 0, 0, 0, 0, 0 }));
									} else {
										t = data.get(Record);
									}
									if (Type == Statd.TBIDRES) {
										++t[Statd.TBIDRES];
										t[5] += fee;
										t[6] += cost;
									} else {
										++t[Type];
									}
								}
							} catch (Exception e) {
								 e.printStackTrace();
							}
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
			TAG("Read complete !");
			System.gc();
			Mongo mongo = null;
			try {
				mongo = new Mongo(mongoh, 12331);
			} catch (UnknownHostException e) {
				 e.printStackTrace();
			}
			if (mongo != null) {
				TAG("Write mongo ...");
				DBCollection col = mongo.getDB(Statd.DATABASE)
						.getCollection(Statd.COLLECTION);
				BasicDBObject query = new BasicDBObject(), document = new BasicDBObject(), r1 = new BasicDBObject();
				int userid = 0, planid = 0;
				double userplancost = 0;
				for (String r : data.keySet()) {
					try {
						double cn[] = data.get(r);
						r1.clear();
						r1.put(Statd.BID, (int) cn[0]);
						r1.put(Statd.BIDRES, (int) cn[1]);
						r1.put(Statd.CREATIVE, (int) cn[2]);
						r1.put(Statd.SHOW, (int) cn[3]);
						r1.put(Statd.CLICK, (int) cn[4]);
						userplancost = cn[5] / 1000;
						r1.put(Statd.COST, userplancost); // 用户花费
						r1.put(Statd.SELFCOST, cn[6] / 1000); // 成本

						document.put("$inc", r1);
						query.put("_id", r);
						String seg[] = r.split(",");
						query.put(Statd.AREAID, Integer.parseInt(seg[0]));
						query.put(Statd.DAY, Integer.parseInt(seg[1]));
						query.put(Statd.HOUR, Integer.parseInt(seg[2]));
						query.put(Statd.SOURCE, seg[3]);
						query.put(Statd.HOST, seg[4]);
						query.put(Statd.ADSPACE, seg[5]);
						int[] info = d.GetAdInfo(Integer.parseInt(seg[6]));
						query.put(Statd.USERID, (userid = info[1]));
						query.put(Statd.PLANID, (planid = info[2]));
						query.put(Statd.GROUPID, info[3]);
						query.put(Statd.ADID, info[0]);
						query.put(Statd.STUFFID, info[4]);
						d.CutDown(userid, planid, userplancost);
						col.update(query, document, true, false);
					} catch (Exception e) {
						 e.printStackTrace();
					}
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
