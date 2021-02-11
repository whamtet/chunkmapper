package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Relation;
import com.chunkmapper.parser.OverpassObject.Way;
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
	private static HashSet<Lake> getLakes(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {

		HashSet<Lake> lakes = new HashSet<Lake>();
		for (Way way : o.ways) {
			if ("water".equals(way.map.get("natural"))) {
				Lake lake = new Lake(way.points, way.bbox);
				if (lake.isClosed()) {
					lakes.add(lake);
				}
			}
		}
		for (Relation relation : o.relations) {
			if ("water".equals(relation.map.get("natural")) && "multipolygon".equals(relation.map.get("type"))) {
				ArrayList<Lake> lakesToJoin = new ArrayList<Lake>();
				for (Way way : relation.ways) {
					lakesToJoin.add(new Lake(way.points, way.bbox));
				}
				while (lakesToJoin.size() > 0) {
					Lake seedLake = lakesToJoin.remove(0);
					
					boolean hasAdded = true;
					while (hasAdded) {
						hasAdded = false;
						for (int i = 0; i < lakesToJoin.size(); i++) {
							if (seedLake.attach(lakesToJoin.get(i))) {
								lakesToJoin.remove(i);
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

	public XapiLakeReader(OverpassObject o, int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException, InterruptedException {


		HashSet<Lake> lakes = getLakes(o, regionx, regionz);

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



}
