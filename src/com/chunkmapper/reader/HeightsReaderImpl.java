package com.chunkmapper.reader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Utila;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;
import com.chunkmapper.resourceinfo.ResourceInfo;

public class HeightsReaderImpl implements HeightsReader {
	protected short[][] cache = new short[HeightsResourceInfo.LEN][HeightsResourceInfo.LEN];
	public final int min, max;
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
	
	public HeightsReaderImpl(int regionx, int regionz) throws InterruptedException, IOException, FileNotYetAvailableException {
		this(regionx, regionz, null, 1);
	}
	
	public HeightsReaderImpl(int regionx, int regionz, UberDownloader uberDownloader, int verticalExaggeration)
			throws InterruptedException, IOException, FileNotYetAvailableException {
		ResourceInfo resourceInfo = new HeightsResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(resourceInfo.file)) {
			if (uberDownloader != null)
				uberDownloader.heightsDownloader.addTask(resourceInfo.regionx, resourceInfo.regionz);
			throw new FileNotYetAvailableException();
		}

		//now we're ready to download
		DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(resourceInfo.file)));
		byte[] data = new byte[HeightsResourceInfo.FILE_LENGTH];
		in.readFully(data);
		in.close();

		ShortBuffer shortBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < HeightsResourceInfo.LEN; i++) {
			for (int j = 0; j < HeightsResourceInfo.LEN; j++) {
				short s = shortBuffer.get();
				if (s < min) min = s;
				if (s > max) max = s;
				cache[i][j] = s;
				//				cache[i][j] = shortBuffer.get();
			}
		}
		this.min = min;
		this.max = max;
		
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
