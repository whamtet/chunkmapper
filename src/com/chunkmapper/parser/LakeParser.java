package com.chunkmapper.parser;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import com.chunkmapper.Point;
import com.chunkmapper.parser.RiverParser.RiverSection;
import com.chunkmapper.resourceinfo.XapiLakeResourceInfo;
import com.chunkmapper.sections.Lake;

public class LakeParser extends Parser {

	public static ArrayList<Lake> getLakes(int regionx, int regionz) throws IOException {
		XapiLakeResourceInfo info = new XapiLakeResourceInfo(regionx, regionz);
		System.out.println(info.url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(info.url.openStream()));
		String lina;
		ArrayList<String> lines = new ArrayList<String>();
		while ((lina = reader.readLine()) != null) {
			lines.add(lina);
		}
		reader.close();
		HashMap<Long, Point> locations = getLocations(lines);
		System.out.println(locations.size());
		ArrayList<Lake> lakes = new ArrayList<Lake>();
		
		boolean isLake = false;
		boolean isCove = false;
		boolean isLagoon = false;
		boolean isRiver = false;
		int minx = Integer.MAX_VALUE, minz = Integer.MAX_VALUE;
		int maxx = Integer.MIN_VALUE, maxz = Integer.MIN_VALUE;
		
		ArrayList<Point> currentPoints = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("way")) {
				currentPoints = new ArrayList<Point>();
				isLake = false; isCove = false; isLagoon = false; isRiver = false;
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
				isLake |= k.equals("natural") && v.equals("water");
				isCove |= k.equals("water") && v.equals("cove");
				isLagoon |= k.equals("water") && v.equals("lagoon");
				isRiver |= k.equals("water") && v.equals("river");
				
			}
			if (tag.equals("/way") && isLake && !isRiver) {
				//public Lake(ArrayList<Point> points, Rectangle bbox, boolean isInner, boolean isCove, boolean isLagoon) {
				Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
				lakes.add(new Lake(currentPoints, bbox, false, isCove, isLagoon));
			}
		}
		return lakes;
	}
}
