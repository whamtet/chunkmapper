package com.chunkmapper.math;

import java.awt.Point;
import java.util.ArrayList;

import com.chunkmapper.Utila;

public class TriangleZonator {
	
	private ArrayList<Point> data = new ArrayList<Point>();
	
	public void addPoint(int h, int lat) {
		//assumes points are added in order
		data.add(new Point(h / Utila.Y_SCALE, lat));
	}
	
	public int[] getCutoffs(int lat) {
		int[] out = new int[data.size()];
		for (int i = 0; i < out.length; i++) {
			Point p = data.get(i);
			out[i] = p.x - p.x * lat / p.y;
		}
		return out;
	}
	public int getFirstCutOff(int lat) {
		return getCutoffs(lat)[0];
	}
	
	public int checkVal(int h, int lat) {
		for (int out = 0; out < data.size(); out++) {
			Point p = data.get(out);
			int h2 = p.x - p.x * lat / p.y;
			if (h2 > h)
				return out;
		}
		return data.size();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TriangleZonator z = new TriangleZonator();
		z.addPoint(3000, 60);
		z.addPoint(4000, 70);
		System.out.println(z.checkVal(100, 10) + ", " + z.checkVal(500, 50) + ", " + z.checkVal(0, 71));

	}

}
