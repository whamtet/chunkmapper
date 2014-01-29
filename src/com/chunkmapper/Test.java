package com.chunkmapper;

import com.chunkmapper.reader.NameReader;

public class Test {
	public static void main(String[] args) throws Exception {
		System.out.println(NameReader.class.getResourceAsStream("/names.txt"));
	}
}
