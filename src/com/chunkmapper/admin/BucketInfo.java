package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class BucketInfo {
	private static HashMap<String, String> map;
	private static Object key = new Object();
	
	public static boolean allowLive() throws IOException {
		return "yes".equals(getBucket("allow-live"));
	}
	public static boolean mustUpgrade() {
		try {
			return "yes".equals(getBucket("mu"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static String getBucket(String key) throws IOException {
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
	private static void initMap() throws IOException {
		BufferedReader br = null;
//		try {
			URL url = new URL(URLs.BUCKET_INFO);
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			map = new HashMap<String, String>();
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				map.put(split[0], split[1]);
			}
			br.close();
	}

	/**
	 * @param args
	 */

}
