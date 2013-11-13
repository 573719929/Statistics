package cn.com.baicdata.stat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

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
		double cost = -1.0d;
		if (line.startsWith(" rtb_")) {
			String[] segments = line.split("\u0001");
			int len = segments.length;
			if (len > 4) {
				if (len >= 33 && line.startsWith(" rtb_bid\u00011\u0001")) { // rtb_bid ^A 1 ^A
					if (db.isValidBid(segments[7])) {
						Areaid = segments[10]; Day = getday(segments[4]); Hour = gethour(segments[4]); Source = segments[2]; Host = segments[22];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = segments[24]; Userid = info[1]; Planid = info[2]; Groupid = info[3]; Adid = info[0]; Stuffid = info[4];
						Type = "bid";
						db.BID(segments[7]);
						db.HOST(segments[7], segments[22], segments[24]);
					}
				} else if (len >= 11 && line.startsWith(" rtb_bidres\u00011\u0001")) { // rtb_bidres ^A 1 ^A
					if (db.isValidBidres(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[10]; Day = getday(segments[4]); Hour = gethour(segments[4]); Source = segments[2]; Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1]; Userid = info[1]; Planid = info[2]; Groupid = info[3]; Adid = info[0]; Stuffid = info[4];
						try {
							cost = Double.parseDouble(segments[9]);
							if (cost>10 || cost<0) {
								cost = -1;
							} else {
								Type = "bidres";
								db.BIDRES(segments[7]);
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						
					}
				} else if (len >= 11 && line.startsWith(" rtb_creative\u00011\u0001")) { // rtb_creative ^A 1 ^A
					if (db.isValidCreative(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[10]; Day = getday(segments[4]); Hour = gethour(segments[4]); Source = segments[2]; Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						//System.out.println(db.getHost(segments[7]));
						Adspace = ha[1]; Userid = info[1]; Planid = info[2]; Groupid = info[3]; Adid = info[0]; Stuffid = info[4];
						Type = "creative";
						db.CREATIVE(segments[7]);
					}
				} else if (len >= 10 && line.startsWith(" rtb_show\u00011\u0001")) { // rtb_show ^A 1 ^A
					if (db.isValidShow(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[9]; Day = getday(segments[4]); Hour = gethour(segments[4]); Source = segments[2]; Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1]; Userid = info[1]; Planid = info[2]; Groupid = info[3]; Adid = info[0]; Stuffid = info[4];
						Type = "show";
						db.SHOW(segments[7]);
					}
				} else if (len >= 10 && line.startsWith(" rtb_click\u00011\u0001")) { // rtb_click ^A 1 ^A
					if (db.isValidClick(segments[7])) {
						String ha[] = db.getHost(segments[7]).split(",");
						Areaid = segments[9]; Day = getday(segments[4]); Hour = gethour(segments[4]); Source = segments[2]; Host = ha[0];
						String info[] = db.GetAdInfo(segments[6]);
						Adspace = ha[1]; Userid = info[1]; Planid = info[2]; Groupid = info[3]; Adid = info[0]; Stuffid = info[4];
						Type = "click";
						db.CLICK(segments[7]);
					}
				} else {
					System.out.println("invalid"+line);
				}
			}
		}
		if (Type != null && ! Type.equals("")) {
			return new Result(new Record(Areaid, Day, Hour, Source, Host, Adspace, Userid, Planid, Groupid, Adid, Stuffid), Type, cost);
		} else {
			return null;
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Options options = new Options();
		options.addOption("i",	"incache",	true,	"read cache from, required");
		options.addOption("o",	"outcache",	true,	"save cache to, required");
		options.addOption("f",	"file",		true,	"log file, required");

		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (cl == null || ! cl.hasOption("i") || ! cl.hasOption("o") || ! cl.hasOption("f")) {
			new HelpFormatter().printHelp("java -jar Statd.jar -i [in cache files] -o [out cache files] -f [log files]", options);
		} else {
			Db d = new Db(1, 1);
			d.read(cl.getOptionValue("i").split(","));
			// result init
			HashMap<Record, Node> data = new HashMap<Record, Node>();
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
							//
							Result r = Statd.parse(d, line);
							if (r != null) {
								//System.out.println(line);
								//System.out.println(r.record);
								//System.out.println(r.type);
								//System.out.println();
								
								if (! data.containsKey(r.record)) {
									data.put(r.record, new Node());
								}
								if (r.type == "bid") {
									data.get(r.record).bid();
								}
								else if (r.type == "bidres") {
									Node t = data.get(r.record);
									t.bidres();
									t.cost(r.cost);
								}
								else if (r.type == "creative") {
									data.get(r.record).creative();
								}
								else if (r.type == "show") {
									data.get(r.record).show();
								}
								else if (r.type == "click") {
									data.get(r.record).click();
								} else {
								}
							} else {
							}
							//
							try {
								line = br.readLine();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			
			for(Record r : data.keySet()) {
				System.out.println(String.format("%s | %s", r, data.get(r)));
			}
			// post calculate
			d.save(cl.getOptionValue("o").split(","));
		}
		
		/*
		Db cache = new Db(1, 1);
		cache.read(new String[]{"C:\\Users\\Administrator\\Desktop\\new  2.txt", "C:\\Users\\Administrator\\Desktop\\new  3.txt"});
		
		for (String i : new String[]{"a", "b", "c", "d", "e", "f"}) {
			System.out.println(">>"+i);
			System.out.println(cache.isValidBid(i));
			System.out.println(cache.isValidBidres(i));
			System.out.println(cache.isValidCreative(i));
			System.out.println(cache.isValidShow(i));
			System.out.println(cache.isValidClick(i));
			cache.CLICK(i);
			System.out.println(cache.isValidClick(i));
			System.out.println(">>"+i);
			System.out.println();
		}
		cache.save(new String[]{"C:\\Users\\Administrator\\Desktop\\new  4.txt"});
		
		
		HashMap<char[], Integer> a = new HashMap<char[], Integer>();
		a.put("abc".toCharArray(), 0);
		a.put("xyz".toCharArray(), 3);
		a.put("zyx".toCharArray(), 4);
		a.put("abc".toCharArray(), 7);
		System.out.println(a);
		
		HashMap<Record, Integer> d = new HashMap<Record, Integer>();
		d.put(new Record("1", "", "", "", "", "", "", "", "", "", ""), 1);
		d.put(new Record("", "", "", "3", "", "", "", "", "", "", ""), 2);
		d.put(new Record("1", "", "", "", "", "", "", "", "", "", ""), 3);
		d.put(new Record("", "", "", "3", "", "", "", "", "", "", ""), 4);
		System.out.println(d.get(new Record("", "", "", "3", "", "", "", "", "", "", "")).intValue());
		System.out.println(d);
		
		String configfile = cl.getOptionValue("config");
		*/
	}

}
