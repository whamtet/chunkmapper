package com.chunkmapper.enumeration;

public enum WoolColor {
	White ((byte) 0), Orange ((byte) 1), Magenta ((byte) 2), Light_Blue ((byte) 3), Yellow ((byte) 4), Lime ((byte) 5), Pink ((byte) 6), Gray ((byte) 7), Light_Gray ((byte) 8), Cyan ((byte) 9), Purple ((byte) 10), Blue ((byte) 11), Brown ((byte) 12), Green ((byte) 13), Red ((byte) 14), Black ((byte) 15);
	public final byte val;
	WoolColor(byte b) {
		val = b;
	}
}
