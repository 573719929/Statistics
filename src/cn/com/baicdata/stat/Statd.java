package cn.com.baicdata.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
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
	public static String getday(String datetime) {
		String[] s = datetime.split(" ")[0].split("-");
		return s[0] + s[1] + s[2];
	}

	public static String gethour(String datetime) {
		String[] s = datetime.split(" ")[1].split(":");
		return s[0];
	}

	public static Result parse(Db db, String line) {
		String Areaid = "", Day = "", Hour = "", Source = "", Host = "";
		String Adspace = "", Userid = "", Planid = "", Groupid = "", Adid = "", Stuffid = "";
		String Type = null;
		double cost = -1.0d, fee = 0.0d;
		if (line.startsWith(" rtb_")) {
			String[] segments = line.split("\u0001");
			int len = segments.length;
			if (len > 4) {
				if (len >= 33 && line.startsWith(" rtb_bid\u00011\u0001")) { // rtb_bid
																				// ^A
																				// 1
																				// ^A
					if (db.isValidBid(segments[7])) {
						Areaid = segments[10];
						Day = getday(segments[4]);
						Hour = gethour(segments[4]);
						Source = segments[2];
						Host = segments[22];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = segments[24];
						Userid = info[1];
						Planid = info[2];
						Groupid = info[3];
						Adid = info[0];
						Stuffid = info[4];
						Type = "bid";
						db.BID(segments[7], segments[22], segments[24], segments[32]);
					}
				} else if (len >= 11
						&& line.startsWith(" rtb_bidres\u00011\u0001")) { // rtb_bidres
																			// ^A
																			// 1
																			// ^A
					if (db.isValidBidres(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[10];
						Day = getday(segments[4]);
						Hour = gethour(segments[4]);
						Source = segments[2];
						Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1];
						Userid = info[1];
						Planid = info[2];
						Groupid = info[3];
						Adid = info[0];
						Stuffid = info[4];
						try {
							cost = Double.parseDouble(segments[9]);
							double bidprice = db.GetBidprice(segments[7]);
							if (cost > 10 || cost < 0) {
								cost = fee = 0.0d;
							} else {
								if (bidprice >= cost) {
									fee = (cost + Math.min(cost, (bidprice-cost)*db.GetX(Userid)))*(1+db.GetY(Userid));
								} else {
									fee = cost;
								}
								Type = "bidres";
								db.BIDRES(segments[7]);
							}
						} catch (Exception e) {
							cost = fee = 0.0d;
							e.printStackTrace();
						}
					}
				} else if (len >= 11
						&& line.startsWith(" rtb_creative\u00011\u0001")) { // rtb_creative
																			// ^A
																			// 1
																			// ^A
					if (db.isValidCreative(segments[8])) {
						String ha[] = db.getHost(segments[8]).split(",");
						Areaid = segments[10];
						Day = getday(segments[4]);
						Hour = gethour(segments[4]);
						Source = segments[2];
						Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1];
						Userid = info[1];
						Planid = info[2];
						Groupid = info[3];
						Adid = info[0];
						Stuffid = info[4];
						Type = "creative";
						db.CREATIVE(segments[8]);
					}
				} else if (len >= 10
						&& line.startsWith(" rtb_show\u00011\u0001")) { // rtb_show
																		// ^A 1
																		// ^A
					if (db.isValidShow(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[9];
						Day = getday(segments[4]);
						Hour = gethour(segments[4]);
						Source = segments[2];
						Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1];
						Userid = info[1];
						Planid = info[2];
						Groupid = info[3];
						Adid = info[0];
						Stuffid = info[4];
						Type = "show";
						db.SHOW(segments[7]);
					}
				} else if (len >= 10
						&& line.startsWith(" rtb_click\u00011\u0001")) { // rtb_click
																			// ^A
																			// 1
																			// ^A
					if (db.isValidClick(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[9];
						Day = getday(segments[4]);
						Hour = gethour(segments[4]);
						Source = segments[2];
						Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1];
						Userid = info[1];
						Planid = info[2];
						Groupid = info[3];
						Adid = info[0];
						Stuffid = info[4];
						Type = "click";
						db.CLICK(segments[7]);
					}
				} else {
					System.out.println("invalid" + line);
				}
			}
		}
		if (Type != null && !Type.equals("")) {
			return new Result(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", Areaid, Day, Hour, Source, Host, Adspace, Userid, Planid, Groupid, Adid, Stuffid), Type, cost, fee);
		} else {
			return null;
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
//		String mysql = "112.124.46.78", mongoh = "112.124.55.156";
		Options options = new Options();
		options.addOption("i", "incache", true, "read cache from, required");
		options.addOption("o", "outcache", true, "save cache to, required");
		options.addOption("f", "file", true, "log file, required");
		options.addOption(null, "adp", true, "adp server, required");
		options.addOption(null, "stat", true, "statistics server, required");
		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (cl == null || !cl.hasOption("i") || !cl.hasOption("o") || !cl.hasOption("f") || !cl.hasOption("adp") || !cl.hasOption("stat")) {
			new HelpFormatter().printHelp("java -jar Statd.jar -i [in cache files] -o [out cache files] -f [log files]", options);
		} else {
			String mysql = cl.getOptionValue("adp"), mongoh = cl.getOptionValue("stat");
			Db d = new Db(1, 1, mysql, mongoh);
			d.read(cl.getOptionValue("i").split(","));
			HashMap<String, Node> data = new HashMap<String, Node>();
			for (String f : cl.getOptionValue("f").split(",")) {
				File fp = new File(f);
				if (fp.exists()) {
					BufferedReader br = null;
					try {
						br = new BufferedReader(new InputStreamReader(new FileInputStream(fp)));
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
							Result r = Statd.parse(d, line);
							if (r != null) {
								if (!data.containsKey(r.record)) {
									data.put(r.record, new Node());
								}
								if (r.type == "bid") {
									data.get(r.record).bid();
								} else if (r.type == "bidres") {
									Node t = data.get(r.record);
									t.bidres();
									t.cost(r.cost, r.fee);
								} else if (r.type == "creative") {
									data.get(r.record).creative();
								} else if (r.type == "show") {
									data.get(r.record).show();
								} else if (r.type == "click") {
									data.get(r.record).click();
								}
							}
							try {
								line = br.readLine();
							} catch (IOException e) {
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
			Mongo mongo = null;
			try {
				mongo = new Mongo(mongoh, 33458);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			if (mongo != null) {
				DBCollection col = mongo.getDB("StatV2").getCollection("Detail");
				DBObject query = new BasicDBObject(), document = new BasicDBObject();
				int id = 0, userid = 0, planid = 0;
				for (String r : data.keySet()) {
					query.put("_id", r);
					String seg[] = r.split(",");
					id = 0;
					try { id = Integer.parseInt(seg[0]); } catch (Exception e) { id = 0; }
					query.put("Areaid", id);
					try { id = Integer.parseInt(seg[1]); } catch (Exception e) { id = 0; }
					query.put("Day", id);
					try { id = Integer.parseInt(seg[2]); } catch (Exception e) { id = 0; }
					query.put("Hour", id);
					query.put("Source", seg[3]);
					query.put("Host", seg[4]);
					query.put("Adspace", seg[5]);
					try { id = Integer.parseInt(seg[6]); } catch (Exception e) { id = 0; }
					query.put("Userid", id);
					userid = id;
					try { id = Integer.parseInt(seg[7]); } catch (Exception e) { id = 0; }
					query.put("Planid", id);
					planid = id;
					try { id = Integer.parseInt(seg[8]); } catch (Exception e) { id = 0; }
					query.put("Groupid", id);
					try { id = Integer.parseInt(seg[9]); } catch (Exception e) { id = 0; }
					query.put("Adid", id);
					try { id = Integer.parseInt(seg[10]); } catch (Exception e) { id = 0; }
					query.put("Stuffid", id);
					
					Node cn = data.get(r);
					BasicDBObject r1 = new BasicDBObject();
					
					r1.put("bid", cn.getBid());
					r1.put("bidres", cn.getBidres());
					r1.put("creative", cn.getCreative());
					r1.put("show", cn.getShow());
					r1.put("click", cn.getClick());
					
					double fee = cn.getFee()/1000;
					r1.put("cost", fee);
					r1.put("selfcost", cn.getCost()/1000);
					d.CutDown(userid, planid, fee);
					document.put("$inc", r1);
					col.update(query, document, true, false);
				}
				d.processCutDown();
				mongo.close();
			}
			d.save(cl.getOptionValue("o").split(","));
		}
	}

}
