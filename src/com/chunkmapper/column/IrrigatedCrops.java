package com.chunkmapper.column;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class IrrigatedCrops extends AbstractColumn {
	public static final Globcover TYPE = Globcover.IrrigatedCrops;
	public final byte cropType;
	public final boolean hasWater;
	public byte biome = Biome.Plains;
	
	public IrrigatedCrops(int absx, int absz, byte cropType, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		this.cropType = cropType;
		this.hasWater = RANDOM.nextInt(4) == 0 &&
				heightsReader.getHeightxz(absx-1, absz) >= h && heightsReader.getHeightxz(absx+1, absz) >= h &&
				heightsReader.getHeightxz(absx, absz-1) >= h && heightsReader.getHeightxz(absx, absz+1) >= h;
		super.HAS_WATER = hasWater;
	}
	
}
