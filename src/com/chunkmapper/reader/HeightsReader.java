package com.chunkmapper.reader;

public interface HeightsReader {
	
	public boolean isAllWater();
	
	public boolean mostlyLand();

	public int[][] getHeights(int chunkx, int chunkz);

	public short getHeightxz(int absx, int absz);

	public short getHeightij(int i, int j);

	public int[][] getAllHeights();

	public int getRealHeightij(int i, int j);

	public boolean isLandij(int i, int j);

	public boolean isWaterij(int i, int j);

}