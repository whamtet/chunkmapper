package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.protoc.RectangleContainer;
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
	public static ArrayList<RailSection> getRailSections2(int regionx, int regionz) throws IOException {
		Rectangle currentRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);
		
		ArrayList<RailSection> out = new ArrayList<RailSection>();
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrails");
		for (File f : parent.listFiles()) {
			if (f.getName().startsWith("f_")) {
				String[] split = f.getName().split("_");
				int x = Integer.parseInt(split[1]);
				int z = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);
				
				Rectangle fileRectangle = new Rectangle(x, z, width, height);
				if (fileRectangle.intersects(currentRectangle)) {
					FileInputStream in = new FileInputStream(f);
					byte[] data = new byte[(int) f.length()];
					in.close();
					
					RailRegion railRegion = RailRegion.parseFrom(data);
					for (RailSectionContainer.RailSection railSection : railRegion.getRailSectionsList()) {
						RectangleContainer.Rectangle sectionBbox = railSection.getBbox();
						Rectangle sectionBbox2 = new Rectangle(sectionBbox.getX(), sectionBbox.getZ(), sectionBbox.getWidth(), sectionBbox.getHeight());
						
						if (sectionBbox2.intersects(currentRectangle)) {
							//construct railSection
//							public RailSection(ArrayList<Point> points,
//									boolean isPreserved, boolean hasBridge, boolean hasCutting, boolean hasEmbankment, boolean hasTunnel) {
							ArrayList<Point> points = new ArrayList<Point>();
							for (PointContainer.Point point : railSection.getPointsList()) {
								points.add(new Point(point.getX(), point.getZ()));
							}
							out.add(new RailSection(points, railSection.getIsPreserved(), railSection.getHasBridge(),
									railSection.getHasCutting(), railSection.getHasEmbankment(), railSection.getHasTunnel()));
						}
					}
				}
			}
		}
		return out;
	}
	public static void main(String[] args) throws Exception {
		getRailSections2(0, 0);
	}
}
