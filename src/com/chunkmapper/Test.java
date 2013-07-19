package com.chunkmapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.chunkmapper.protoc.FileContainer.FileInfo;


public class Test {

	public static void main(String[] args) throws Exception {
		System.out.println(getNum());
	}
	
	private static int getNum() {
		try {
			return 3;
		} finally {
			System.out.println("hi");
		}
	}

}
