package com.chunkmapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GameMetaInfo {
	public final File store;
	public final Point rootPoint;
	private int numChunksMade;
	public final static String STORE_NAME = "meta.txt";
	
	public int getNumChunksMade() {
		return numChunksMade;
	}
	
	public GameMetaInfo(File store) throws IOException {
		this.store = store;
		BufferedReader reader = new BufferedReader(new FileReader(store));
		int x = Integer.parseInt(reader.readLine());
		int z = Integer.parseInt(reader.readLine());
		numChunksMade = Integer.parseInt(reader.readLine());
		rootPoint = new Point(x, z);
		reader.close();
	}
	public GameMetaInfo(File store, double lat, double lon) {
		this.store = store;
		int x = (int) Math.floor(lon * 3600 / 512);
		int z = (int) Math.floor(-lat * 3600 / 512);
		rootPoint = new Point(x, z);
		save();
	}
	private void save() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(store)));
			pw.println(rootPoint.x);
			pw.println(rootPoint.z);
			pw.println(numChunksMade);
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public synchronized void incrementChunksMade() {
		numChunksMade++;
		save();
	}

}
