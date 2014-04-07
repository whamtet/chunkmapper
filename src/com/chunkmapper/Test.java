package com.chunkmapper;

import java.io.File;
import java.util.TreeSet;

public class Test {
	public static void main(String[] args) throws Exception {
		try {
			throw new Exception();
		} catch (Exception e) {
			System.out.println(e.getCause());
		}
	}
}
