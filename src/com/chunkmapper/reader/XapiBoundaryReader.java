package com.chunkmapper.reader;

import java.awt.Rectangle;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.parser.BoundaryParser;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.sections.Boundary;
import com.chunkmapper.writer.ArtifactWriter;

public class XapiBoundaryReader {
	private boolean[][] data = new boolean[512][512];
	private final ArrayList<StringPoint> stringPoints = new ArrayList<StringPoint>();
	public final boolean hasBorder;

	private static class StringPoint {
		public final String string;
		public final Point point;
		public StringPoint(String string, Point point) {
			this.string = string;
			this.point = point;
		}
	}

	private static void interpolate(boolean[][] mask, Point p1, Point p2, int regionx, int regionz) {
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (zstride == 0) {
			if (z0 >= 0 && z0 < 512) {
				if (x0 < 0)
					x0 = 0;
				if (x2 > 512)
					x2 = 512;
				for (int x = x0; x < x2; x++) {
					mask[z0][x] = true;
				}
			}
			return;
		}
		int width = xstride >= 0 ? xstride : -xstride;
		int height = zstride >= 0 ? zstride : -zstride;
		int xstep = xstride >= 0 ? 1 : -1;
		int zstep = zstride >= 0 ? 1 : -1;

		if (width >= height) {
			int oldz = z0;
			for (int i = 0; i <= width; i++) {
				int x = x0 + i*xstep;
				int z = z0 + i * zstride / width;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = true;
				if (oldz >= 0 && x >= 0 && oldz < 512 && x < 512)
					mask[oldz][x] = true;
				oldz = z;
			}
		} else {
			int oldx = x0;
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = true;
				if (z >= 0 && oldx >= 0 && z < 512 && oldx < 512)
					mask[z][oldx] = true;
				oldx = x;
			}
		}
	}
	public void addBoundariesToChunk(int chunkx, int chunkz, Chunk chunk) {
		if (!hasBorder)
			return;
		//first add fences
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				if (data[i + chunkz * 16][j + chunkx * 16]) {
					int h = chunk.getHeights(j, i);
					for (int y = 1; y < h + 3; y++) {
						chunk.Blocks[y][i][j] = Blocka.Stone_Brick;
					}
				}
			}
		}
		Rectangle chunkRectangle = new Rectangle(chunk.x0, chunk.z0, 16, 16);
		//then add signs to inform you bastards
		for (StringPoint sp : stringPoints) {
			if (chunkRectangle.contains(sp.point)) {
				int relx = sp.point.x - chunk.x0;
				int relz = sp.point.z - chunk.z0;
				if (sp.string != null) {
					ArtifactWriter.addSign(chunk, chunk.getHeights(relx, relz), relz, relx, sp.string.split(" "));
				}
			}
		}

	}
	public XapiBoundaryReader(OverpassObject o, int regionx, int regionz) throws IOException, URISyntaxException, DataFormatException, InterruptedException {

		
		HashSet<Boundary> boundaries = BoundaryParser.getBoundaries(o, regionx, regionz);
		hasBorder = boundaries.size() > 0;

		for (Boundary boundary : boundaries) {
			if (boundary.adminLevel <= 4) {
				int limit = boundary.points.size() - 1;
				for (int i = 0; i < limit; i++) {
					Point p1 = boundary.points.get(i), p2 = boundary.points.get(i+1);
					if (p1.equals(p2))
						continue;
					interpolate(data, p1, p2, regionx, regionz);
					//need to add signs
					double delx = p2.x - p1.x, delz = p2.z - p1.z;
					//scale
					double abs = Math.sqrt(delx*delx + delz * delz);
					if (abs > 20) {
						double scale = 3 / abs;
						delx *= scale; delz *= scale;

						Point leftPoint = new Point((int) (p1.x + delz), (int) (p1.z - delx));
						stringPoints.add(new StringPoint(boundary.leftArea, leftPoint));
						Point rightPoint = new Point((int) (p1.x - delz), (int) (p1.z + delx));
						stringPoints.add(new StringPoint(boundary.rightArea, rightPoint));
					}
				}
			}
		}
	}

	public void print(File f) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				pw.println(data[i][j] ? 1 : 0);
			}
		}
		pw.close();
	}
}