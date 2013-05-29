package com.chunkmapper.math;

public class Matthewmatics {
	public static int div(int a, int b) {
		assert b > 0;
		if (a >= 0 || a%b == 0)
			return a/b;
		else
			return a/b - 1;
	}
	public static int mod(int a, int b) {
		assert b > 0;
		int c = a%b;
		return c < 0 ? c + b: c;
	}
	//tests
	public static void main(String[] args) {
		assert mod(-1, 2) == 1;
		assert mod(-2, 2) == 0;
		assert div(-1, 2) == -1;
		assert div(-2, 2) == -1;
		assert div(-3, 2) == -2;
		assert false;
		System.out.println(-2%3);
	}

}
