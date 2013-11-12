package cn.com.baicdata.stat;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Statd {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Options options = new Options();
		options.addOption("c", "config", true, "config file, required");

		CommandLine cl = null;
		try {
			cl = new PosixParser().parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (cl == null || cl.hasOption("help") || !cl.hasOption("config")) {
			new HelpFormatter().printHelp(
					"java -jar Data-Importer.jar -c config.xml", options);
			System.exit(-1);
		}

		String configfile = cl.getOptionValue("config");
	}

}
