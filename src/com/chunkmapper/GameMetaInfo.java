package com.chunkmapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.Utila;

public class GameMetaInfo {
	public final File store;
	public final Point rootPoint;
	private int numChunksMade;
	public final static String STORE_NAME = "meta.txt";
	public final int verticalExaggeration;
	public final boolean isNew;

	public int getNumChunksMade() {
		return numChunksMade;
	}

	public GameMetaInfo(File gameFolder, double lat, double lon, int verticalExaggeration) throws IOException {
		(new File(gameFolder, "chunkmapper")).mkdirs();
		store = new File(gameFolder, "chunkmapper/meta.txt");
		isNew = !store.exists();
		if (store.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(store));
			int x = Integer.parseInt(reader.readLine());
			int z = Integer.parseInt(reader.readLine());
			numChunksMade = Integer.parseInt(reader.readLine());

			String verticalExaggerationString = reader.readLine();
			if (verticalExaggerationString == null) {
				this.verticalExaggeration = 1;
			} else {
				this.verticalExaggeration = Integer.parseInt(verticalExaggerationString);
			}

			rootPoint = new Point(x, z);
			reader.close();
		} else {
			this.verticalExaggeration = verticalExaggeration;
			int x = (int) Math.floor(lon * 3600 / 512);
			int z = (int) Math.floor(-lat * 3600 / 512);
			rootPoint = new Point(x, z);
			save();
		}
	}
	private void save() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(store)));
			pw.println(rootPoint.x);
			pw.println(rootPoint.z);
			pw.println(numChunksMade);
			pw.println(verticalExaggeration);
			pw.close();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	//numChunksmade is the only mutable field, so its okay to lock on the whole object
	public synchronized void incrementChunksMade() {
		numChunksMade++;
		save();
	}

	public static GameMetaInfo getFromName(String name) throws IOException {
		File gameFolder = new File(Utila.MINECRAFT_DIR, name);
		return new GameMetaInfo(gameFolder, 0, 0, 1);
	}

}
