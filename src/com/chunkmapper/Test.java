package com.chunkmapper;

import java.util.ArrayList;


public class Test {

	public static void main(String[] args) throws Exception {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		arr.add(0);
		arr.add(0);
		for (int i = 0; i < arr.size(); i++) {
			arr.remove(i);
			i--;
		}
		System.out.println(arr.size());
	}

}
