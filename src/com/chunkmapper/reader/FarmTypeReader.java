package com.chunkmapper.reader;

import java.util.Random;

import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.FarmType;

public class FarmTypeReader {
	public static final int WIDTH = 32;
//	private byte[][] blockTypes = new byte[WIDTH][WIDTH];
	private FarmType[][] blockTypes = new FarmType[WIDTH][WIDTH];
	public FarmTypeReader(boolean includeLivestock) {
		Random random = new Random();
		FarmType[] l = includeLivestock ? FarmType.getFarmTypes() : FarmType.getVegetarianFarmTypes();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < WIDTH; j++) {
				blockTypes[i][j] = l[random.nextInt(l.length)];
			}
		}
	}
//	public byte getFarmType(int i, int j) {
//		return blockTypes[i*WIDTH/512][j*WIDTH/512];
//	}
	public FarmType getFarmType(int i, int j) {
		return blockTypes[i*WIDTH/512][j*WIDTH/512];
	}

}
