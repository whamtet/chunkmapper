package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class Nominatim extends Parser {

	private static String getString(String q) throws URISyntaxException, MalformedURLException, IOException {
		URI uri = new URI("http", "nominatim.openstreetmap.org", "/search/" + q, "format=xml", null);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(uri.toURL().openStream()));
		String out = "";
		String inputline;
		while ((inputline = in.readLine()) != null) {
			out += inputline;
		}
		in.close();
		return out;
	}
	public static double[] getPoint(String q) throws MalformedURLException, URISyntaxException, IOException {
		String response = getString(q);
		String latStr = getValue(response, "lat"), lonStr = getValue(response, "lon");
		double lat = Double.parseDouble(latStr), lon = Double.parseDouble(lonStr);
		return new double[] {lat, lon};
	}
	public static void main(String[] args) {
		double[] latlon = geocode.core.placeToCoords("rotorua, nz");
		//		double[] latlon = Nominatim.getPoint("te anau, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		System.out.println("f_" + regionx + "_" + regionz + ".xml");
	}
}
