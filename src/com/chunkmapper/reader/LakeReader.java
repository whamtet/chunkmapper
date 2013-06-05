package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.resourceinfo.LakeResourceInfo;


public class LakeReader {
	private short[][] lakeMask = new short[512][512];

	//	public short waterHeight(int x, int z) {
	////		return lakeMask[z + chunkz*16][x + chunkx*16];
	//		return lakeMask[com.chunkmapper.math.Math.mod(z, 512)][com.chunkmapper.math.Math.mod(x, 512)];
	//	}
	public boolean hasWaterij(int i, int j, int h) {
		return lakeMask[i][j] != 0 && h <= lakeMask[i][j];
	}
	//	public boolean hasWater(int x, int z) {
	//		return lakeMask[z + chunkz*16][x + chunkx*16] != 0;
	//	}

	private void dumpAndAbort() {
		System.out.println("too many lakes: aborting");
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				pw.println(lakeMask[i][j]);
			}
		}
		pw.close();
		System.exit(0);
	}

	public LakeReader(int regionx, int regionz, HeightsReader heightsReader) throws IOException, FileNotYetAvailableException {
		LakeResourceInfo info = new LakeResourceInfo(regionx, regionz);
		//lets just assume that the water info become available earlier
			BufferedImage image = ImageIO.read(info.file);
			if (!FileValidator.checkValid(info.file))
				throw new FileNotYetAvailableException();

			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			ArrayList<Byte> blueIndices = Reader.getBlueIndices(info.file);
			//lakes are designated by 1.  So lets number 
			boolean hasLakes = false;
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					byte p = data[j + i * 512];
					if (blueIndices.contains(p)) {
						//					if (p != 0) {
						lakeMask[i][j] = 1;
						hasLakes = true;
					}
				}
			}
			//			PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
			//			for (int i = 0 ; i < 512; i++) {
			//				for (int j = 0; j < 512; j++) {
			//					pw.println(lakeMask[i][j]);
			//				}
			//			}
			//			pw.close();
			if (!hasLakes)
				return;

			//The work that follows removes the lake mask at points above the median height for each lake.
			//We do this because the lake position data is less accurate than the height data
			//and it often causes the lake to go uphill near the edges.

			//first we partition the lakes
			int numLakes = 0;
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					if (lakeMask[i][j] == 1) {
						if (numLakes == Byte.MAX_VALUE)
							dumpAndAbort();
						numLakes++;
						floodFill(lakeMask, (byte) (numLakes + 1), i, j);

					}
				}
			}
			//then we get the median height for each lake
			MedianCalculator[] calcs = new MedianCalculator[numLakes];
			for (int i = 0; i < numLakes; i++) {
				calcs[i] = new MedianCalculator();
			}

			//no need to cache heights as most of the time they won't be called.
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					short l = lakeMask[i][j];
					if (l != 0) {
						//						calcs[l-2].add(heightsReader.getHeightij(i, j));

						if (i == 0 || j == 0 || i == 511 || j == 511) {
							calcs[l-2].add(heightsReader.getHeightij(i, j));
						} else {
							//only add it if its a boundary block
							int m1 = i - 1;
							int m2 = i + 1;
							int n1 = j - 1;
							int n2 = j + 1;

							outer: for (int m = m1; m <= m2; m++) {
								for (int n = n1; n <= n2; n++) {
									if (lakeMask[m][n] == 0) {
										//this is a boundary block, add it and quit
										calcs[l-2].add(heightsReader.getHeightij(i, j));
										break outer;
									}
								}
							}
						}
					}
				}
			}
			short[] maxHeights = new short[numLakes];
			//			System.out.println("***");
			for (int i = 0; i < numLakes; i++) {
				maxHeights[i] = (short) calcs[i].getMedian();
				//								System.out.println(maxHeights[i]);
				//								System.out.println(calcs[i].numPoints);
			}
			//			System.out.println("***");
			//now we go through one last time setting lake heights

			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					short l = lakeMask[i][j];
					if (l != 0) {
						lakeMask[i][j] = maxHeights[l-2];
					}
				}
			}


		
	}
	private static class MedianCalculator {
		private HashMap<Integer, Integer> data = new HashMap<Integer, Integer>();
		public int numPoints = 0;
		public void add(int i) {
			numPoints++;
			if (data.containsKey(i)) {
				data.put(i, data.get(i) + 1);
			} else {
				data.put(i, 1);
			}
		}
		public int getMedian() {
			int highestCount = 0;
			int median = Integer.MIN_VALUE;
			for (int possibleMedian : data.keySet()) {
				int currentCount = data.get(possibleMedian);
				if (currentCount > highestCount) {
					highestCount = currentCount;
					median = possibleMedian;
				}
			}
			return median;
		}
	}
	private static void floodFill(short[][] data, byte replacementColor, int i1, int j1) {
		int h = data.length;
		int w = data[0].length;
		byte targetColor = 1;
		Stack<Integer> is = new Stack<Integer>();
		Stack<Integer> js = new Stack<Integer>();
		is.add(i1);
		js.add(j1);
		while (is.size() > 0) {
			int i = is.pop();
			int j = js.pop();
			//			data[i][j] = replacementColor;
			//go to right
			int jd;
			for (jd = j; jd < w && data[i][jd] == targetColor; jd++) {
				data[i][jd] = replacementColor;
				if (i > 0 && data[i-1][jd] == targetColor) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == targetColor) {
					is.add(i+1);
					js.add(jd);
				}
			}
			//we're doing 8-checking
			//			jd++;
			if (jd < w) {
				if (i > 0 && data[i-1][jd] == targetColor) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == targetColor) {
					is.add(i+1);
					js.add(jd);
				}
			}
			for (jd = j - 1; jd >= 0 && data[i][jd] == targetColor; jd--) {
				data[i][jd] = replacementColor;
				if (i > 0 && data[i-1][jd] == targetColor) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == targetColor) {
					is.add(i+1);
					js.add(jd);
				}
			}
			//			jd--;
			if (jd >= 0) {
				if (i > 0 && data[i-1][jd] == targetColor) {
					is.add(i-1);
					js.add(jd);
				}
				if (i < h-1 && data[i+1][jd] == targetColor) {
					is.add(i+1);
					js.add(jd);
				}
			}
		}
	}


	/**
	 * @param args
	 */

}
