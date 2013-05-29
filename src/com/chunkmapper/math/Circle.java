package com.chunkmapper.math;

import java.util.Random;

public class Circle {
	
	private static Random RANDOM = new Random();
	
	private double x, y, r;

	public Circle(double x, double y, double r) {
		this.x = x;
		this.y = y;
		this.r = r;
	}
	public boolean contains(int x, int y) {
		return java.lang.Math.sqrt((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y)) <= r; 
	}
//	public double r(int x, int y) {
//		return java.lang.Math.sqrt((x - this.x)*(x - this.x) + (y - this.y)*(y - this.y));
//	}
	
//	private static double triangleProb() {
//		//returns a triangular distributed value
//		double x = RANDOM.nextDouble();
//		double y = RANDOM.nextDouble();
//		return x <= y ? x : triangleProb();
//	}
	public static Circle getCircle() {
		double maxRad = 3.75;
		double rad = RANDOM.nextDouble() * maxRad;
		double x = rad + RANDOM.nextDouble() * (15 - 2 * rad);
		double y = rad + RANDOM.nextDouble() * (15 - 2 * rad);
		return new Circle(x, y, rad);
	}

}
