package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.OSMRouter;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.RenderingSection;

public class VineyardReader {
	public boolean[][] hasVineyard = new boolean[512][512];

	public VineyardReader(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		ArrayList<RenderingSection> sections = new ArrayList<RenderingSection>();
		for (Way way : o.ways) {
			if ("vineyard".equals(way.map.get("landuse")) || "grape".equals(way.map.get("crop"))) {
				if (way.points.size() > 1)
					addVineyard(way, sections);
			}
		}
		if (sections.size() > 0)
			paintSections(sections, regionx, regionz);
	}
	private void paintSections(ArrayList<RenderingSection> sections, int regionx, int regionz) {
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
					hasVineyard[z][a] = true;
				} else {
					for (int x = a; x <= b; x++) {
						hasVineyard[z][x] = true;
					}
				}
			}

		}
		
	}
	private static <E> E wrappedGet(ArrayList<E> list, int i) {
		return list.get(Matthewmatics.mod(i, list.size()));
	}
	@SuppressWarnings("unchecked")
	private static void addVineyard(Way way, ArrayList<RenderingSection> sections) {
		ArrayList<Point> points = (ArrayList<Point>) way.points.clone();
		if (points.get(0).equals(points.get(points.size() - 1)))
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
//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("motueka, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		VineyardReader reader = new VineyardReader(regionx, regionz);
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
//		for (int i = 0; i < 512; i++) {
//			for (int j = 0; j < 512; j++) {
//				pw.println(reader.hasVineyard[i][j] ? 1 : 0);
//			}
//		}
//		pw.close();
//		System.out.println("done");
//	}

}
