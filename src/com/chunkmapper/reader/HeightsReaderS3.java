package com.chunkmapper.reader;

import java.io.IOException;
import java.util.zip.DataFormatException;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.heights.HGTFile;
import com.chunkmapper.protoc.admin.HeightsInfo;

public class HeightsReaderS3 implements HeightsReader {
	private static final int LEN = 512 + 2 * Utila.HEIGHTS_START;
	protected short[][] data = new short[LEN][LEN];
//	public final int min, max;
	private final int x0, z0, verticalExaggeration;
	public final boolean allWater;
	
	public boolean isAllWater() {
		return allWater;
	}

	public boolean mostlyLand() {
		int sumHeight = 0;
		for (int i = 0; i < LEN; i++) {
			for (int j = 0; j < LEN; j++) {
				sumHeight += data[i][j];
			}
		}
		return sumHeight > 0;
	}

	public HeightsReaderS3(int regionx, int regionz, int verticalExaggeration) throws IOException, InterruptedException, DataFormatException {

		double buffer = Utila.HEIGHTS_START / 3600.;
		double lon1 = regionx * 512. / 3600 - buffer, lon2 = lon1 + 512. / 3600 + 2 * buffer;
		double lat2 = -regionz * 512. / 3600 + buffer, lat1 = lat2 - 512. / 3600 - 2 * buffer;

		//this part gets height data from possibly more than one degree square
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
		double rooti = (lati2 + 1 - lat2) * 1200;
		double rootj = (lon1 - loni1) * 1200;
		int imax = height * 1200 - 1, jmax = width * 1200 - 1;
		
		for (int i = 0; i < LEN; i++) {
			for (int j = 0; j < LEN; j++) {
				
				double id = rooti + i/3., jd = rootj + j/3.;
				int i1 = (int) Math.floor(id), i2 = (int) Math.ceil(id);
				int j1 = (int) Math.floor(jd), j2 = (int) Math.ceil(jd);
				double ifr = id - i1, jfr = jd - j1;
				if (i2 > imax) i2 = imax;
				if (j2 > jmax) j2 = jmax;
				
				double h1 = tempCache[i1][j1] * (1-jfr) + tempCache[i1][j2] * jfr;
				double h2 = tempCache[i2][j1] * (1-jfr) + tempCache[i2][j2] * jfr;
				data[i][j] = (short) (h1 * (1-ifr) + h2*ifr);
			}
		}
		


		this.verticalExaggeration = verticalExaggeration;
		x0 = regionx*512 - Utila.HEIGHTS_START;
		z0 = regionz*512 - Utila.HEIGHTS_START;

		boolean isWater = true;
		for (int i = Utila.HEIGHTS_START; i < 512 + Utila.HEIGHTS_START; i++) {
			for (int j = Utila.HEIGHTS_START; j < 512 + Utila.HEIGHTS_START; j++) {
				if (data[i][j] >= 0) {
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
				int h = data[z + offsetz][x + offsetx];
				h = h < 0 ? 4 : h * verticalExaggeration / Utila.Y_SCALE + 4;
				//temp
				//				h -= 128;
				//				if (h < 4) h = 4;
				//end temp
				if (h > 250)
					h = 250;
				if (verticalExaggeration > 1 && h > 215) h = 215;
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
		short h = data[absz][absx];
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		//temp
		//		h -= 128;
		//		if (h < 4) h = 4;
		//end temp
		if (h > 250)
			h = 250;
		if (verticalExaggeration > 1 && h > 215) h = 215;
		return h;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeightij(int, int)
	 */
	@Override
	public short getHeightij(int i, int j) {

		//note reversed order
		short h = data[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		//temp
		//		h -= 128;
		//		if (h < 4) h = 4;
		//end temp
		if (h > 250)
			h = 250;
		if (verticalExaggeration > 1 && h > 215) h = 215;
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
				out[i][j] = data[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
			}
		}
		return out;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getRealHeightij(int, int)
	 */
	@Override
	public int getRealHeightij(int i, int j) {
		return data[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START];
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isLandij(int, int)
	 */
	@Override
	public boolean isLandij(int i, int j) {
		return data[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START] >= 0;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isWaterij(int, int)
	 */
	@Override
	public boolean isWaterij(int i, int j) {
		return data[i + Utila.HEIGHTS_START][j + Utila.HEIGHTS_START] < 1;
	}

}
