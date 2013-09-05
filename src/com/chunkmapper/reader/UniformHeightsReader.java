package com.chunkmapper.reader;

import com.chunkmapper.Utila;

public class UniformHeightsReader implements HeightsReader {
	
	public boolean mostlyLand() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeights(int, int)
	 */
	@Override
	public int[][] getHeights(int chunkx, int chunkz) {
		int size = Utila.CHUNK_START + Utila.CHUNK_END;
		int[][] out = new int[size][size];
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				out[x][z] = 4;
			}
		}
		return out;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeightxz(int, int)
	 */
	@Override
	public short getHeightxz(int absx, int absz) {
		return 4;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getHeightij(int, int)
	 */
	@Override
	public short getHeightij(int i, int j) {
		return 4;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getAllHeights()
	 */
	@Override
	public int[][] getAllHeights() {
		return null;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#getRealHeightij(int, int)
	 */
	@Override
	public int getRealHeightij(int i, int j) {
		return -1;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isLandij(int, int)
	 */
	@Override
	public boolean isLandij(int i, int j) {
		return false;
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.HeightsReader#isWaterij(int, int)
	 */
	@Override
	public boolean isWaterij(int i, int j) {
		return false;
	}

}
