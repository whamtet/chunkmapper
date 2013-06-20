package com.chunkmapper.binaryparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.rail.RailSection;

public class MyRailParser {

	public static ArrayList<RailSection> getRailSections(int regionx, int regionz) throws IOException {
		File f = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrails/f_" + regionx + "_" + regionz + ".pb");
		FileInputStream in = new FileInputStream(f);
		RailRegion railRegion = RailRegion.parseFrom(in);
		in.close();
		
		ArrayList<RailSection> out = new ArrayList<RailSection>();
		
//		public RailSection(ArrayList<Point> points,
//				boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel) {
		
		for (RailSectionContainer.RailSection raw : railRegion.getRailSectionsList()) {
			ArrayList<Point> points = new ArrayList<Point>();
			for (PointContainer.Point rawPoint : raw.getPointsList()) {
				points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
			}
			out.add(new RailSection(points, raw.getIsPreserved(), raw.getHasBridge(),
					raw.getHasCutting(), raw.getHasEmbankment(), raw.getHasTunnel()));
		}
		return out;
	}
}
