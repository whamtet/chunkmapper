package com.chunkmapper.parser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.XapiResourceInfo;
import com.chunkmapper.sections.POI;

public class POIParser extends Parser {

	public static HashSet<POI> getPois(ArrayList<String> lines) {

		HashSet<POI> pois = new HashSet<POI>();

		String type = null;
		String text = null;
		Point p = null;
		Integer population = null;
		int adminLevel = -1;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("node")) {
				double lat = Double.parseDouble(getValue(line, "lat"));
				double lon = Double.parseDouble(getValue(line, "lon"));
				p = new Point((int) (lon * 3600), (int) (-3600 * lat));
				type = null;
				population = null;
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				if (k.equals("place"))
					type = v;
				if (k.equals("name"))
					text = v;
				if (k.equals("population"))
					population = Integer.parseInt(v);
			}
			if (tag.equals("/node") && type != null) {
				pois.add(new POI(p, text, population, type));
			}
		}
		return pois;
	}
//	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("auckland, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		int i = 0;
//		for (POI poi : getPois(regionx, regionz)) {
//			System.out.println(poi.text);
//			i++;
//			if (i == 10)
//				break;
//		}
//	}
}
