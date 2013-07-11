package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Stack;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.BinaryCoastlineParser;
import com.chunkmapper.sections.Coastline;

public class XapiCoastlineReader {
	//	private boolean[][] data = new boolean[512][512];
	private int[][] data = new int[512][512];
	private static final int LAND = 2, WATER = -2, COAST = 3;
	
	public boolean isCoastij(int i, int j) {
		int data2 = data[i][j];
		return data2 == COAST || data2 == 1 || data2 == -1;
	}

	public boolean hasWaterij(int i, int j) {
		return data[i][j] == WATER;
	}
	public int getValueij(int i, int j) {
		return data[i][j];
	}
	private static void interpolate(int[][] mask, Point p1, Point p2, int regionx, int regionz, int fill) {
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			if (z0 >= 0 && x0 >= 0 && z0 < 512 && x0 < 512)
				mask[z0][x0] = fill;
			return;
		}
		int width = xstride >= 0 ? xstride : -xstride;
		int height = zstride >= 0 ? zstride : -zstride;
		int xstep = xstride >= 0 ? 1 : -1;
		int zstep = zstride >= 0 ? 1 : -1;

		if (width >= height) {
			for (int i = 0; i <= width; i++) {
				int x = x0 + i*xstep;
				int z = z0 + i * zstride / width;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = fill;
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = fill;
			}
		}
	}
	private static void floodFill(int[][] data, int i1, int j1, int fill) {
		// data == hasLand
		int h = data.length;
		int w = data[0].length;

		Stack<Integer> is = new Stack<Integer>();
		Stack<Integer> js = new Stack<Integer>();
		is.add(i1);
		js.add(j1);

		while (is.size() > 0) {
			int i = is.pop();
			int j = js.pop();

			int jd;
			for (jd = j; jd < w && data[i][jd] == 0; jd++) {
				data[i][jd] = fill;
				if (i > 0 && data[i-1][jd] == 0) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == 0) {
					is.add(i+1);
					js.add(jd);
				}
			}
			for (jd = j - 1; jd >= 0 && data[i][jd] == 0; jd--) {
				data[i][jd] = fill;
				if (i > 0 && data[i-1][jd] == 0) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == 0) {
					is.add(i+1);
					js.add(jd);
				}
			}
		}
	}
	public XapiCoastlineReader(int regionx, int regionz, GlobcoverReader coverReader) throws IOException, URISyntaxException, DataFormatException {

		HashSet<Coastline> coastlines = BinaryCoastlineParser.getCoastlines(regionx, regionz);
		if (coastlines.size() == 0) {
			int fill = coverReader.mostlyLand() ? LAND : WATER;
			if (coverReader.mostlyLand()) {
				for (int i = 0; i < 512; i++) {
					for (int j = 0; j < 512; j++) {
						data[i][j] = fill;
					}
				}
			}
			return;
		}
		for (Coastline coastline : coastlines) {
//			if (coastline.isSinglePoint()) {
//				Point p = coastline.points.get(0);
//				int x0 = regionx * 512, z0 = regionz * 512;
//				data[p.z - z0][p.x - x0] = LAND;
//			} else {
				int limit = coastline.points.size() - 1;
				for (int i = 0; i < limit; i++) {
					Point p1 = coastline.points.get(i), p2 = coastline.points.get(i+1);
					int fill;
					if (p1.z == p2.z) {
						fill = COAST;
					} else {
						fill = p1.z > p2.z ? 1 : -1;
					}
					interpolate(data, p1, p2, regionx, regionz, fill);
				}
//			}
		}
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				if (data[i][j] == 0) {
					//need to fill
					if (j < 511 && (data[i][j+1] == 1 || data[i][j+1] == -1)) {
						boolean isDown = data[i][j+1] == -1;
						int fill = isDown ? WATER : LAND;
						floodFill(data, i, j, fill);
					}
					if (j > 0 && (data[i][j-1] == 1 || data[i][j-1] == -1)) {
						boolean isDown = data[i][j-1] == -1;
						int fill = isDown ? LAND : WATER;
						floodFill(data, i, j, fill);
					}
				}
			}
		}
		//		int fill = 1;
		//		ArrayList<FillDatum> filldatums = new ArrayList<FillDatum>();
		//		for (int i = 0; i < 512; i++) {
		//			for (int j = 0; j < 512; j++) {
		//				if (data[i][j] == 0) {
		//					fill++;
		//					floodFill(data, i, j, fill);
		//					filldatums.add(new FillDatum(data, fill, heightsReader));
		//				}
		//			}
		//		}
		//		for (int filld = 2; filld <= fill; filld++) {
		//			int total = 0, numWater = 0;
		//			for (int i = 0; i < 512; i++) {
		//				for (int j = 0; j < 512; j++) {
		//					if (data[i][j] == filld) {
		//						total++;
		//						if (heightsReader.isWaterij(i, j)) {
		//							numWater++;
		//						}
		//					}
		//				}
		//			}
		//			if (numWater >= total / 4) {
		////			if (true) {
		//				//wipe out this color
		////				System.out.println("wiping color " + filld);
		//				for (int i = 0; i < 512; i++) {
		//					for (int j = 0; j < 512; j++) {
		//						if (data[i][j] == filld) {
		//							data[i][j] = 0;
		//						}
		//					}
		//				}
		//			}
		//		}

	}
	//	private static class FillDatum {
	//		private final double meanHeight;
	//		public FillDatum(int[][] data, int fill, HeightsReader heightsReader) {
	//			double s = 0;
	//			int n = 0;
	//			for (int i = 0; i < 512; i++) {
	//				for (int j = 0; j < 512; j++) {
	//					if (data[i][j] == fill) {
	//						n++;
	//					}
	//				}
	//			}
	//		}
	//	}

	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("auckland, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512)+1;
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		GlobcoverReader coverReader = new GlobcoverReader(regionx, regionz);
		XapiCoastlineReader reader = new XapiCoastlineReader(regionx, regionz, coverReader);
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
//				pw.println(reader.getValueij(i, j));
												pw.println(reader.hasWaterij(i, j) || i == 0 && j == 0 ? 0 : 1);
			}
		}
		pw.close();
		System.out.println("done");
	}
}
