package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.chunkmapper.gui.dialog.NoNetworkDialog;

public class BucketInfo {
	private static HashMap<String, String> map;
	private static Object key = new Object();

	public static void main(String[] args) {
		System.out.println(versionSupported());

	}
	public static boolean allowLive() throws IOException {
		return "yes".equals(getBucket("allow-live"));
	}
	public static boolean mustUpgrade() {
		return "yes".equals(getBucket("mu"));
	}
	public static boolean versionSupported() {
		return getBucket("supported-versions").contains(Utila.VERSION);
	}
	public static boolean spUpgradeAvailable() {
		return !Utila.VERSION.equals(getBucket("latest-sp"));
	}
	public static boolean mpUpgradeAvailable() {
		return !Utila.VERSION.equals(getBucket("latest-mp"));
	}
	public static String getBucket(String key) {
		synchronized(key) {
			while (map == null) {
				initMap();
				if (map == null)
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						throw new RuntimeException("interrupted");
					}
			}
		}
		return map.get(key);
	}
	private static void initMap() {
		BufferedReader br = null;
		try {
			URL url = new URL(URLs.BUCKET_INFO);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			map = new HashMap<String, String>();
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				map.put(split[0], split[1]);
			}
			br.close();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
			NoNetworkDialog d = new NoNetworkDialog();
			d.setVisible(true);
			if (!d.continueGeneration) {
				System.exit(0);
			}
		}
	}
	public static boolean multiplayerInitMap() {
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

	/**
	 * @param args
	 */

}
