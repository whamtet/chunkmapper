package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class Test {

	public static void main(String[] args) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader("hi.txt"));
		reader.readLine();
		System.out.println(reader.readLine());
		reader.close();
	}

	/**
	 * @param args
	 */


}
