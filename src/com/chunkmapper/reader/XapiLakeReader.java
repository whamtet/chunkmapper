package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.LakeParser;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.sections.Lake;
import com.chunkmapper.sections.Section;


public class XapiLakeReader {
	//	private boolean[][] hasWater = new boolean[512][512];
	private int[][] data = new int[512][512];
	
	public boolean hasWaterij(int i, int j) {
		return data[i][j] != 0;
	}
	public int getValij(int i, int j) {
		return data[i][j];
	}
	private static void interpolate(boolean[][] mask, Point p1, Point p2, int regionx, int regionz) {
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			if (z0 >= 0 && x0 >= 0 && z0 < 512 && x0 < 512)
				mask[z0][x0] = true;
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
					mask[z][x] = true;
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = true;
			}
		}
	}



	public XapiLakeReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException {

		HashSet<Lake> lakes = LakeParser.getLakes(regionx, regionz);

		ArrayList<Section> sections = new ArrayList<Section>();
		for (Lake lake : lakes) {
			ArrayList<Point> points = new ArrayList<Point>();
			Point previousPoint = null;
			for (Point p : lake.points) {
				if (!p.equals(previousPoint)) {
					points.add(p);
					previousPoint = p;
				}
			}

			boolean isClosed = points.get(0).equals(points.get(points.size() - 1));
			if (isClosed) {
				points.remove(points.size() - 1);
				for (int i = 0; i < points.size(); i++) {
					Point previous = wrappedGet(points, i - 1);
					Point a = points.get(i), b = wrappedGet(points, i + 1);
					
					int j = i - 1;
					while (previous.z == a.z) {
						j--;
						previous = wrappedGet(points, j);
					}
					
					Section section = new Section(previous, a, b);
					if (!section.isHorizontal) {
						sections.add(section);
					}
				}
			} else {
				throw new RuntimeException("not closed");
			}
		}
		System.out.println("***");
		//now go through each row
		for (int z = 0; z < 512; z++) {
			int absz = z + regionz * 512;
			ArrayList<MyPoint> intersections = new ArrayList<MyPoint>();
			//			ArrayList<Integer> intersections = new ArrayList<Integer>();
			//			for (Section section : sections) {
			for (int i = 0; i < sections.size(); i++) {
				Section section = sections.get(i);
				Integer intersection = section.getIntersection(absz);
				if (intersection != null) {
					intersections.add(new MyPoint(intersection, i));
				}
			}

			Collections.sort(intersections);

			for (int i = 1; i < intersections.size(); i += 2) {

				int a = intersections.get(i-1).x - regionx * 512, b = intersections.get(i).x - regionx * 512;
				if (a < 0)
					a = 0;
				if (b > 511)
					b = 511;
				int c = intersections.get(i-1).z, d = intersections.get(i).z;
				if (a == b) {
					data[z][a] = c;
				} else {
					for (int x = a; x <= b; x++) {
						data[z][x] = c + (d - c) * (x - a) / (b - a);
					}
				}
			}

		}
	}
	private static <E> E wrappedGet(ArrayList<E> list, int i) {
		return list.get(Matthewmatics.mod(i, list.size()));
	}
	private static class MyPoint extends com.chunkmapper.Point implements Comparable<Point> {

		public MyPoint(int x, int z) {
			super(x, z);
			// TODO Auto-generated constructor stub
		}

		@Override
		public int compareTo(Point arg0) {
			Integer a = x;
			Integer b = arg0.x;
			return a.compareTo(b);
		}

	}

	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("lake taupo, nz");
				double[] latlon = Nominatim.getPoint("te anau, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		XapiLakeReader reader = new XapiLakeReader(regionx, regionz);

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int z = 0; z < 512; z++) {
			for (int x = 0; x < 512; x++) {
				pw.println(reader.getValij(z, x));
			}
		}
		pw.close();
		System.out.println("done");
	}
	private static void printLake(Lake lake, int regionx, int regionz) {
		System.out.println(String.format("%s, %s, %s, %s", lake.bbox.x - regionx*512,
				lake.bbox.y - regionz * 512, lake.bbox.width, lake.bbox.height));
	}

}
