package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

public class BucketInfo {
	
	/*
	 * Class that queries Amazon S3 to get version info about Chunkmapper.
	 */
	public static HashMap<String, String> map;
	private static Object key = new Object();

	public static void main(String[] args) {
		initMap();
		System.out.println(map.get("chunkmapper-admin"));
	}
	public static boolean allowLive() throws IOException {
		if (map == null) return true;
		return "yes".equals(map.get("allow-live"));
	}
	public static boolean versionSupported() {
		return map.get("supported-versions").contains(Utila.VERSION);
	}
	public static String imageEndpoint() {
		return map.get("image-endpoint");
	}

	public static boolean initMap() {
		if (map == null) {
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
		}
		return map != null;
	}
	public static String getLogUserUrl() {
		return map.get("log-user-url");
	}

	/**
	 * @param args
	 */

}
