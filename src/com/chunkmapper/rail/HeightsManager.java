package com.chunkmapper.rail;

import java.io.IOException;

import com.chunkmapper.Point;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class HeightsManager {
	private HeightsCache cache;
	private RailTypeCache railTypeCache;
	private final int verticalExaggeration;

	private int numReloads = 0;

	public HeightsManager(int verticalExaggeration) {
		this.verticalExaggeration = verticalExaggeration;
	}
	public short getHeight(int x, int z) throws IOException, InterruptedException, FileNotYetAvailableException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (cache != null && neededPoint.equals(cache.regionPoint)) {
			return cache.getHeight(x, z);
		} else {
			if (cache != null)
				cache.save();
			numReloads++;
			cache = new HeightsCache(neededPoint, verticalExaggeration);
			return cache.getHeight(x, z);
		}
	}
	public void setHeight(int x, int z, short h) throws IOException, InterruptedException, FileNotYetAvailableException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (cache != null && neededPoint.equals(cache.regionPoint)) {
			cache.setHeight(x, z, h);
		} else {
			if (cache != null)
				cache.save();
			cache = new HeightsCache(neededPoint, verticalExaggeration);
			cache.setHeight(x, z, h);
		}
	}
	public void save() throws IOException {
		if (cache != null)
			cache.save();
		if (railTypeCache != null)
			railTypeCache.save();
	}
	public boolean hasRail(int x, int z) throws IOException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (railTypeCache != null && neededPoint.equals(railTypeCache.regionPoint)) {
			return railTypeCache.getRailType(x, z) != 0;
		} else {
			if (railTypeCache != null)
				railTypeCache.save();
			railTypeCache = new RailTypeCache(neededPoint);
			return railTypeCache.getRailType(x, z) != 0;
		}
	}
	public byte getRailType(int x, int z) throws IOException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (railTypeCache != null && neededPoint.equals(railTypeCache.regionPoint)) {
			return (byte) (railTypeCache.getRailType(x, z) - 1);
		} else {
			if (railTypeCache != null)
				railTypeCache.save();
			railTypeCache = new RailTypeCache(neededPoint);
			return (byte) (railTypeCache.getRailType(x, z) - 1);
		}
	}
	public void setRailType(int x, int z, byte b) throws IOException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (railTypeCache != null && neededPoint.equals(railTypeCache.regionPoint)) {
			railTypeCache.setRailType(x, z, (byte) (b + 1));
		} else {
			if (railTypeCache != null)
				railTypeCache.save();
			railTypeCache = new RailTypeCache(neededPoint);
			railTypeCache.setRailType(x, z, (byte) (b + 1));
		}
	}
	public void setBoth(int x, int z, short h, byte b) throws IOException, InterruptedException, FileNotYetAvailableException {
		setRailType(x, z, b);
		setHeight(x, z, h);
	}
}
