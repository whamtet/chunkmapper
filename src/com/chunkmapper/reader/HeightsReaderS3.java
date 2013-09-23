package com.chunkmapper.reader;

import java.io.IOException;
import java.io.PrintWriter;

import com.chunkmapper.Utila;
import com.chunkmapper.heights.HGTFile;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.protoc.admin.HeightsInfo;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;

public class HeightsReaderS3 implements HeightsReader {
	private static final int LEN = 512 + 2 * Utila.HEIGHTS_START;
	protected short[][] cache = new short[LEN][LEN];
//	public final int min, max;
	private final int x0, z0, verticalExaggeration;
	public final boolean allWater;
	
	public boolean isAllWater() {
		return allWater;
	}

	public boolean mostlyLand() {
		int sumHeight = 0;
		for (int i = 0; i < HeightsResourceInfo.LEN; i++) {
			for (int j = 0; j < HeightsResourceInfo.LEN; j++) {
				sumHeight += cache[i][j];
			}
		}
		return sumHeight > 0;
	}
	
	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("Brisbane");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		HeightsReader reader = new HeightsReaderS3(regionx, regionz, 1);
		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				pw.println(reader.getRealHeightij(i, j));
			}
		}
		pw.close();
		System.out.println("done");
	}


	public HeightsReaderS3(int regionx, int regionz, int verticalExaggeration) throws IOException, InterruptedException {

		double buffer = Utila.HEIGHTS_START / 3600.;
		double lon1 = regionx * 512. / 3600 - buffer, lon2 = lon1 + 512. / 3600 + 2 * buffer;
		double lat2 = -regionz * 512. / 3600 + buffer, lat1 = lat2 - 512. / 3600 - 2 * buffer;

		int loni1 = (int) Math.floor(lon1), loni2 = (int) Math.floor(lon2);
		int lati1 = (int) Math.floor(lat1), lati2 = (int) Math.floor(lat2);
		int width = loni2 - loni1 + 1, height = lati2 - lati1 + 1;
		short[][] tempCache = new short[height*1200][width*1200];
		//init to minus one
		for (int i = 0; i < height*1200; i++) {
			for (int j = 0; j < width * 1200; j++) {
				tempCache[i][j] = -1;
			}
		}
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (HeightsInfo.hasPoint(lati2 - i, loni1 + j)) {
					short[][] tempTempCache = HGTFile.getHeights(lati2 - i, loni1 + j);
					for (int k = 0; k < 1200; k++) {
						for (int l = 0; l < 1200; l++) {
							tempCache[i*1200 + k][j * 1200 + l] = tempTempCache[k][l];
						}
					}
				}
			}
		}
		int rooti = (int) ((lati2 + 1 - lat2) * 1200);
		int rootj = (int) ((lon1 - loni1) * 1200);
		for (int i = 0; i < LEN; i++) {
			for (int j = 0; j < LEN; j++) {
				cache[i][j] = tempCache[rooti + i/3][rootj + j/3];
			}
		}
		
//		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
//		for (int i = 0; i < height * 1200; i++) {
//			for (int j = 0; j < width * 1200; j++) {
//				pw.println(tempCache[i][j]);
//			}
//		}
//		pw.close();
//		System.out.println("done");
//		System.exit(0);
		


		this.verticalExaggeration = verticalExaggeration;
		x0 = regionx*512 - Utila.HEIGHTS_START;
		z0 = regionz*512 - Utila.HEIGHTS_START;

		boolean isWater = true;
		for (int i = Utila.HEIGHTS_START; i < 512 + Utila.HEIGHTS_START; i++) {
			for (int j = Utila.HEIGHTS_START; j < 512 + Utila.HEIGHTS_START; j++) {
				if (cache[i][j] >= 0) {
					isWater = false;
				}
			}
		}
		allWater = isWater;
	}


	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeights(int, int)
	 */
	@Override
	public int[][] getHeights(int chunkx, int chunkz) {
		int size = Utila.CHUNK_START + Utila.CHUNK_END;
		int[][] out = new int[size][size];
		int offsetx = chunkx * 16 + Utila.HEIGHTS_START - Utila.CHUNK_START;
		int offsetz = chunkz * 16 + Utila.HEIGHTS_START - Utila.CHUNK_START;
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				int h = cache[z + offsetz][x + offsetx];
				h = h < 0 ? 4 : h * verticalExaggeration / Utila.Y_SCALE + 4;
				//temp
				//				h -= 128;
				//				if (h < 4) h = 4;
				//end temp
				if (h > 250)
					h = 250;
				out[x][z] = h;
			}
		}
		return out;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeightxz(int, int)
	 */
	@Override
	public short getHeightxz(int absx, int absz) {
		absx -= x0; absz -= z0;
		short h = cache[absz][absx];
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		//temp
		//		h -= 128;
		//		if (h < 4) h = 4;
		//end temp
		if (h > 250)
			h = 250;
		return h;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeightij(int, int)
	 */
	@Override
	public short getHeightij(int i, int j) {

		//note reversed order
		short h = cache[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		//temp
		//		h -= 128;
		//		if (h < 4) h = 4;
		//end temp
		if (h > 250)
			h = 250;
		return h;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getAllHeights()
	 */
	@Override
	public int[][] getAllHeights() {
		int[][] out = new int [512][512];
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				out[i][j] = cache[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
			}
		}
		return out;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getRealHeightij(int, int)
	 */
	@Override
	public int getRealHeightij(int i, int j) {
		return cache[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isLandij(int, int)
	 */
	@Override
	public boolean isLandij(int i, int j) {
		return cache[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START] >= 0;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isWaterij(int, int)
	 */
	@Override
	public boolean isWaterij(int i, int j) {
		return cache[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START] < 1;
	}

}
