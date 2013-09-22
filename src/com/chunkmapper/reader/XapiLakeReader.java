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
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassParser;
import com.chunkmapper.parser.OverpassParser.OverpassObject;
import com.chunkmapper.parser.OverpassParser.Relation;
import com.chunkmapper.parser.OverpassParser.Way;
import com.chunkmapper.sections.Lake;
import com.chunkmapper.sections.RenderingSection;


public class XapiLakeReader implements LakeReader {
	private boolean[][] hasWater = new boolean[512][512];

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.LakeReader#hasWaterij(int, int)
	 */
	@Override
	public boolean hasWaterij(int i, int j) {
		return hasWater[i][j];
	}
	private static ArrayList<Lake> getLakes(int regionx, int regionz) throws IOException {
		//NOTEE!!!!!
//		OverpassObject o = OverpassParser.getTestObject(regionx, regionz);
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		
		HashSet<Lake> lakes = new HashSet<Lake>();
		for (Way way : o.ways) {
			if ("water".equals(way.map.get("natural"))) {
				Lake lake = new Lake(way.points, way.bbox);
//				System.out.println("***");
//				for (String k : way.map.keySet()) {
//					System.out.println(k + ": " + way.map.get(k));
//				}
				if (lake.isClosed()) {
					lakes.add(lake);
				} else {
					System.out.println("skipping");
				}
			}
		}
//		System.out.println("Relations");
		for (Relation relation : o.relations) {
			if ("water".equals(relation.map.get("natural")) && "multipolygon".equals(relation.map.get("type"))) {
				for (Way way : relation.ways) {
//					Lake lake = new Lake(way.points, way.bbox);
					lakes.add(new Lake(way.points, way.bbox));
//					if (lake.isOpen()) {
//						System.out.println(lake.points.size());
//					}
				}
//				System.out.println("***");
//				for (String k : relation.map.keySet()) {
//					System.out.println(k + ": " + relation.map.get(k));
//				}
			}
		}
		Lake stub = null;
		ArrayList<Lake> closedLakes = new ArrayList<Lake>();
		
		while (lakes.size() > 0) {
			ArrayList<Lake> attachedLakes = new ArrayList<Lake>();
			for (Lake lake : lakes) {
				if (stub == null) {
					stub = lake;
					attachedLakes.add(lake);
				} else if (stub.attachEitherEnd(lake)) {
					attachedLakes.add(lake);
				}
				if (stub.isClosed())
					break;
			}
			for (Lake lake : attachedLakes) {
				lakes.remove(lake);
			}
			if (stub.isClosed()) {
				closedLakes.add(stub);
				stub = null;
			} else if (attachedLakes.isEmpty()) {
//				System.out.println("discarding");
//				closedLakes.add(stub);
				stub = null;
			}
		}
		return closedLakes;
//				ArrayList<Point> points = new ArrayList<Point>();
//				for (Way way : relation.ways) {
//					if ("outer".equals(way.map.get("role"))) {
//						for (Point point : way.points){
//							points.add(point);
//						}
//					}
//				}
//				lakes.add(new Lake(points, relation.bbox, false, false));
////				for (Way way : relation.ways){
////					if ("inner".equals(way.map.get("role"))) {
////						lakes.add(new Lake(way.points, relation.bbox, false, false));
////					}
////				}
//			}
//		}
//		return lakes;
	}

	public XapiLakeReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException {


		ArrayList<Lake> lakes = getLakes(regionx, regionz);

		//		ArrayList<Lake> openLakes = new ArrayList<Lake>(), closedLakes = new ArrayList<Lake>();
		//		for (Lake lake : lakes) {
		//			if (lake.isClosed()) {
		//				closedLakes.add(lake);
		//			} else {
		//				openLakes.add(lake);
		//			}
		//		}
		//		outer: for (Lake lakeToClose : openLakes) {
		//			for (Lake closedLake : closedLakes) {
		//				if (closedLake.contains(lakeToClose)) {
		//					continue outer;
		//				}
		//			}
		//			int i;
		//			for (i = 0; lakeToClose.isOpen() && i < 100; i++) {
		//				Point endPoint = lakeToClose.getEndPoint();
		//				int regionxd = Matthewmatics.div(endPoint.x, 512), regionzd = Matthewmatics.div(endPoint.z, 512);
		//				Collection<Lake> lakes2 = regionxd == regionx && regionzd == regionz ?
		//						openLakes : getLakes(regionxd, regionzd);
		//				for (Lake lake : lakes2) {
		//					lakeToClose.connect(lake);
		//					if (lakeToClose.isClosed()) {
		//						closedLakes.add(lakeToClose);
		//						break;
		//					}
		//				}
		//			}
		//			if (i == 100) {
		//				System.out.println("quitting");
		//				return;
		//			}
		//		}

		ArrayList<RenderingSection> sections = new ArrayList<RenderingSection>();
		for (Lake lake : lakes) {
			ArrayList<Point> points = lake.points;
			if (lake.isClosed())
				points.remove(points.size() - 1);

			for (int i = 0; i < points.size(); i++) {
				Point previous = wrappedGet(points, i - 1);
				Point a = points.get(i), b = wrappedGet(points, i + 1);
				if (a.z != b.z) {
					int j = i - 1;
					while (previous.z == a.z) {
						j--;
						previous = wrappedGet(points, j);
					}

					sections.add(new RenderingSection(previous, a, b));
				}
			}
		}
		//now go through each row
		for (int z = 0; z < 512; z++) {
			int absz = z + regionz * 512;
			ArrayList<Integer> intersections = new ArrayList<Integer>();
			for (int i = 0; i < sections.size(); i++) {
				RenderingSection section = sections.get(i);
				Integer intersection = section.getIntersection(absz);
				if (intersection != null) {
					intersections.add(intersection);
				}
			}

			Collections.sort(intersections);

			for (int i = 1; i < intersections.size(); i += 2) {

				int a = intersections.get(i-1) - regionx * 512, b = intersections.get(i) - regionx * 512;
				if (a < 0)
					a = 0;
				if (b > 511)
					b = 511;
				if (a == b) {
					hasWater[z][a] = true;
				} else {
					for (int x = a; x <= b; x++) {
						hasWater[z][x] = true;
					}
				}
			}

		}
	}
	private static <E> E wrappedGet(ArrayList<E> list, int i) {
		return list.get(Matthewmatics.mod(i, list.size()));
	}
	//	private static class MyPoint extends com.chunkmapper.Point implements Comparable<Point> {
	//
	//		public MyPoint(int x, int z) {
	//			super(x, z);
	//			// TODO Auto-generated constructor stub
	//		}
	//
	//		@Override
	//		public int compareTo(Point arg0) {
	//			Integer a = x;
	//			Integer b = arg0.x;
	//			return a.compareTo(b);
	//		}
	//
	//	}

	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("rotorua, nz");
		//		double[] latlon = Nominatim.getPoint("te anau, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512)-1;
//		for (Lake lake : getLakes(regionx, regionz)) {
//			System.out.println(lake.isClosed());
//		}
		LakeReader reader = new XapiLakeReader(regionx, regionz);

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int z = 0; z < 512; z++) {
			for (int x = 0; x < 512; x++) {
				pw.println(reader.hasWaterij(z, x) && (z > 0 || x > 0) ? 0 : 1);
			}
		}
		pw.close();
		System.out.println("done");
	}

}
