package com.chunkmapper.enumeration;

public enum StraightRail {
	
	EastUp ((byte) 2), WestDown((byte) 2),
	EastDown ((byte) 3), WestUp ((byte) 3),
	NorthUp ((byte) 4), SouthDown ((byte) 4),
	NorthDown ((byte) 5), SouthUp ((byte) 5),
	North ((byte) 0), South ((byte) 0),
	East ((byte) 1), West ((byte) 1);
	public final byte val;
	StraightRail(byte b) {val = b;}

}
