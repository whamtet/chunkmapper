package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class PreferenceManager {
	private static final File Cache = new File(Utila.CACHE, "preferences.txt");
	private static final HashMap<String, String> data = new HashMap<String, String>();
	static {
		if (Cache.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(Cache));
				String line;
				while ((line = br.readLine()) != null) {
					String[] split = line.split(" ");
					data.put(split[0], split[1]);
				}
			} catch (IOException e) {
				MyLogger.LOGGER.severe(MyLogger.printException(e));
			}
		}
	}
	private static void spit() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(Cache)));
			for (String k : data.keySet()) {
				pw.println(k + " " + data.get(k));
			}
			pw.close();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	public static boolean getNoPurchaseShown() {
		return "yes".equals(data.get("no-purchase-shown"));
	}
	public static void setNoPurchaseShown() {
		data.put("no-purchase-shown", "yes");
		spit();
	}
	public static boolean getIgnoreUpgrade() {
		return "yes".equals(data.get("ignore-upgrade"));
	}
	public static void setIgnoreUpgrade() {
		data.put("ignore-upgrade", "yes");
		spit();
	}
	public static boolean getIgnoreFeedback() {
		return "yes".equals(data.get("ignore-feedback"));
	}
	public static void setIgnoreFeedback() {
		data.put("ignore-feedback", "yes");
		spit();
	}
	public static void setAllowUsageReports() {
		data.put("submit-usage", "yes");
		spit();
	}
	public static boolean getAllowUsageReports() {
		return "yes".equals(data.get("submit-usage"));
	}
	
	
	

}
