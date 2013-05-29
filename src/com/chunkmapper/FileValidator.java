package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileValidator {
	public static void setValid(File f) throws IOException {
		if (f.getName().endsWith("~"))
			return;
		File metaFile = new File(f.getAbsolutePath() + "~");
		FileWriter writer = new FileWriter(metaFile);
		writer.write(f.length() + "");
		writer.close();
	}
	public static boolean checkValid(File f) throws IOException {
		File metaFile = new File(f.getAbsolutePath() + "~");
		if (!f.exists() || !metaFile.exists())
			return false;
		
		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		boolean isValid = Long.parseLong(reader.readLine()) == f.length();
		reader.close();
		return isValid;
	}
//	public static void clearUp(File f) {
//		if (f.getName().startsWith("~"))
//			f.delete();
//	}
//	public static void main(String[] args) throws Exception {
//		File f = new File("/Library/Caches/Chunkmapper");
//		for (File g : f.listFiles()) {
//			if (!g.getName().startsWith(".")) {
//				System.out.println(g);
//				for (File h : g.listFiles()) {
//					if (h.getName().endsWith("~"))
//						continue;
//					if (!checkValid(h)) {
//						System.out.println(h);
//					}
//				}
//			}
//		}
//	}

}
