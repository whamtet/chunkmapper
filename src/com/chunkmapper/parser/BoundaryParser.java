package com.chunkmapper.parser;

import java.util.HashSet;
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
