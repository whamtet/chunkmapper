package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Stack;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.parser.CoastlineParser;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.sections.Coastline;

public class XapiCoastlineReader {
	private int[][] data = new int[512][512];
	private static final int BAND_WIDTH = 5;
	private static final int COAST = BAND_WIDTH + 2, DOWN_COAST = -BAND_WIDTH - 2, UP_COAST = BAND_WIDTH + 3;
	private static final int FINAL_COAST = BAND_WIDTH + 4;
	private static final int FORESHORE = BAND_WIDTH + 5;

	public boolean isCoastij(int i, int j) {
		return data[i][j] == FINAL_COAST;
	}
	public boolean isForeshoreij(int i, int j) {
		return data[i][j] == FORESHORE;
	}

	public boolean hasWaterij(int i, int j) {
		return data[i][j] < 0 && data[i][j] != DOWN_COAST;
	}
	public int getValueij(int i, int j) {
		return data[i][j];
	}
	private static void interpolate(int[][] mask, Point p1, Point p2, int regionx, int regionz, int fill, int endFill) {
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			if (z0 >= 0 && x0 >= 0 && z0 < 512 && x0 < 512)
				mask[z0][x0] = endFill;
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
					mask[z][x] = i == 0 || i == width ? endFill : fill;
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					mask[z][x] = i == 0 || i == height ? endFill : fill;
			}
		}
	}
	private static void floodFill(int[][] data, int i1, int j1, int fill, int target) {
		// data == hasLand
		int h = data.length;
		int w = data[0].length;

		Stack<Integer> is = new Stack<Integer>();
		Stack<Integer> js = new Stack<Integer>();
		is.add(i1);
		js.add(j1);

		while (is.size() > 0) {
			int i = is.pop();
			int j = js.pop();

			int jd;
			for (jd = j; jd < w && data[i][jd] == target; jd++) {
				data[i][jd] = fill;
				if (i > 0 && data[i-1][jd] == target) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == target) {
					is.add(i+1);
					js.add(jd);
				}
			}
			for (jd = j - 1; jd >= 0 && data[i][jd] == target; jd--) {
				data[i][jd] = fill;
				if (i > 0 && data[i-1][jd] == target) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == target) {
					is.add(i+1);
					js.add(jd);
				}
			}
		}
	}
	public XapiCoastlineReader(OverpassObject o2, int regionx, int regionz, GlobcoverReader reader) throws IOException, URISyntaxException, DataFormatException, InterruptedException {

		Collection<Coastline> coastlines = CoastlineParser.getCoastlines(o2, regionx, regionz);
		//1062, 237
		if (coastlines.size() == 0 || regionx == 1062 && regionz == 237) {
			int fill = reader.mostlyLand() ? 1 : -1;
			if (regionx == 1062 && regionz == 237)
				fill = 1;
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					data[i][j] = fill;
				}
			}
			return;
		}
		for (Coastline coastline : coastlines) {
			int limit = coastline.points.size() - 1;
			for (int i = 0; i < limit; i++) {
				Point p1 = coastline.points.get(i), p2 = coastline.points.get(i+1);
				int fill;
				if (p1.z == p2.z) {
					fill = COAST;
				} else {
					fill = p1.z > p2.z ? UP_COAST : DOWN_COAST;
				}
				interpolate(data, p1, p2, regionx, regionz, fill, COAST);
			}
		}
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int val = data[i][j];
				if (val != COAST && val != UP_COAST && val != DOWN_COAST && -BAND_WIDTH < val && val < BAND_WIDTH) {
					boolean mustFill = false, isWater = false;
					if (j < 511 && (data[i][j+1] == UP_COAST || data[i][j+1] == DOWN_COAST)) {
						isWater = data[i][j+1] == DOWN_COAST;
						mustFill = true;
					}
					if (j > 0 && (data[i][j-1] == UP_COAST || data[i][j-1] == DOWN_COAST)) {
						isWater = data[i][j-1] == UP_COAST;
						mustFill = true;
					}
					if (mustFill) {
						int target = val;
						int fill = isWater ? target - 1 : target + 1;
						floodFill(data, i, j, fill, target);
					}
				}
			}
		}
		//go through and widen the coasts
		int RAD = 1;
		for (int i = 0 ; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int val = data[i][j];
				if (val == COAST || val == UP_COAST || val == DOWN_COAST) {
					int k = i - RAD, l = i + RAD;
					int m = j - RAD, n = j + RAD;
					if (k < 0) k = 0;
					if (m < 0) m = 0;
					if (l > 511) l = 511;
					if (n > 511) n = 511;
					for (int o = k; o <= l; o++) {
						for (int p = m; p <= n; p++) {
							data[o][p] = FINAL_COAST;
						}
					}
				}
			}
		}
		//add foreshore
		for (int i = 0 ; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int val = data[i][j];
				if (val == FINAL_COAST) {
					int k = i - RAD, l = i + RAD;
					int m = j - RAD, n = j + RAD;
					if (k < 0) k = 0;
					if (m < 0) m = 0;
					if (l > 511) l = 511;
					if (n > 511) n = 511;
					for (int o = k; o <= l; o++) {
						for (int p = m; p <= n; p++) {
							if (data[o][p] < 0) {
								data[o][p] = FORESHORE;
							}
						}
					}
				}
			}
		}
	}


	public void print(File f) throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				//				pw.println(reader.getValueij(i, j));
				pw.println(this.hasWaterij(i, j) || i == 0 && j == 0 ? 0 : 1);
			}
		}
		pw.close();
	}

//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("new plymouth, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		System.out.println(regionx + ", " + regionz);
//		GlobcoverReader globcoverReader = new GlobcoverReaderImpl2(regionx, regionz);
//		XapiCoastlineReader reader = new XapiCoastlineReader(regionx, regionz, globcoverReader);
//		reader.print(new File("/Users/matthewmolloy/python/wms/data.csv"));
//		System.out.println("done");
//	}
}