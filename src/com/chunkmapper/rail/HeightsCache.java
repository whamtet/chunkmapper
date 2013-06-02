package com.chunkmapper.rail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chunkmapper.Point;
import com.chunkmapper.Utila;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.reader.HeightsReader;


public class HeightsCache {
	public static final File HEIGHTS_CACHE = new File(Utila.CACHE, "railHeights");
	static {
		if (!HEIGHTS_CACHE.exists())
			HEIGHTS_CACHE.mkdirs();
	}
	public final Point regionPoint;
	private short[][] data;
	private final File heightsCacheFile;
	private boolean stale = false;
	public static void deleteCache() {
		if (HEIGHTS_CACHE.exists()) {
			for (File f : HEIGHTS_CACHE.listFiles()) {
				f.delete();
			}
		}
	}
	public HeightsCache(Point p, UberDownloader uberDownloader) throws IOException, InterruptedException, FileNotYetAvailableException {
		heightsCacheFile = new File(HEIGHTS_CACHE, "f_" + p.x + "_" + p.y);
		regionPoint = p;

		if (heightsCacheFile.exists()) {
			DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(heightsCacheFile)));
			data = new short[512][512];
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					data[i][j] = in.readShort();
				}
			}
			in.close();
		} else {
			
			HeightsReader reader = new HeightsReader(p.x, p.y, uberDownloader);
			data = new short[512][512];
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					data[i][j] = reader.getHeightij(i, j);
					if (data[i][j] < 4)
						data[i][j] = 4;
				}
			}
		}
	}
	public void save() throws IOException {
		//write cache to heightsCacheFile;
		if (stale) {
			DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(heightsCacheFile)));
			for (int i = 0; i < 512; i++) {
				for (int j = 0; j < 512; j++) {
					out.writeShort(data[i][j]);
				}
			}
			out.close();
			stale = false;
		}
	}
	public short getHeight(int x, int z) {
		x -= regionPoint.x*512; z -= regionPoint.y*512;
		return data[z][x];
	}
	public void setHeight(int x, int z, short h) {
		x -= regionPoint.x*512; z -= regionPoint.y*512;
		if (data[z][x] != h) {
			data[z][x] = h;
			stale = true;
		}
	}
}
