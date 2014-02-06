package com.chunkmapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.io.FileUtils;

public class Utila {
	public static final int CHUNK_START = 4, CHUNK_END = 20;
	public static final int HEIGHTS_START = 20;
	public static final File CACHE;
	public static final int Y_SCALE = 31;
	public static final String BINARY_SUFFIX = "_.txt";
	public static File MINECRAFT_DIR;
	private static final File customDirStore;

	private static String slurp(File f) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String s = br.readLine();
			br.close();
			return s;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void spit(File f) {
		try {
			PrintWriter pw = new PrintWriter(new FileWriter(customDirStore));
			pw.println(f.getAbsolutePath());
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	static {
		CACHE = new File(FileUtils.getUserDirectory(), ".chunkmapper");
		CACHE.mkdirs();
		customDirStore = new File(CACHE, "customDir.txt");
		
		if (customDirStore.exists()) {
			MINECRAFT_DIR = new File(slurp(customDirStore));
		} else {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.indexOf("win") >= 0) {
				File appData = new File(System.getenv("APPDATA"));
				MINECRAFT_DIR = new File(appData, ".minecraft");
			} else if (os.indexOf("mac") >= 0) {
				MINECRAFT_DIR = new File(FileUtils.getUserDirectory(), "/Library/Application Support/minecraft");
			} else {
				MINECRAFT_DIR = new File(FileUtils.getUserDirectory(), "/.minecraft");
			}
		}
	}
}
