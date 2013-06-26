package com.chunkmapper.binaryparser;

import geocode.core;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.parser.RiverParser.RiverSection;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RiverContainer;
import com.chunkmapper.protoc.RiverContainer.RiverRegion;
import com.chunkmapper.protoc.admin.FileListManager;
import com.chunkmapper.rail.RailSection;

public class BinaryRiverParser {

	public static ArrayList<RiverSection> getRiverSections(int regionx, int regionz) throws IOException, URISyntaxException {
		Rectangle myRectangle = new Rectangle(regionx * 512, regionz*512, 512, 512);
		BinaryRiverCache cache = new BinaryRiverCache();

		ArrayList<RiverSection> out = new ArrayList<RiverSection>();
		
		for (FileInfo info : FileListManager.railFileList.getFilesList()) {
			String[] split = info.getFile().split("_");

			int x = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			int width = Integer.parseInt(split[3]);
			int height = Integer.parseInt(split[4]);

			Rectangle fileRectangle = new Rectangle(x, z, width, height);
			if (fileRectangle.intersects(myRectangle)) {
				for (RiverSection section : cache.getSections(info)) {
					if (section.bbox.intersects(myRectangle)) {
						out.add(section);
					}
				}
			}
		}
		cache.shutdown();
		return out;
	}


	public static ArrayList<RiverSection> getOfflineRiverSections(int regionx, int regionz) throws IOException {
		Rectangle currentRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);

		ArrayList<RiverSection> out = new ArrayList<RiverSection>();
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myrivers");
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

					RiverRegion riverRegion = RiverRegion.parseFrom(data);
					for (RiverContainer.RiverSection riverSection : riverRegion.getRiverSectionsList()) {
						RectangleContainer.Rectangle sectionBbox = riverSection.getBbox();
						Rectangle sectionBbox2 = new Rectangle(sectionBbox.getX(), sectionBbox.getZ(), sectionBbox.getWidth(), sectionBbox.getHeight());

						if (sectionBbox2.intersects(currentRectangle)) {

							ArrayList<Point> points = new ArrayList<Point>();
							Point rootPoint = null;
							for (PointContainer.Point point : riverSection.getPointsList()) {
								if (rootPoint == null) {
									rootPoint = new Point(point.getX(), point.getZ());
									points.add(rootPoint);
								} else {
									points.add(new Point(point.getX() + rootPoint.x, point.getZ() + rootPoint.z));
								}
							}
							out.add(new RiverSection(points));
						}
					}
				}

			}
		}

		return out;
	}


}
