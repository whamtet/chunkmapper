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
import com.chunkmapper.resourceinfo.XapiBoundaryResourceInfo;
import com.chunkmapper.sections.Boundary;
import com.chunkmapper.sections.Coastline;

public class BoundaryParser extends Parser {

	public static HashSet<Boundary> getCoastlines(int regionx, int regionz) throws IOException {
		XapiBoundaryResourceInfo info = new XapiBoundaryResourceInfo(regionx, regionz);
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
		HashSet<Boundary> boundaries = new HashSet<Boundary>();

		boolean isBoundary = false;
		String leftCountry = null, rightCountry = null;
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
				leftCountry = null;
				rightCountry = null;
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
				
			}
			if (tag.equals("/way") && isBoundary) {
				if (isBoundary) {
					Rectangle bbox = new Rectangle(minx, minz, maxx - minx, maxz - minz);
					boundaries.add(new Boundary(currentPoints, bbox, leftCountry, rightCountry));
				}
			}
		}
		return boundaries;
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("mong cai, vietnam");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		System.out.println(getCoastlines(regionx, regionz).size());
	}
}
