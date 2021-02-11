package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.sections.RenderingSection;

public class OrchardReader {
	public boolean[][] hasOrchard = new boolean[512][512];
	public final boolean hasAnOrchard;

	public OrchardReader(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		ArrayList<RenderingSection> sections = new ArrayList<RenderingSection>();
		for (Way way : o.ways) {
			if ("orchard".equals(way.map.get("landuse")) || "orchard".equals(way.map.get("agriculture"))) {
				if (way.points.size() > 1)
					addVineyard(way, sections);
			}
		}
		if (sections.size() > 0)
			paintSections(sections, regionx, regionz);
		hasAnOrchard = sections.size() > 0;
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
					hasOrchard[z][a] = true;
				} else {
					for (int x = a; x <= b; x++) {
						hasOrchard[z][x] = true;
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

}
