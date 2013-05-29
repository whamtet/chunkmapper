package com.chunkmapper.reader;

import java.io.IOException;

import com.chunkmapper.Utila;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;

public class HeightsReader extends Reader {
	private final int x0, z0;
	
	public HeightsReader(int regionx, int regionz)
			throws InterruptedException, IOException, FileNotYetAvailableException {
		super(new HeightsResourceInfo(regionx, regionz));
		x0 = regionx*512 - Utila.CHUNK_START;
		z0 = regionz*512 - Utila.CHUNK_START;
	}

	public int[][] getHeights(int chunkx, int chunkz) {
		int size = Utila.CHUNK_START + Utila.CHUNK_END;
		int[][] out = new int[size][size];
		int offsetx = chunkx * 16;
		int offsetz = chunkz * 16;
		for (int x = 0; x < size; x++) {
			for (int z = 0; z < size; z++) {
				int h = cache[z + offsetz][x + offsetx];
				h = h < 0 ? 4 : h / Utila.Y_SCALE + 4;
				if (h > 250)
					h = 250;
				out[x][z] = h;
			}
		}
		return out;
	}
	public short getHeightxz(int absx, int absz) {
		absx -= x0; absz -= z0;
		short h = cache[absz][absx];
		h = h < 0 ? 4 : (short) (h / Utila.Y_SCALE + 4);
		if (h > 250)
			h = 250;
		return h;
	}
	public short getHeightij(int i, int j) {
		
		//note reversed order
		short h = cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START];
		h = h < 0 ? 4 : (short) (h / Utila.Y_SCALE + 4);
		if (h > 250)
			h = 250;
		return h;
	}


//	public void setHeight(int i, int j, short h) {
//		cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START] = h;
//		
//	}

//	public int[][] getOneRing() {
//		int w = 514;
//		int[][] out = new int[w][w];
//		for (int i = Utila.CHUNK_START - 1; i < Utila.CHUNK_START + w - 1; i++) {
//			for (int j = Utila.CHUNK_START - 1; j < Utila.CHUNK_START + w - 1; j++) {
//				out[i-Utila.CHUNK_START + 1][j - Utila.CHUNK_START + 1] = cache[i][j];
//			}
//		}
//		return out;
//	}

	public int[][] getAllHeights() {
		int[][] out = new int [512][512];
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				out[i][j] = cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START];
			}
		}
		return out;
	}

}
