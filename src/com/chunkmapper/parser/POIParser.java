package com.chunkmapper.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassParser.Node;
import com.chunkmapper.parser.OverpassParser.OverpassObject;
import com.chunkmapper.sections.POI;

public class POIParser extends Parser {
	private static final ConcurrentHashMap<Point, HashSet<POI>> cache = new ConcurrentHashMap<Point, HashSet<POI>>(); 
	public static HashSet<POI> getPois(int regionx, int regionz) throws IOException {
		Point p = new Point(regionx, regionz);
		if (cache.containsKey(p)) {
			return cache.get(p);
		} else {
			HashSet<POI> pois = doGetPois(regionx, regionz);
			cache.put(p, pois);
			return pois;
		}
	}
	private static HashSet<POI> doGetPois(int regionx, int regionz) throws IOException {
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		HashSet<POI> pois = new HashSet<POI>();
		
		for (Node node : o.nodes){
			String type = node.map.get("place");
			String type2 = node.map.get("sport");
			if (type2 != null)
				type = type2;
			String text = node.map.get("name");
			Integer population = null;
			if (node.map.containsKey("population")) {
				population = Integer.parseInt(node.map.get("population"));
			}
			pois.add(new POI(node.point, text, population, type));
		}
		return pois;
	}

}
