package com.chunkmapper.binaryparser;

import geocode.core;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.admin.OfflineFileListManager;
import com.chunkmapper.rail.RailSection;

public class BinaryRailParser {
	
	public static HashSet<RailSection> getRailSections(int regionx, int regionz) throws IOException, URISyntaxException, DataFormatException {
		Rectangle myRectangle = new Rectangle(regionx * 512, regionz*512, 512, 512);
		BinaryRailCache binaryRailCache = new BinaryRailCache(true);
		
		HashSet<RailSection> out = new HashSet<RailSection>();
		for (FileInfo info : OfflineFileListManager.railFileList.getFilesList()) {
			String[] split = info.getFile().split("_");
			
			int x = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			int width = Integer.parseInt(split[3]);
			int height = Integer.parseInt(split[4]);
			
			Rectangle fileRectangle = new Rectangle(x, z, width, height);
			if (fileRectangle.intersects(myRectangle)) {
				for (RailSection railSection : binaryRailCache.getSections(info)) {
					if (railSection.bbox.intersects(myRectangle)) {
						out.add(railSection);
					}
				}
			}
		}
		return out;
	}
	
	
	public static ArrayList<RailSection> getOfflineRailSections(int regionx, int regionz) throws IOException {
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
					DataInputStream in = new DataInputStream(new FileInputStream(f));
					byte[] data = new byte[(int) f.length()];
					in.readFully(data);
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
							Point rootPoint = null;
							for (PointContainer.Point point : railSection.getPointsList()) {
								if (rootPoint == null) {
									rootPoint = new Point(point.getX(), point.getZ());
									points.add(rootPoint);
								} else {
									points.add(new Point(point.getX() + rootPoint.x, point.getZ() + rootPoint.z));
								}
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
		double[] latlon = core.placeToCoords("auckland, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		
		System.out.println(getRailSections(regionx, regionz).size());
	}
	
}
