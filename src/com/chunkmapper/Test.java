package com.chunkmapper;

import java.util.HashSet;

public class Test {
	public static void main(String[] args) {
		HashSet<Integer> ints = new HashSet<Integer>();
		ints.add(1);
		ints.add(2);
		ints.add(3);
		
		for (int i : ints) {
			ints.remove(3);
		}

	}
}
