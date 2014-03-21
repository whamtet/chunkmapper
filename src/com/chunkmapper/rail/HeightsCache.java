package com.chunkmapper.rail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.HeightsReaderS3;


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
	private static final Object masterLock = new Object();
	private static final HashMap<Point, Point> lockMap = new HashMap<Point, Point>();
	private final Object pointLock;

	private static Point getPoint(Point p) {
		synchronized(masterLock) {
			if (lockMap.containsKey(p)) {
				return lockMap.get(p);
			} else {
				lockMap.put(p, p);
				return p;
			}
		}
	}

	public static void deleteCache() {
		if (HEIGHTS_CACHE.exists()) {
			for (File f : HEIGHTS_CACHE.listFiles()) {
				f.delete();
			}
		}
	}
	public HeightsCache(Point p, int verticalExaggeration) throws IOException, InterruptedException, FileNotYetAvailableException, DataFormatException {

		heightsCacheFile = new File(HEIGHTS_CACHE, "f_" + p.x + "_" + p.y + Utila.BINARY_SUFFIX);
		regionPoint = p;
		pointLock = getPoint(p);

		synchronized(pointLock) {
			if (heightsCacheFile.exists()) {
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(heightsCacheFile)));
				data = new short[512][512];
				for (int i = 0; i < 512; i++) {
					for (int j = 0; j < 512; j++) {
						data[i][j] = in.readShort();
					}
				}
				in.close();
				return;
			} else {
				HeightsReader reader = new HeightsReaderS3(p.x, p.y, verticalExaggeration);
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
	}
	public void save() throws IOException {
		//write cache to heightsCacheFile;
		if (stale) {
			synchronized(pointLock) {
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(heightsCacheFile)));
				for (int i = 0; i < 512; i++) {
					for (int j = 0; j < 512; j++) {
						out.writeShort(data[i][j]);
					}
				}
				out.close();
			}
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
