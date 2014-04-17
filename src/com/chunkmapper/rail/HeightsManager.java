package com.chunkmapper.rail;

import java.io.IOException;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.column.AbstractColumn;
import com.chunkmapper.reader.FileNotYetAvailableException;

public class HeightsManager {
	private HeightsCache cache;
	private RailTypeCache railTypeCache;
	private final int verticalExaggeration;
	private final int regionx, regionz;
	private final AbstractColumn[][] cols;
	private final Point originPoint;

	public HeightsManager(int verticalExaggeration, int regionx, int regionz, AbstractColumn[][] cols) {
		this.verticalExaggeration = verticalExaggeration;
		this.regionx = regionx;
		this.regionz = regionz;
		this.cols = cols;
		originPoint = new Point(regionx, regionz);
	}
	public int getHeight(int x, int z) throws IOException, InterruptedException, FileNotYetAvailableException, DataFormatException {
		Point neededPoint = Point.getRegionPoint(x, z);
		int h;
		if (cache != null && neededPoint.equals(cache.regionPoint)) {
			h = cache.getHeight(x, z);
		} else {
			if (cache != null)
				cache.save();
			cache = new HeightsCache(neededPoint, verticalExaggeration);
			h = cache.getHeight(x, z);
		}
		if (neededPoint.equals(originPoint)) {
			int i = z - regionz * 512;
			int j = x - regionx * 512;
			if (cols[i][j].HAS_ANIMALS) {
				h += 2;
			}
		}
		return h;
	}
	public void setHeight(int x, int z, int h) throws IOException, InterruptedException, FileNotYetAvailableException, DataFormatException {
		Point neededPoint = Point.getRegionPoint(x, z);
		if (neededPoint.equals(originPoint)) {
			int i = z - regionz * 512;
			int j = x - regionx * 512;
			if (cols[i][j].HAS_ANIMALS) {
				h -= 2;
			}
		}
		if (cache != null && neededPoint.equals(cache.regionPoint)) {
			cache.setHeight(x, z, (short) h);
		} else {
			if (cache != null)
				cache.save();
			cache = new HeightsCache(neededPoint, verticalExaggeration);
			cache.setHeight(x, z, (short) h);
		}
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
	public void setBoth(int x, int z, int h, byte b) throws IOException, InterruptedException, FileNotYetAvailableException, DataFormatException {
		setRailType(x, z, b);
		setHeight(x, z, h);
	}
	public void save() throws IOException {
		if (railTypeCache != null)
			railTypeCache.save();
		if (cache != null)
			cache.save();
		
	}
	public boolean hasRailij(int i, int j) throws IOException {
		return hasRail(regionx * 512 + j, regionz * 512 + i);
	}
	public byte getRailTypeij(int i, int j) throws IOException {
		return getRailType(regionx * 512 + j, regionz * 512 + i);
	}
	public void setRailTypeij(int i, int j, byte railType) throws IOException {
		setRailType(j + regionx + 512, i + regionz * 512, railType);
		
	}
}
