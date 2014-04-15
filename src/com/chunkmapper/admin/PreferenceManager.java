package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import com.chunkmapper.writer.GenericWriter;

public class PreferenceManager {
	private static final File Cache = new File(Utila.CACHE, "preferences.txt");
	private static final HashMap<String, String> data = new HashMap<String, String>();
	static {
		if (Cache.exists()) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(Cache));
				String line;
				while ((line = br.readLine()) != null) {
					int i = line.indexOf(" ");
					data.put(line.substring(0, i), line.substring(i + 1));
				}
			} catch (IOException e) {
				MyLogger.LOGGER.severe(MyLogger.printException(e));
			}
		}
	}
	public static void setOrePrefs() {
		data.put("coal-width", GenericWriter.COAL_WIDTH + "");
		data.put("diamond-width", GenericWriter.DIAMOND_WIDTH + "");
		data.put("emerald-width", GenericWriter.EMERALD_WIDTH + "");
		data.put("gold-width", GenericWriter.GOLD_WIDTH + "");
		data.put("iron-width", GenericWriter.IRON_WIDTH + "");
		data.put("lapis-lazuli-width", GenericWriter.LAPIS_LAZULI_WIDTH + "");
		data.put("redstone-width", GenericWriter.REDSTONE_WIDTH + "");
		spit();
	}
	private static int getInt(String k) {
		return Integer.parseInt(data.get(k));
	}
	public static void activateOrePrefs() {
		if (data.containsKey("coal-width")) {
			GenericWriter.COAL_WIDTH = getInt("coal-width");
			GenericWriter.DIAMOND_WIDTH = getInt("diamond-width");
			GenericWriter.EMERALD_WIDTH = getInt("emerald-width");
			GenericWriter.GOLD_WIDTH = getInt("gold-width");
			GenericWriter.IRON_WIDTH = getInt("iron-width");
			GenericWriter.LAPIS_LAZULI_WIDTH = getInt("lapis-lazuli-width");
			GenericWriter.REDSTONE_WIDTH = getInt("redstone-width");
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
	public static String getInitLog() {
		return data.get("init-log");
	}
	public static void setInitLog(String s) {
		data.put("init-log", s);
		spit();
	}
	public static void main(String[] args) {
		System.out.println(getInitLog());
	}

}
