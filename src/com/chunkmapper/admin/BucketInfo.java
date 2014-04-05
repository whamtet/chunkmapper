package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.chunkmapper.gui.dialog.NoNetworkDialog;

public class BucketInfo {
	public static HashMap<String, String> map;
	private static Object key = new Object();

	public static void main(String[] args) {
		System.out.println(versionSupported());

	}
	public static boolean allowLive() throws IOException {
		return "yes".equals(map.get("allow-live"));
	}
	public static boolean mustUpgrade() {
		return "yes".equals(map.get("mu"));
	}
	public static boolean versionSupported() {
		return map.get("supported-versions").contains(Utila.VERSION);
	}
	public static boolean spUpgradeAvailable() {
		return !Utila.VERSION.equals(map.get("latest-sp"));
	}
	public static boolean mpUpgradeAvailable() {
		return !Utila.VERSION.equals(map.get("latest-mp"));
	}

	public static boolean initMap() {
		HashMap<String, String> localMap = null;
		try {
			URL url = new URL(URLs.BUCKET_INFO);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			localMap = new HashMap<String, String>();
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				localMap.put(split[0], split[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		map = localMap;
		return localMap != null;
	}
	public static String getLogUserUrl() {
		return map.get("log-user-url");
	}

	/**
	 * @param args
	 */

}
