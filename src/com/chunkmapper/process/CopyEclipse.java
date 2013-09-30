package com.chunkmapper.process;

import java.io.File;

public class CopyEclipse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 3; i < 6; i++) {
			File f = new File("/Users/matthewmolloy/Downloads/eclipse " + i);
			System.out.println(f.exists());
		}

	}

}
