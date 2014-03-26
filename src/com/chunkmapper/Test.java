package com.chunkmapper;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Test {
	public static void main(String[] args) throws Exception {
		
		File f = new File("test");
		FileUtils.deleteDirectory(f);
		System.out.println("done");
	}
}
