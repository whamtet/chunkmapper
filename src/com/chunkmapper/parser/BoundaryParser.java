package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.OSMRouter;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.Boundary;

public class BoundaryParser extends Parser {

	public static HashSet<Boundary> getBoundaries(OverpassObject o) {
		HashSet<Boundary> out = new HashSet<Boundary>();
		for (Way way : o.ways) {
			if ("administrative".equals(way.map.get("boundary"))) {
				String leftArea = null, rightArea = null;
				for (String k : way.map.keySet()) {
					if (k.startsWith("left:"))
						leftArea = way.map.get(k);
					if (k.startsWith("right:"))
						rightArea = way.map.get(k);
				}
				try {
					int adminLevel = Integer.parseInt(way.map.get("admin_level"));
					out.add(new Boundary(way.points, way.bbox, leftArea, rightArea, adminLevel));
				} catch (NumberFormatException e) {
				}
			}
		}
		return out;
	}
}
