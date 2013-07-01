package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import com.chunkmapper.binaryparser.BinaryLakeParser;
import com.chunkmapper.sections.EndPointer;
import com.chunkmapper.sections.Lake;


public class XapiLakeReader {
	private boolean[][] hasWater = new boolean[512][512];

	public boolean hasWaterij(int i, int j) {
		return hasWater[i][j];
	}


	public XapiLakeReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException, URISyntaxException, DataFormatException {
		ArrayList<Lake> lakes = BinaryLakeParser.getLakes(regionx, regionz);
		for (Lake lake : lakes) {
			//first need to get points
			EndPointer endPointer = new EndPointer(lake.points);
			System.out.println(lake.points.size());

			boolean fill = !lake.isInner;

			int z1 = lake.bbox.y, z2 = z1 + lake.bbox.height;
			int minz = regionz * 512, maxz = minz + 511;
			if (z1 < minz) {
				z1 = minz;
			}
			if (z2 > maxz) {
				z2 = maxz;
			}
			int minx = regionx * 512;
			for (int z = z1; z <= z2; z++) {
				boolean applyFill = false;
				boolean previousIsBarrier = false;
				for (int x = lake.bbox.x; x <= lake.bbox.x + lake.bbox.width; x++) {
					int xd = x - minx, zd = z - minz;
					if (endPointer.isEndPoint(x, z)) {
//					if (false) {
						if (0 <= xd && xd <= 511) {
							hasWater[zd][xd] = fill;
						}
					} else {
						if (endPointer.isPoint(x, z)) {
							if (!previousIsBarrier) {
								applyFill = !applyFill;
								previousIsBarrier = true;
							}
							if (0 <= xd && xd <= 511) {
								hasWater[zd][xd] = fill;
							}
						} else {
							previousIsBarrier = false;
							if (applyFill) {
								if (0 <= xd && xd <= 511) {
									hasWater[zd][xd] = fill;
								}
							}
						}
					}
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		double[] latlon = geocode.core.placeToCoords("queenstown, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		XapiLakeReader reader = new XapiLakeReader(regionx, regionz);
		
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("/Users/matthewmolloy/python/wms/data.csv"))));
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				pw.println(reader.hasWaterij(z, x) ? 1 : 0);
			}
		}
		pw.close();
		System.out.println("done");
	}

}
