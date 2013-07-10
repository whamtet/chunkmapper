package com.chunkmapper.reader;

import java.io.IOException;

import com.chunkmapper.Utila;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;

public class HeightsReader extends Reader {
	private final int x0, z0, verticalExaggeration;
	
	public HeightsReader(int regionx, int regionz, UberDownloader uberDownloader, int verticalExaggeration)
			throws InterruptedException, IOException, FileNotYetAvailableException {
		super(new HeightsResourceInfo(regionx, regionz), uberDownloader);
		this.verticalExaggeration = verticalExaggeration;
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
				h = h < 0 ? 4 : h * verticalExaggeration / Utila.Y_SCALE + 4;
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
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		if (h > 250)
			h = 250;
		return h;
	}
	public short getHeightij(int i, int j) {
		
		//note reversed order
		short h = cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START];
		h = h < 0 ? 4 : (short) (h * verticalExaggeration / Utila.Y_SCALE + 4);
		if (h > 250)
			h = 250;
		return h;
	}

	public int[][] getAllHeights() {
		int[][] out = new int [512][512];
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				out[i][j] = cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START];
			}
		}
		return out;
	}

	public int getRealHeightij(int i, int j) {
		return cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START];
	}
	public boolean isLandij(int i, int j) {
		return cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START] >= 0;
	}
	public boolean isWaterij(int i, int j) {
		return cache[i + Utila.CHUNK_START][j + Utila.CHUNK_START] < 1;
	}

}
