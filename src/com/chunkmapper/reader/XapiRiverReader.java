package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.RiverParser;
import com.chunkmapper.sections.RiverSection;

public class XapiRiverReader {
	private byte[][] mask = new byte[512][512];


	public boolean hasWaterxz(int x, int z) {
		//		return mask[z + chunkz*16][x + chunkx*16] != 0;
		return mask[com.chunkmapper.math.Matthewmatics.mod(z, 512)][com.chunkmapper.math.Matthewmatics.mod(x, 512)] != 0;
	}
	public boolean hasWaterij(int i, int j) {
		return mask[i][j] != 0;
	}
	private static void interpolate(byte[][] mask, Point p1, Point p2, int regionx, int regionz) {
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			if (z0 >= 0 && x0 >= 0 && z0 < 512 && x0 < 512)
				mask[z0][x0] = 1;
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
					mask[z][x] = 1;
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = 1;
			}
		}
	}



	public XapiRiverReader(OverpassObject o, int regionx, int regionz, HeightsReader heightsReader) throws FileNotYetAvailableException, IOException, URISyntaxException, DataFormatException, InterruptedException {

		Collection<RiverSection> riverSections = RiverParser.getRiverSections(o);

		for (RiverSection riverSection : riverSections) {
			for (int i = 0; i < riverSection.points.size() - 1; i++) {
				Point p1 = riverSection.points.get(i), p2 = riverSection.points.get(i+1);
				interpolate(mask, p1, p2, regionx, regionz);
			}
		}
		widen(mask, heightsReader);
	}

	private static void widen(byte[][] data, HeightsReader heightsReader) {
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				if (data[i][j] == 1) {
					int h = heightsReader.getRealHeightij(i, j);
					int k1 = i - 1, k2 = i + 1;
					int l1 = j - 1, l2 = j + 1;

					if (k1 < 0) k1 = 0;
					if (l1 < 0) l1 = 0;
					if (k2 > 511) k2 = 511;
					if (l2 > 511) l2 = 511;

					for (int k = k1; k <= k2; k++) {
						for (int l = l1; l <= l2; l++) {
							if (heightsReader.getRealHeightij(k, l) <= h)
								data[k][l] = 2;
						}
					}
				}
			}
		}
	}



}
