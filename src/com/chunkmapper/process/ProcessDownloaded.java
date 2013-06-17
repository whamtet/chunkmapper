package com.chunkmapper.process;

import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.parser.RailParser;
import com.chunkmapper.rail.RailSection;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class ProcessDownloaded {
	private static class MyRectangle extends Rectangle2D.Double {
		public final double x2, y2;
		public MyRectangle(double x, double y, double width, double height) {
			super(x, y, width, height);
			x2 = x + width;
			y2 = y + width;
		}

		public boolean contains(MyRectangle other) {
			double eps = 1e-5;
			return this.x - eps < other.x && other.x2 < this.x2 + eps &&
					this.y - eps < other.y && other.y2 < this.y2 + eps;
		}
	}
	public static void main(String[] args) throws Exception {

		int latRad = 90 * 3600 / 512;
		int lonRad = 180 * 3600 / 512;
		String name = "xapirail";
		File parent = new File("/Users/matthewmolloy/workspace/geo-downloader/downloads/" + name);
		File firstChild = parent.listFiles()[1];
		File outParent = new File("processedDownloads/" + name);
		outParent.mkdirs();

		System.out.println(getLevel(0, parent));
//		Point testPoint = getPoint(fileToBounds(firstChild));
//
//		for (int regionx = 0; regionx <= lonRad; regionx++) {
//			int numValid = 0, total = 0;
//			for (int regionz = -latRad; regionz <= latRad; regionz++) {
//				total++;
//				if (getFile(parent, regionx, regionz) != null) {
//					numValid++;
//				}
//			}
//			System.out.println(numValid + " out of " + total);
//			
//		}

	}
	private static int getLevel(int level, File parent) {
		if (parent.isFile()) {
			return level + 1;
		}
		int maxLevel = level + 1;
		for (File f : parent.listFiles()) {
			int newLevel = getLevel(level + 1, f);
			if (newLevel > maxLevel) {
				maxLevel = newLevel;
			}
		}
		return maxLevel;
	}
	private static void process(File parent, File outParent, int regionx, int regionz) throws IOException, FileNotYetAvailableException {
		File f = getFile(parent, regionx, regionz);
		ArrayList<RailSection> railSections = RailParser.getRailSections(f);

		File outFile = new File(outParent, "f_" + regionx + "_" + regionz);
		System.out.println(outFile.getAbsolutePath());
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		for (RailSection section : railSections) {
			if (section.intersectsRegion(regionx, regionz)) {
				writer.write(section.toString());
			}
		}
		writer.close();
	}

	private static Point getPoint(Rectangle2D.Double rect) {
		double lon = rect.x, lat = rect.y;
		int regionx = (int) Math.round(lon * 3600 / 512);
		int regionz = (int) Math.round(-lat * 3600 / 512) - 1;
		return new Point(regionx, regionz);
	}
	private static File getFile(File parent, int regionx, int regionz) {
		MyRectangle myRectangle = getRectangle(regionx, regionz);

		for (File f : parent.listFiles()) {
			if (f.getName().startsWith("f_")) {
				if (fileToBounds(f).contains(myRectangle)) {
					if (f.isDirectory()) {
						return getFile(f, regionx, regionz);
					} else {
						return f;
					}
				}
			}
		}
		return null;
	}
	private static MyRectangle getRectangle(int regionx, int regionz) {
		double lon1 = regionx*512 / 3600.;
		double lat1 = -(regionz + 1) * 512 / 3600.;
		double width = 512 / 3600.;

		return new MyRectangle(lon1, lat1, width, width);
	}
	private static MyRectangle fileToBounds(File f) {
		String[] splitName = f.getName().split("_");
		double[] out = new double[4];

		for (int i = 0; i < 4; i++) {
			out[i] = Double.parseDouble(splitName[i+1]);
		}
		double x = out[0], y = out[1];
		double width = out[2] - out[0], height = out[3] - out[1];
		return new MyRectangle(x, y, width, height);
	}
	//	private static File getContainer(File parent, int regionx, int regionz) {
	//		
	//	}

}
