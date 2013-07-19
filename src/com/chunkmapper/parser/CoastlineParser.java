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

public class CoastlineParser extends Parser {

	public static HashSet<Coastline> getCoastlines(ArrayList<String> lines) {

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
}
