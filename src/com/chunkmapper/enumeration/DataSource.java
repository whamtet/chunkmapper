package com.chunkmapper.enumeration;

public enum DataSource {
	Spruce ((byte) 1), Jungle ((byte) 3), Fern ((byte) 2), Oak ((byte) 0), Birch ((byte) 2), Long_Grass ((byte) 1);
	public final byte val;
	DataSource(byte b) {val = b;}

}
