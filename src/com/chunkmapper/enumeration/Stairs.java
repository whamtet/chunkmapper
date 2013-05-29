package com.chunkmapper.enumeration;

public enum Stairs {
	Ascending_East ((byte) 0), Ascending_West ((byte) 1), Ascending_South ((byte) 2), Ascending_North ((byte) 3);
	public final byte val;
	Stairs (byte b) {
		val = b;
	}

}
