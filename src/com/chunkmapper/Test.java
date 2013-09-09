package com.chunkmapper;

import java.util.ArrayList;

public class Test {
	public static void main(String[] args) {
		ArrayList<Integer> a1 = new ArrayList<Integer>(), a2;
		a1.add(1);
		a1.add(2);
		a2 = (ArrayList<Integer>) a1.clone();
		a1.remove(1);
		System.out.println(a2);
		System.out.println(a1);
	}

}
