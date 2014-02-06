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
import com.chunkmapper.admin.OSMRouter;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Relation;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.Glacier;
import com.chunkmapper.sections.RenderingSection;


public class GlacierReader {
	private boolean[][] hasGlacier = new boolean[512][512];

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.LakeReader#hasWaterij(int, int)
	 */
	public boolean hasGlacierij(int i, int j) {
		return hasGlacier[i][j];
	}
	private static ArrayList<Glacier> getLakes(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {

		OverpassObject o = OSMRouter.getObject(regionx, regionz);
		
		HashSet<Glacier> lakes = new HashSet<Glacier>();
		for (Way way : o.ways) {
			if ("glacier".equals(way.map.get("natural"))) {
				Glacier lake = new Glacier(way.points, way.bbox);
				if (lake.isClosed()) {
					lakes.add(lake);
				} else {
					System.out.println("skipping");
				}
			}
		}
		for (Relation relation : o.relations) {
			if ("glacier".equals(relation.map.get("natural")) && "multipolygon".equals(relation.map.get("type"))) {
				for (Way way : relation.ways) {
					lakes.add(new Glacier(way.points, way.bbox));
				}
			}
		}
		Glacier stub = null;
		ArrayList<Glacier> closedLakes = new ArrayList<Glacier>();
		
		while (lakes.size() > 0) {
			ArrayList<Glacier> attachedLakes = new ArrayList<Glacier>();
			for (Glacier lake : lakes) {
				if (stub == null) {
					stub = lake;
					attachedLakes.add(lake);
				} else if (stub.attachEitherEnd(lake)) {
					attachedLakes.add(lake);
				}
				if (stub.isClosed())
					break;
			}
			for (Glacier lake : attachedLakes) {
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
	}

	public GlacierReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException, InterruptedException {


		ArrayList<Glacier> lakes = getLakes(regionx, regionz);

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
		for (Glacier lake : lakes) {
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
					hasGlacier[z][a] = true;
				} else {
					for (int x = a; x <= b; x++) {
						hasGlacier[z][x] = true;
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
		System.out.println("starting");
		double[] latlon = Nominatim.getPoint("nassihorn, switzerland");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		GlacierReader reader = new GlacierReader(regionx, regionz);

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int z = 0; z < 512; z++) {
			for (int x = 0; x < 512; x++) {
				pw.println(reader.hasGlacierij(z, x) && (z > 0 || x > 0) ? 1 : 0);
			}
		}
		pw.close();
		System.out.println("done");
	}

}
