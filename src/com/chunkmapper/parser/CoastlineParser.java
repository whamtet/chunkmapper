package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.XapiCoastlineResourceInfo;
import com.chunkmapper.sections.Coastline;
import com.chunkmapper.sections.Lake;

public class CoastlineParser extends Parser {

	public static HashSet<Coastline> getCoastlines(int regionx, int regionz) throws IOException {
		XapiCoastlineResourceInfo info = new XapiCoastlineResourceInfo(regionx, regionz);
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


		HashMap<Long, Point> locations = getLocations(lines);
		HashSet<Coastline> coastlines = new HashSet<Coastline>();

		boolean isCoastline = false;
		int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;

		ArrayList<Point> currentPoints = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isCoastline = false;
				minx = Integer.MAX_VALUE; minz = Integer.MAX_VALUE;
				maxx = Integer.MIN_VALUE; maxz = Integer.MIN_VALUE;
			}
			if (tag.equals("nd")) {
				long ref = Long.parseLong(RailParser.getValue(line, "ref"));
				Point p = locations.get(ref);

				if (p.x < minx)
					minx = p.x;
				if (p.z < minz)
					minz = p.z;

				if (p.x > maxx)
					maxx = p.x;
				if (p.z > maxz)
					maxz = p.z;

				currentPoints.add(locations.get(ref));
			}
			if (tag.equals("tag")) {
				String k = getValue(line, "k"), v = getValue(line, "v");
				isCoastline |= k.equals("natural") && v.equals("coastline");
			}
			if (tag.equals("/way") && isCoastline) {
				if (isCoastline) {
					Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
					coastlines.add(new Coastline(currentPoints, bbox));
				}
			}
		}
		return coastlines;
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("cape reinga");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		System.out.println(getCoastlines(regionx, regionz).size());
	}
}
