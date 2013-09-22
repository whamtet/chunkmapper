package com.chunkmapper.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class GitDelete {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(new File("git.txt")));
		String line;
		while ((line = br.readLine()) != null) {
			String file = line.split("    ")[1];
			String command = "git rm " + file;
			Runtime.getRuntime().exec(command);
		}
		br.close();
		System.out.println("done");

	}

}
