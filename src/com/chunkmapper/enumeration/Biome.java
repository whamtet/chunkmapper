package com.chunkmapper.enumeration;

public enum Biome {
	PLAINS ((byte) 1), DESERT ((byte) 2), FOREST ((byte) 4), TAIGA ((byte) 5), ICE ((byte) 12), JUNGLE ((byte) 21),
	PINE ((byte) 30), SPARSE_FOREST ((byte) 31), OCEAN ((byte) 0), SWAMP ((byte) 6);
	public final byte val;
	Biome(byte b) {val = b;}
}
