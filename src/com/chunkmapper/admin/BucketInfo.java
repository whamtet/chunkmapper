package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class BucketInfo {
	private static HashMap<String, String> map;
	private static Object key = new Object();
	
	public static boolean allowLive() {
		return getBucket("allow-live").equals("yes");
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
			URL url = new URL("http://chunkbackend.appspot.com/static/buckets.txt");
			br = new BufferedReader(new InputStreamReader(url.openStream()));
			map = new HashMap<String, String>();
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				map.put(split[0], split[1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			map = null;
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		System.out.println(getBucket("chunkmapper-admin"));
	}

}
