package com.chunkmapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;


public class Test {

	public static void main(String[] args) throws Exception {
		findRotorua();
	}
	private static void findRotorua() throws Exception {
		double lon1 = 176.1980, lon2 = 176.3436;
		double lat1 = -38.1478, lat2 = -38.0278;
//		URL url = new URL(String.format("http://www.overpass-api.de/api/xapi?way[natural=water][bbox=%s,%s,%s,%s]", lon1, lat1, lon2, lat2));
		URL url = new URL("http://www.overpass-api.de/api/xapi?way[id=6646541]");
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("osm.xml")));
		String line;
		while ((line = reader.readLine()) != null) {
			pw.println(line);
		}
		reader.close();
		pw.close();
		Runtime.getRuntime().exec("open osm.xml");
		
		
	}

}
