package com.chunkmapper.reader;

import java.io.IOException;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.binaryparser.OSMRouter;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.column.AbstractColumn;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Way;

public class FerryReader {
	public final boolean[][] hasFerry = new boolean[512][512];
	public final boolean hasAFerry;
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
	public FerryReader(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		for (Way way : o.ways) {
			if ("ferry".equals(way.map.get("route"))) {
				for (int i = 0; i < way.points.size() - 1; i++) {
					Point p1 = way.points.get(i), p2 = way.points.get(i+1);
					interpolate(hasFerry, p1, p2, regionx, regionz);
				}
			}
		}
		boolean hasAFerry = false;
		outer: for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				if (hasFerry[i][j]) {
					hasAFerry = true;
					break outer;
				}
			}
		}
		this.hasAFerry = hasAFerry;
	}
//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("wellington, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		FerryReader reader = new FerryReader(regionx, regionz);
//		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
//		for (int i = 0; i < 512; i++) {
//			for (int j = 0; j < 512; j++) {
//				pw.println(reader.hasFerry[i][j] ? 1 : 0);
//			}
//		}
//		pw.close();
//		System.out.println("done");
//	}
	public void addLillies(Chunk chunk, int chunkx, int chunkz, AbstractColumn[][] columns) {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				AbstractColumn col = columns[i + chunkz*16][j + chunkx*16];
				if (hasFerry[i + chunkz*16][j + chunkx*16] && !col.HAS_WATER) {
					int h = chunk.getHeights(j, i);
					chunk.Blocks[h][i][j] = Blocka.Lilly;
				}
			}
		}
	}

}
