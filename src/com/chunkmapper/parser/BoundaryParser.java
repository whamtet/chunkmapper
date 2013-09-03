package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassParser.OverpassObject;
import com.chunkmapper.parser.OverpassParser.Way;
import com.chunkmapper.sections.Boundary;

public class BoundaryParser extends Parser {
	
	public static HashSet<Boundary> getBoundaries(int regionx, int regionz) throws IOException {
		HashSet<Boundary> out = new HashSet<Boundary>();
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		for (Way way : o.ways) {
			if ("administrative".equals(way.map.get("boundary"))) {
				String leftArea = null, rightArea = null;
				for (String k : way.map.keySet()) {
					if (k.startsWith("left:"))
						leftArea = way.map.get(k);
					if (k.startsWith("right:"))
						rightArea = way.map.get(k);
				}
				int adminLevel = Integer.parseInt(way.map.get("admin_level"));
				out.add(new Boundary(way.points, way.bbox, leftArea, rightArea, adminLevel));
			}
		}
		return out;
	}

	public static HashSet<Boundary> getBoundaries(ArrayList<String> lines) {
		HashMap<Long, Point> locations = getLocations(lines);
		HashSet<Boundary> boundaries = new HashSet<Boundary>();

		boolean isBoundary = false;
		String leftArea = null, rightArea = null;
		int adminLevel = 100;
		int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;

		ArrayList<Point> currentPoints = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isBoundary = false;
				leftArea = null;
				rightArea = null;
				adminLevel = 100;
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
				isBoundary |= k.equals("boundary") && v.equals("administrative");
//				if (k.equals("left:country"))
				if (k.startsWith("left:"))
					leftArea = v;
//				if (k.equals("right:country"))
				if (k.startsWith("right:"))
					rightArea = v;
				if (k.equals("admin_level"))
					adminLevel = Integer.parseInt(v);
			}
			if (tag.equals("/way") && isBoundary) {
				if (isBoundary) {
					Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
					boundaries.add(new Boundary(currentPoints, bbox, leftArea, rightArea, adminLevel));
				}
			}
		}
		return boundaries;
	}
}
