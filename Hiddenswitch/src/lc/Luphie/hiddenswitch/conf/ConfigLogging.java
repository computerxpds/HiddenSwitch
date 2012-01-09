package lc.Luphie.hiddenswitch.conf;

import java.util.logging.Logger;


public final class ConfigLogging {
	
	private static Logger log;
	private static String logname = "[HiddenSwitch]";
	public static enum mLevel {
		MESSAGE	(" "),
		WARNING	("[WARNING] "),
		ERROR	("[ERROR] "),
		SEVERE	("[SEVERE] "),
		INFO	("[INFO] ");
		
		private final String outString;
		
		mLevel(String output) {
			this.outString = output;
		}
		
	}
	
	public static void logMes(String mes, mLevel levl) {
		log.info(logname+levl.outString+mes);
	}
	public static void logMes(String mes) {
		logMes(mes, mLevel.MESSAGE);
	}

}
