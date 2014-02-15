package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
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
	private static ArrayList<Glacier> getLakes(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {

		ArrayList<Glacier> lakes = new ArrayList<Glacier>();
		for (Way way : o.ways) {
			if ("glacier".equals(way.map.get("natural"))) {
				Glacier lake = new Glacier(way.points, way.bbox);
				if (lake.isClosed()) {
					lakes.add(lake);
				}
			}
		}
		for (Relation relation : o.relations) {
			
			if ("glacier".equals(relation.map.get("natural")) && "multipolygon".equals(relation.map.get("type"))) {
				
				ArrayList<Glacier> glaciersToJoin = new ArrayList<Glacier>();
				for (Way way : relation.ways) {
					glaciersToJoin.add(new Glacier(way.points, way.bbox));
				}
				
				while (glaciersToJoin.size() > 0) {
					
					Glacier seedLake = glaciersToJoin.remove(0);
					
					boolean hasAdded = true;
					while (hasAdded) {
						hasAdded = false;
						for (int i = 0; i < glaciersToJoin.size(); i++) {
							if (seedLake.attach(glaciersToJoin.get(i))) {
								glaciersToJoin.remove(i);
								hasAdded = true;
								i--;
							}
						}
					}
					if (seedLake.isClosed() && !seedLake.isPoint()) {
						lakes.add(seedLake);
					}
				}
			}
		}		
		return lakes;
	}

	public GlacierReader(OverpassObject o, int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException, InterruptedException {


		ArrayList<Glacier> lakes = getLakes(o, regionx, regionz);

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

//	public static void main(String[] args) throws Exception {
//		System.out.println("starting");
//		double[] latlon = Nominatim.getPoint("nassihorn, switzerland");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		GlacierReader reader = new GlacierReader(regionx, regionz);
//
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
//		for (int z = 0; z < 512; z++) {
//			for (int x = 0; x < 512; x++) {
//				pw.println(reader.hasGlacierij(z, x) && (z > 0 || x > 0) ? 1 : 0);
//			}
//		}
//		pw.close();
//		System.out.println("done");
//	}

}
