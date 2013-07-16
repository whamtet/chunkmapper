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

	public static HashSet<POI> getPois(int regionx, int regionz) throws IOException {
		XapiResourceInfo info = new XapiResourceInfo(regionx, regionz);
		Reader rawReader = FileValidator.checkValid(info.file) ? new FileReader(info.file) : new InputStreamReader(info.url.openStream());
		BufferedReader reader = new BufferedReader(rawReader);
		String lina;
		ArrayList<String> lines = new ArrayList<String>();
		while ((lina = reader.readLine()) != null) {
			lines.add(lina);
		}
		reader.close();

		if (!FileValidator.checkValid(info.file)) {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(info.file)));
			for (String line : lines) {
				pw.println(line);
			}
			pw.close();
			FileValidator.setValid(info.file);
		}


		HashSet<POI> pois = new HashSet<POI>();

		boolean isPoi = false;
		String text = null;
		Point p = null;
		Integer population = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("node")) {
				double lat = Double.parseDouble(getValue(line, "lat"));
				double lon = Double.parseDouble(getValue(line, "lon"));
				p = new Point((int) (lon * 3600), (int) (-3600 * lat));
				isPoi = false;
				population = null;
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isPoi |= k.equals("place");
				if (k.equals("name"))
					text = v;
				if (k.equals("population"))
					population = Integer.parseInt(v);
			}
			if (tag.equals("/node") && isPoi) {
				pois.add(new POI(p, text, population));
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
