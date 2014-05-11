package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;

import com.chunkmapper.Point;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.reader.POIReader;
import com.chunkmapper.reader.POIReader.SpecialPlace;

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
		MyLogger.LOGGER.info("Find coordinates for " + q);
		for (SpecialPlace specialPlace : POIReader.specialPlaces) {
			if (q.toLowerCase().trim().equals(specialPlace.toString().toLowerCase().trim())) {
				return specialPlace.latlon;
			}
		}
		try {
			String response = getString(q);
			String latStr = getValue(response, "lat"), lonStr = getValue(response, "lon");
			double lat = Double.parseDouble(latStr), lon = Double.parseDouble(lonStr);
			return new double[] {lat, lon};
		} catch (NumberFormatException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}
	public static double[] getPointSafe(String q) {
		MyLogger.LOGGER.info("Find coordinates for " + q);
		for (SpecialPlace specialPlace : POIReader.specialPlaces) {
			if (q.toLowerCase().trim().equals(specialPlace.toString().toLowerCase().trim())) {
				return specialPlace.latlon;
			}
		}
		try {
			String response = getString(q);
			String latStr = getValue(response, "lat"), lonStr = getValue(response, "lon");
			double lat = Double.parseDouble(latStr), lon = Double.parseDouble(lonStr);
			return new double[] {lat, lon};
		} catch (Exception e) {
			MyLogger.LOGGER.warning(MyLogger.printException(e));
			return null;
		}
	}
	public static Point getRegionPoint(String q) {
		
		double[] latlon = getPointSafe(q);
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		return new Point(regionx, regionz);
	}
}
