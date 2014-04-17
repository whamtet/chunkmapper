package com.chunkmapper.rail;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import com.chunkmapper.Point;

public class RailTypeCache {
	public static final File CACHE = HeightsCache.HEIGHTS_CACHE;	
	static {
		CACHE.mkdirs();
	}

	public final Point regionPoint;
	private byte[][] data = new byte[512][512];
	private final File cacheFile;
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

	public RailTypeCache(Point p) throws IOException {
		cacheFile = new File(CACHE, "b_" + p.x + "_" + p.y);
		regionPoint = p;
		pointLock = getPoint(p);
		synchronized(pointLock) {
			if (cacheFile.exists()) {
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(cacheFile));
				for (int i = 0; i < 512; i++) {
					in.read(data[i]);
				}
				in.close();
			}
		}
	}
	public void save() throws IOException {
		synchronized(pointLock) {
			if (stale) {
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile));
				for (int i = 0; i < 512; i++) {
					out.write(data[i]);
				}
				out.close();
				stale = false;
			}
		}
	}
	public byte getRailType(int x, int z) {
		x -= regionPoint.x*512; z -= regionPoint.y*512;
		return data[z][x];
	}
	public void setRailType(int x, int z, byte b) {
		x -= regionPoint.x*512; z -= regionPoint.y*512;
		if (data[z][x] != b) {
			data[z][x] = b;
			stale = true;
		}
	}
}

