package com.chunkmapper;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;


public class Test {

	public static void main(String[] args) throws Exception {
		String t = "ok";
		doNothing(t);
		System.out.println(t);
	}
	private static void doNothing(String s) {
		s = "hi";
	}

}
