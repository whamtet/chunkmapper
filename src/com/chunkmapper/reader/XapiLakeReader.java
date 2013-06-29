package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Interpolator;
import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.BinaryLakeParser;
import com.chunkmapper.sections.Lake;


public class XapiLakeReader {
	private boolean[][] hasWater = new boolean[512][512];

	public boolean hasWaterij(int i, int j) {
		return hasWater[i][j];
	}


	public XapiLakeReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException {
		ArrayList<Lake> lakes = BinaryLakeParser.getRiverSections(regionx, regionz);
		for (Lake lake : lakes) {
			//first need to get points
			HashSet<Point> points = new HashSet<Point>();
			for (int i = 0; i < lake.points.size() - 1; i++) {
				Point p1 = lake.points.get(i), p2 = lake.points.get(i+1);
				Interpolator.interpolate(p1, p2, points);
			}

			boolean fill = !lake.isInner;

			int z1 = lake.bbox.y, z2 = z1 + lake.bbox.height;
			int min = regionz * 512, max = min + 511;
			if (z1 < min) {
				z1 = min;
			}
			if (z2 > max) {
				z2 = max;
			}
			for (int z = z1; z <= z2; z++) {
				boolean previousIsFill = false;
				boolean nw = false, ne = false, se = false, sw = false;
				for (int x = lake.bbox.x; x <= lake.bbox.x + lake.bbox.width; x++) {
					if (points.contains(new Point(x, z))) {//we're fill baby
						if (!previousIsFill) {
							nw
						}
					}
				}
			}

		}
	}


	/**
	 * @param args
	 */

}
