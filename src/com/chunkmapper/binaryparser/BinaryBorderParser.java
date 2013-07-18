package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.protoc.BorderContainer;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RailRegionContainer.RailRegion;
import com.chunkmapper.protoc.RailSectionContainer;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.wrapper.BorderRegionBuilder;
import com.chunkmapper.rail.RailSection;
import com.chunkmapper.sections.Boundary;

public class BinaryBorderParser {

	public static HashSet<Boundary> getOfflineBorders(int regionx, int regionz) throws IOException {
		Rectangle currentRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);

		HashSet<Boundary> out = new HashSet<Boundary>();
		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/myborders");
		for (File f : parent.listFiles()) {
			if (f.getName().startsWith("f_")) {
				String[] split = f.getName().split("_");
				int x = Integer.parseInt(split[1]);
				int z = Integer.parseInt(split[2]);
				int width = Integer.parseInt(split[3]);
				int height = Integer.parseInt(split[4]);

				Rectangle fileRectangle = new Rectangle(x, z, width, height);
				if (fileRectangle.intersects(currentRectangle)) {
					InputStream in = new FileInputStream(f);
					BorderRegionBuilder builder = (BorderRegionBuilder) (new BorderRegionBuilder()).newBuilder(in);
					in.close();

					for (BorderContainer.BorderSection borderSection : builder.borderSections) {
						RectangleContainer.Rectangle sectionBbox = borderSection.getBbox();
						Rectangle sectionBbox2 = new Rectangle(sectionBbox.getX(), sectionBbox.getZ(), sectionBbox.getWidth(), sectionBbox.getHeight());

						if (sectionBbox2.intersects(currentRectangle)) {
							ArrayList<Point> points = new ArrayList<Point>();
							Point rootPoint = null;
							for (PointContainer.Point point : borderSection.getPointsList()) {
								if (rootPoint == null) {
									rootPoint = new Point(point.getX(), point.getZ());
									points.add(rootPoint);
								} else {
									points.add(new Point(point.getX() + rootPoint.x, point.getZ() + rootPoint.z));
								}
							}
							RectangleContainer.Rectangle rawRectangle = borderSection.getBbox();
							Rectangle bbox = new Rectangle(rawRectangle.getX(), rawRectangle.getZ(), rawRectangle.getWidth(), rawRectangle.getHeight());
							//public Boundary(ArrayList<Point> points, Rectangle bbox, String leftCountry, String rightCountry) {
							out.add(new Boundary(points, bbox, borderSection.getLeftArea(), borderSection.getRightArea()));
						}
					}
				}
			}
		}

		return out;
	}

}
