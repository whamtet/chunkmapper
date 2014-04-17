package com.chunkmapper.enumeration;

public enum CircleRail {
	//Labeled by quadrant
	One ((byte) 7), Two ((byte) 6), Three ((byte) 9), Four ((byte) 8);
	public final byte val;
	CircleRail(byte b) {val = b;}

//	public static void main(String[] args) {
//		CircleRail a = One, b = One;
//		System.out.println(a == b);
//	}
}
