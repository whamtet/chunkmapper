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
				e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	public static boolean getIgnoreUpgrade() {
		return "yes".equals(data.get("ignore-upgrade"));
	}
	public static void setIgnoreUpgrade() {
		data.put("ignore-upgrade", "yes");
		spit();
	}
	
	
	

}
