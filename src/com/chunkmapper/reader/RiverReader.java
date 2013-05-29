package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.chunkmapper.Utila;
import com.chunkmapper.resourceinfo.RiverResourceInfo;

public class RiverReader {
	private byte[][] mask;// = new byte[512][512];
//	private int chunkx, chunkz;
	//	private static final Random RANDOM = new Random();

//	public void setChunk(int chunkx, int chunkz) {
//		this.chunkx = chunkx;
//		this.chunkz = chunkz;
//	}

	public boolean hasWaterxz(int x, int z) {
//		return mask[z + chunkz*16][x + chunkx*16] != 0;
		return mask[com.chunkmapper.math.Matthewmatics.mod(z, 512)][com.chunkmapper.math.Matthewmatics.mod(x, 512)] != 0;
	}
	public boolean hasWaterij(int i, int j) {
		return mask[i][j] != 0;
	}
	private static class Point {
		public int i, j;
		public ArrayList<Point> neighbours = new ArrayList<Point>(8);
		public Point(int i, int j) {
			this.i = i;
			this.j = j;		
		}
	}
	private static byte[][] pointsToMask(Point[][] points, int[][] heights) {
		byte[][] mask = new byte[512][512];
		//need to initialize explicitly
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				mask[i][j] = 0;
			}
		}
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				Point origin = points[i][j];
				if (origin != null) {
					mask[i][j] = 1;
					for (Point neighbour : origin.neighbours) {
						int x0 = origin.i, y0 = origin.j;
						int xn = neighbour.i, yn = neighbour.j;
						int h0 = heights[x0][y0] / Utila.Y_SCALE, hn = heights[xn][yn] / Utila.Y_SCALE;
						int h = h0 > hn ? h0 : hn;

						int dx, dy, incrE, incrNE, d, y, flag = 0;
						if (xn<x0) {
							//swapd(&x0,&xn);swapd(&y0,&yn);
							int temp = x0;
							x0 = xn; xn = temp;
							temp = y0; y0 = yn; yn = temp;
						}
						if (yn<y0) {
							y0 = -y0; yn = -yn;
							flag = 10;
						}
						dy = yn-y0; dx = xn-x0;

						if (dy <= 1 && dx <= 1)
							continue;

						if (dx<dy) {
							//swapd(&x0,&y0);
							int temp = x0;
							x0 = y0; y0 = temp;
							//swapd(&xn,&yn);
							temp = xn; xn = yn; yn = temp;
							//swapd(&dy,&dx);
							temp = dy; dy = dx; dx = temp;
							flag++;
						}
						y = y0; d = 2*dy-dx;
						incrE = 2*dy; incrNE = 2*(dy-dx);
						for (int x = x0; x <= xn; x++) {
							if (flag == 0 && heights[x][y] / Utila.Y_SCALE <= h)
								mask[x][y] = 1;
							if (flag == 1 && heights[y][x] / Utila.Y_SCALE <= h)
								mask[y][x] = 1;
							if (flag == 10 && heights[x][-y] / Utila.Y_SCALE <= h)
								mask[x][-y] = 1;
							if (flag == 11 && heights[y][-x] / Utila.Y_SCALE <= h)
								mask[y][-x] = 1;
							if (d<=0) {
								d += incrE;
							} else {
								y++; d += incrNE;
							}
						}

					}
				}
			}
		}
		return mask;
	}
	private static Point[][] dataToPoints(byte[] data, ArrayList<Byte> blueIndices) {
		//assumes mask is already set
		Point[][] out = new Point[512][512];
		int c = 0;
		
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					byte p = data[j + i * 512];
					if (blueIndices.contains(p)) {
						//make fat mask
						int k1 = i - 1;
						int k2 = i + 1;
						int l1 = j - 1;
						int l2 = j + 1;

						if (k1 < 0) k1 = 0;
						if (l1 < 0) l1 = 0;
						if (k2 > 511) k2 = 511;
						if (l2 > 511) l2 = 511;
						for (int k = k1; k <= k2; k++) {
							for (int l = l1; l <= l2; l++) {
								if (out[k][l] == null) {
									c++;
									out[k][l] = new Point(k, l);
								}
							}
						}
					}
				}
			}
		
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				Point o = out[i][j];
				if (o != null) {
					int k1 = i;
					int k2 = i;
					int l1 = j;
					int l2 = j;

					if (k1 < 0) k1 = 0;
					if (l1 < 0) l1 = 0;
					if (k2 > 511) k2 = 511;
					if (l2 > 511) l2 = 511;

					for (int k = k1; k <= k2; k++) {
						for (int l = l1; l <= l2; l++) {
							Point p = out[k][l];
							if (p != null) {
								o.neighbours.add(p);
							}
						}
					}
				}
			}
		}
		return out;
	}


	public RiverReader(int regionx, int regionz, HeightsReader heightsReader) throws FileNotYetAvailableException {
		RiverResourceInfo info = new RiverResourceInfo(regionx, regionz);
		if (!info.file.exists())
			throw new FileNotYetAvailableException();
		//lets just assume that the water info become available earlier
		try {
			
			BufferedImage image = ImageIO.read(info.file);

			byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
			ArrayList<Byte> blueIndices = Reader.getBlueIndices(info.file);

			Point[][] points = dataToPoints(data, blueIndices);
			int[][] heights = heightsReader.getAllHeights();

			int scaleDown = 2;
			for (int iter = 0; iter < 20; iter++) {
				for (int i = 0; i < 512; i++) {
					for (int j = 0; j < 512; j++) {
						Point p = points[i][j];
						if (p != null) {
							//downhill search
							//try moving it downhill
							int id = i, jd = j, hd = heights[i][j] / scaleDown;

							int k1 = i - 1;
							int k2 = i + 1;
							int l1 = j - 1;
							int l2 = j + 1;

							if (k1 < 0) k1 = 0;
							if (l1 < 0) l1 = 0;
							if (k2 > 511) k2 = 511;
							if (l2 > 511) l2 = 511;

							for (int k = k1; k <= k2; k++) {
								for (int l = l1; l <= l2; l++) {
									int hdd = heights[k][l] / scaleDown;
									if (hdd < hd && points[k][l] == null) {
										hd = hdd;
										id = k; jd = l;
									}
								}
							}
							//shift points
							if (id != i || jd != j) {
								points[i][j] = null;
								points[id][jd] = p;
								p.i = id; p.j = jd;
							}
						}
					}
				}
			}
			//now initialize the mask
			mask = pointsToMask(points, heights);

			//now we can safely fatten
			//			for (int i = 0; i < 512; i++) {
			//				for (int j = 0; j < 512; j++) {
			//					if (mask[i][j] == 1) {
			//						int h = heights[i][j] / Utila.Y_SCALE;
			//						int k1 = i - 1;
			//						int k2 = i + 1;
			//						int l1 = j - 1;
			//						int l2 = j + 1;
			//
			//						if (k1 < 0) k1 = 0;
			//						if (l1 < 0) l1 = 0;
			//						if (k2 > 511) k2 = 511;
			//						if (l2 > 511) l2 = 511;
			//
			//						for (int k = k1; k <= k2; k++) {
			//							for (int l = l1; l <= l2; l++) {
			//								if (heights[k][l] / Utila.Y_SCALE <= h)
			//									mask[k][l] = 2;
			//							}
			//						}
			//					}
			//				}
			//			}

		} catch (IOException e) {
			e.printStackTrace();
//			info.file.delete();
//			ParallelWriter.riverDownloader.addFile(info);
			throw new FileNotYetAvailableException();
		}
	}


	/**
	 * @param args
	 */

}
