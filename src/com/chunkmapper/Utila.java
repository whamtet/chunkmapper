package com.chunkmapper;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Utila {
	public static final int CHUNK_START = 4, CHUNK_END = 20;
	public static final int HEIGHTS_START = 20;
	public static final File CACHE;
	public static final int Y_SCALE = 31;
	public static final String BINARY_SUFFIX = "_.txt";
	public static final File MINECRAFT_DIR;
	static {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			MINECRAFT_DIR = new File(FileUtils.getUserDirectory(), "\\.minecraft");
		} else if (os.indexOf("mac") >= 0) {
			MINECRAFT_DIR = new File(FileUtils.getUserDirectory(), "/Library/Application Support/minecraft");
//			    			MINECRAFT_DIR = new File("wwffd");
		} else {
			//linux
			MINECRAFT_DIR = new File(FileUtils.getUserDirectory(), "/.minecraft");
		}
//		//set Cache
//		
//		if (os.indexOf("win") >= 0) {
//			CACHE = new File(FileUtils.getUserDirectory(), "\\.chunkmapper");
//		} else if (os.indexOf("mac") >= 0) {
//			CACHE = new File("/Library/Caches/Chunkmapper");
//		} else {
//			//linux
//			CACHE = new File(FileUtils.getUserDirectory(), "/.chunkmapper");
//		}
		CACHE = new File(FileUtils.getUserDirectory(), ".chunkmapper");
		CACHE.mkdirs();
	}
}
