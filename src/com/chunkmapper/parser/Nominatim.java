package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

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
	public static DoublePoint getPoint(String q) throws MalformedURLException, URISyntaxException, IOException {
		String response = getString(q);
		String latStr = getValue(response, "lat"), lonStr = getValue(response, "lon");
		return new DoublePoint(latStr, lonStr);
	}
	public static class DoublePoint {
		public final double lat, lon;
		
		public DoublePoint(String latStr, String lonStr) {
			lat = Double.parseDouble(latStr);
			lon = Double.parseDouble(lonStr);
		}
	}
}
