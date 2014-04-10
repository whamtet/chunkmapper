package com.chunkmapper.math;

import java.util.ArrayList;

public class Piecewise {
	
	public static class PiecewiseException extends RuntimeException {
		public PiecewiseException(String s) {
			super(s);
		}
	}

	private ArrayList<Integer> xs = new ArrayList<Integer>();
	private ArrayList<Integer> ys = new ArrayList<Integer>();
	
	public void addControlPoint(int x, int y) {

		for (int i = 0; i < xs.size(); i++) {
			if (x == xs.get(i))
				throw new PiecewiseException("Duplicate control point");
			if (x < xs.get(i)) {
				xs.add(i, x);
				ys.add(i, y);
				return;
			}
		}
		xs.add(x);
		ys.add(y);
	}
	public double interpolateDouble(double x) {
		if (xs.size() < 2)
			throw new PiecewiseException("Insufficient control points");
		if (x < xs.get(0))
			return ys.get(0);
		for (int i = 0; i < xs.size() - 1; i++) {
			if (xs.get(i) <= x && x <= xs.get(i + 1)) {
				double delx = x - xs.get(i);
				int dely = ys.get(i + 1) - ys.get(i);
				return ys.get(i) + delx*dely/(xs.get(i+1) - xs.get(i));
			}
		}
		return ys.get(ys.size()-1);
	}
	public int interpolate(int x) {
		if (xs.size() < 2)
			throw new PiecewiseException("Insufficient control points");
		if (x < xs.get(0))
			return ys.get(0);
		for (int i = 0; i < xs.size() - 1; i++) {
			if (xs.get(i) <= x && x <= xs.get(i + 1)) {
				int delx = x - xs.get(i);
				int dely = ys.get(i + 1) - ys.get(i);
				return ys.get(i) + delx*dely/(xs.get(i+1) - xs.get(i));
			}
		}
		return ys.get(ys.size()-1);
	}

}
