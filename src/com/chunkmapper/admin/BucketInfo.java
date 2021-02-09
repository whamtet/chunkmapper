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
	private static HashMap<String, String> map;

	public static String mat() {
		return map.get("chunkmapper-mat");
	}
	public static String osm() {
		return map.get("chunkmapper-osm2");
	}
	public static String heights() {
		return map.get("chunkmapper-heights2");
	}
	public static String admin() {
		return map.get("chunkmapper-admin");
	}

	public static boolean initMap() {
		if (map == null) {
			HashMap<String, String> localMap = null;
			try {
				URL url = new URL(URLs.BUCKET_INFO);
				BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
				localMap = new HashMap<>();
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
}
