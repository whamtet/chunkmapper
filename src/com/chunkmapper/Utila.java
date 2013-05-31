package com.chunkmapper;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class Utila {
	public static final File SAVES_FOLDER = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves");
	public static final int CHUNK_START = 4, CHUNK_END = 20;
	public static final File CACHE;// = new File("/Library/Caches/Chunkmapper");
	public static final int Y_SCALE = 31;
	static {
		//set Cache
		String os = System.getProperty("os.name").toLowerCase();
		if (os.indexOf("win") >= 0) {
			CACHE = new File(FileUtils.getUserDirectory(), "\\.chunkmapper");
		} else if (os.indexOf("mac") >= 0) {
			CACHE = new File("/Library/Caches/Chunkmapper");
		} else {
			//linux
			CACHE = new File(FileUtils.getUserDirectory(), "/.chunkmapper");
		}
		CACHE.mkdirs();
	}
}
