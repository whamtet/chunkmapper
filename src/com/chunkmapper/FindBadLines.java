package com.chunkmapper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FindBadLines {
	
	private static List<File> getJavaFiles() {
		List<File> files = new ArrayList<File>();
		File f = new File("/Users/matthewmolloy/workspace/chunkmapper2/src");
		getJavaFiles(files, f);
		return files;
	}
	private static void getJavaFiles(List<File> files, File f) {
		if (f.isDirectory()) {
			for (File g : f.listFiles()) {
				getJavaFiles(files, g);
			}
		}
		if (f.getName().endsWith(".java")) {
			files.add(f);
		}
	}
	public static void main(String[] args) throws Exception {
		for (File f : getJavaFiles()) {
			printBadLines(f);
		}
	}
	private static void printBadLines(File f) throws IOException {
		String[] lines = readEntireFile(f).split("\n");
		for (int i = 0; i < lines.length - 2; i++) {
			String a = lines[i], b = lines[i+1], c = lines[i+2];
			if (a.contains("catch") && !b.contains("printStackTrace") && !c.contains("printStackTrace")) {
				System.out.println(f);
				System.out.println(a + "\n" + b + "\n" + c + "\n" + "***");
			}
		}
	}
	private static String readEntireFile(File f) throws IOException {
        FileReader in = new FileReader(f);
        StringBuilder contents = new StringBuilder();
        char[] buffer = new char[4096];
        int read = 0;
        do {
            contents.append(buffer, 0, read);
            read = in.read(buffer);
        } while (read >= 0);
        in.close();
        return contents.toString();
    }
	

}
