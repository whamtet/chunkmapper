package com.chunkmapper.reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.downloader.OSMDownloader;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.OSMSource;
import com.chunkmapper.parser.HighwayParser;
import com.chunkmapper.sections.HighwaySection;
import com.chunkmapper.writer.ArtifactWriter;

public class XapiHighwayReader {
	public short[][] heights = new short[512][512];
	private boolean[][] hasBridge = new boolean[512][512];
	private String[][] names = new String[512][512];

	public final boolean hasHighways;
	private static final int MIN_BUMP_SIZE = 3, MIN_RUT_SIZE = 5;
	/**
	 * @param args
	 */
	private static short[] smoothHeights(short[] heights) {

		for (int i = 1; i < heights.length; i++) {
			int a = heights[i-1], b = heights[i];
			if (a < b) {
				//look for a small rut
				for (int j = i - 2; j >= 0 && j >= i - MIN_RUT_SIZE; j--) {
					if (heights[j] == b && heights[j+1] == a) {
						//the hollow is too small and we have to remove it
						for (int k = j + 1; k < i; k++) {
							heights[k]++;
						}
						break;
					}
				}
			}
			if (a > b) {
				//look for small mound
				for (int j = i - 2; j >= 0 && j >= i - MIN_BUMP_SIZE; j--) {
					if (heights[j] == b && heights[j+1] == a) {
						//mound is too small
						for (int k = j + 1; k < i; k++) {
							heights[k]--;
						}
						break;
					}
				}

			}
		}
		return heights;
	}
	private static ArrayList<Point> interpolate(Point p1, Point p2, int regionx, int regionz) {
		ArrayList<Point> out = new ArrayList<Point>();
		int x0 = p1.x - regionx*512, z0 = p1.z - regionz*512;
		int x2 = p2.x - regionx*512, z2 = p2.z - regionz*512;

		int xstride = x2 - x0, zstride = z2 - z0;
		if (xstride == 0 && zstride == 0) {
			if (z0 >= 0 && x0 >= 0 && z0 < 512 && x0 < 512)
				out.add(new Point(x0, z0));
			return out;
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
					out.add(new Point(x, z));
			}
		} else {
			for (int i = 0; i <= height; i++) {
				int x = x0 + i * xstride / height;
				int z = z0 + i*zstep;
				if (z >= 0 && x >= 0 && z < 512 && x < 512)
					out.add(new Point(x, z));
			}
		}
		return out;
	}

	public XapiHighwayReader(int regionx, int regionz, HeightsReader heightsReader) throws IllegalArgumentException, NoSuchElementException, IOException, InterruptedException, FileNotYetAvailableException, URISyntaxException, DataFormatException {

		Collection<HighwaySection> allSections = HighwayParser.getHighwaySections(regionx, regionz);

		hasHighways = allSections.size() > 0;
		System.out.println(allSections.size());

		for (HighwaySection highwaySection : allSections) {
			boolean allowAscend = !highwaySection.hasTunnel;
			boolean allowDescend = !highwaySection.hasBridge;
			
			if (highwaySection.points.size() > 0 && highwaySection.name != null) {
				Point sectionRoot = highwaySection.points.get(0);
				int x = sectionRoot.x - regionx * 512, z = sectionRoot.z - regionz * 512;
				if (0 <= x && x < 512 && 0 <= z && z < 512) {
					names[z][x] = highwaySection.name;
				}
			}
			for (int i = 0; i < highwaySection.points.size() - 1; i++) {
				Point p1 = highwaySection.points.get(i), p2 = highwaySection.points.get(i+1);
				ArrayList<Point> interpolatedPoints = interpolate(p1, p2, regionx, regionz);
				
				if (interpolatedPoints.size() == 0)
					continue;
				short[] smoothedHeights = new short[interpolatedPoints.size()];
				Point initialPoint = interpolatedPoints.get(0);
				short currHeight = heightsReader.getHeightij(initialPoint.z, initialPoint.x);
				
				for (int j = 0; j < interpolatedPoints.size(); j++) {
					Point p = interpolatedPoints.get(j);
					short targetHeight = heightsReader.getHeightij(p.z, p.x);
					if (targetHeight > currHeight && allowAscend)
						currHeight++;
					if (targetHeight < currHeight && allowDescend)
						currHeight--;
					smoothedHeights[j] = currHeight;
				}
				smoothHeights(smoothedHeights);
				//now we actually set the heights
				for (int j = 0; j < interpolatedPoints.size(); j++) {
					Point p = interpolatedPoints.get(j);
					heights[p.z][p.x] = smoothedHeights[j];
					if (highwaySection.hasBridge) {
						hasBridge[p.z][p.x] = true;
					}
				}
			}
		}
	}
	public boolean addRoad(int chunkx, int chunkz, Chunk chunk) {
		boolean chunkHasRoad = false;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int x = j + chunkx * 16;
				int z = i + chunkz * 16;
				
				int h = heights[z][x];
				if (h != 0) {
					chunkHasRoad = true;
					int x1 = j - 1, x2 = j + 1;
					int z1 = i - 1, z2 = i + 1;
					if (z1 < 0) z1 = 0;
					if (x1 < 0) x1 = 0;
					if (z2 > 15) z2 = 15;
					if (x2 > 15) x2 = 15;
					
					if (hasBridge[z][x]) {
						//throw in some wooden planks
						for (int zd = z1; zd <= z2; zd++) {
							for (int xd = x1; xd <= x2; xd++) {
								chunk.Blocks[h-1][zd][xd] = Blocka.Planks;
								chunk.Blocks[h][zd][xd] = 0;
								chunk.Blocks[h+1][zd][xd] = 0;
								chunk.Blocks[h+2][zd][xd] = 0;
								chunk.Blocks[h+3][zd][xd] = 0;
								chunk.Blocks[h+4][zd][xd] = 0;
							}
						}
					} else {
						//put in some roading
						for (int zd = z1; zd <= z2; zd++) {
							for (int xd = x1; xd <= x2; xd++) {
								chunk.Blocks[h-3][zd][xd] = Blocka.Cobblestone;
								chunk.Blocks[h-2][zd][xd] = Blocka.Cobblestone;
								chunk.Blocks[h-1][zd][xd] = Blocka.Block_Of_Quartz;
								chunk.Blocks[h][zd][xd] = 0;
								chunk.Blocks[h+1][zd][xd] = 0;
								chunk.Blocks[h+2][zd][xd] = 0;
								chunk.Blocks[h+3][zd][xd] = 0;
								chunk.Blocks[h+4][zd][xd] = 0;
							}
						}
					}
					//add sign
					if (names[z][x] != null) {
						ArtifactWriter.addSign(chunk, h, i, j, names[z][x].split(" "));
					}
				}
			}
		}
		return chunkHasRoad;
	}
//	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("new plymouth, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512)+1;
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		
//		HeightsReader hr = new HeightsReaderImpl(regionx, regionz);
//		XapiHighwayReader reader = new XapiHighwayReader(regionx, regionz, hr);
//		
//		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("/Users/matthewmolloy/python/wms/data.csv")));
//		for (int i = 0; i < 512; i++) {
//			for (int j = 0; j < 512; j++) {
//				pw.println(reader.heights[i][j]);
//			}
//		}
//		pw.close();
//		System.out.println("done");
//	}
}
