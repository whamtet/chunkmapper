package com.chunkmapper.math;

import java.io.PrintWriter;

public class StaticSobol {
	private static final int GRID_SIZE = 1000;
	private static final int[][] data = new int[GRID_SIZE][GRID_SIZE];
	private static final int DIRECTION_SIZE = 18;
	private static final double[] ps = new double[DIRECTION_SIZE], qs = new double[DIRECTION_SIZE];

	static {

		ps[0] = 0.5; ps[1] = 0.75; ps[2] = 0.875;
		qs[0] = 0.5; qs[1] = 0.75; qs[2] = 0.375;
		for (int i = 3; i < DIRECTION_SIZE; i++) {
			ps[i] = doubleXor(ps[i-2], doubleXor(ps[i-3], ps[i-3] / 8));
			qs[i] = doubleXor(qs[i-1], doubleXor(qs[i-3], qs[i-3] / 8));
		}
		double x = 0, y = 0;
		for (int i = 1; i < 100000; i++) {
			int zeroPos = zeroPos(i);
			x = doubleXor(x, ps[zeroPos]);
			y = doubleXor(y, qs[zeroPos]);
			data[(int) (x * GRID_SIZE)][(int) (y * GRID_SIZE)] = i;
		}
	}
	public static boolean hasObject(int absx, int absz, int spacing) {
		absx = Matthewmatics.mod(absx, GRID_SIZE);
		absz = Matthewmatics.mod(absz, GRID_SIZE);
		int minVal = GRID_SIZE * GRID_SIZE / spacing / spacing;
		return data[absz][absx] != 0 && data[absz][absx] <= minVal;
	}
	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if (StaticSobol.hasObject(j, i, 10))
					pw.println(1);
				else
					pw.println(0);
			}
		}
		pw.close();
		System.out.println("done");
	}
	private static int zeroPos(int i) {
		for (int j = 0; j < 100; j++) {
			if ((i >> j) % 2 == 0) {
				return j;
			}
		}
		return -1;
	}
	private static double doubleXor(double a, double b) {
		int i = 0;
		while ((int) a != a || (int) b != b) {
			i++;
			a *= 2;
			b *= 2;
		}
		//	System.out.println(a + ", " + b);
		double c = (int) a ^ (int) b;
		//	System.out.println(c);
		return c / (1 << i);
	}
}
