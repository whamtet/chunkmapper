package com.chunkmapper.parser;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.admin.OSMRouter;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.sections.POI;

public class POIParser extends Parser {
	private static final ConcurrentHashMap<Point, HashSet<POI>> cache = new ConcurrentHashMap<Point, HashSet<POI>>(); 
	public static HashSet<POI> getPois(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		Point p = new Point(regionx, regionz);
		if (cache.containsKey(p)) {
			return cache.get(p);
		} else {
			HashSet<POI> pois = doGetPois(o, regionx, regionz);
			cache.put(p, pois);
			return pois;
		}
	}
	private static HashSet<POI> doGetPois(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		HashSet<POI> pois = new HashSet<POI>();
		
		for (Node node : o.nodes){
			String type = node.map.get("place");
			String type2 = node.map.get("sport");
			if (type2 != null)
				type = type2;
			String text = node.map.get("name");
			Integer population = null;
			if (node.map.containsKey("population")) {
				try {
				population = Integer.parseInt(node.map.get("population"));
				} catch (NumberFormatException e) {
					try {
						String s = node.map.get("population").split(" ")[0];
						population = Integer.parseInt(s);
					} catch (NumberFormatException e2) {
						
					}
				}
			}
			pois.add(new POI(node.point, text, population, type));
		}
		return pois;
	}

}
