package com.chunkmapper.admin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Geocoder {
	private static final HashMap<String, double[]> m = new HashMap<String, double[]>();
	static {
		try {
			System.out.println("Starting");
			BufferedReader br = new BufferedReader(new FileReader("/Users/matthewmolloy/Downloads/GeoLite2-City-CSV_20140204/GeoLite2-City-Blocks.csv"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("::ffff:")) {
					String rest = line.substring(7);
					String[] split = rest.split(",");
					try {
						double lat = Double.parseDouble(split[6]);
						double lon = Double.parseDouble(split[7]);
						String ip = split[0];
						m.put(ip, new double[] {lat, lon});
					} catch (NumberFormatException e2) {

					}
				}
			}
			br.close();
			System.out.println(m.size());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {

	}
}
