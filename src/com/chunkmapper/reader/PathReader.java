package com.chunkmapper.reader;

import java.io.IOException;
import java.io.PrintWriter;

import com.chunkmapper.Point;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.parser.OverpassParser;

public class PathReader {
	public final boolean[][] hasPath = new boolean[512][512];
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
	public PathReader(int regionx, int regionz) throws IOException {
		OverpassObject o = OverpassParser.getObject(regionx, regionz);
		for (Way way : o.ways) {
			String highway = way.map.get("highway");
			String foot = way.map.get("foot");
			if ("path".equals(highway) || ("track".equals(highway) && "yes".equals(foot))) {
				for (int i = 0; i < way.points.size() - 1; i++) {
					Point p1 = way.points.get(i), p2 = way.points.get(i+1);
					interpolate(hasPath, p1, p2, regionx, regionz);
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("cobb dam, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		PathReader reader = new PathReader(regionx, regionz);
		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				pw.println(reader.hasPath[i][j] ? 1 : 0);
			}
		}
		pw.close();
		System.out.println("done");
	}
	public void addPath(Chunk chunk, int chunkx, int chunkz) {
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (hasPath[i + chunkz*16][j + chunkx*16]) {
					int h = chunk.getHeights(j, i);
					chunk.Blocks[h-1][i][j] = Blocka.Gravel;
					chunk.Blocks[h][i][j] = 0;
					chunk.Blocks[h+1][i][j] = 0;
					chunk.Blocks[h+2][i][j] = 0;
				}
			}
		}
	}

}
