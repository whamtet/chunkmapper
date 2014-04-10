package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Piecewise;
import com.chunkmapper.reader.HeightsReader;

public class NoData extends AbstractColumn {
	public static final Globcover TYPE = Globcover.NoData;	
	public final byte surfaceType;
	private static final Piecewise surfaceTemp = new Piecewise();
	static {
		surfaceTemp.addControlPoint(0, 30);
		surfaceTemp.addControlPoint(30, 40);
		surfaceTemp.addControlPoint(90, -40);
	}

	public NoData(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		double lat = absz / 3600.;
		if (lat < 0) lat = -lat;
		double temp = surfaceTemp.interpolateDouble(lat) - h * 30 * 6e-3;
		if (temp > 15) {
			surfaceType = RANDOM.nextInt(2) == 0 ?
					Block.Sand.val : Block.Sandstone.val;
			biome = Biome.Desert;
		} else if (temp > 0) {
			surfaceType = Blocka.Gravel;
			biome = Biome.MushroomIsland;
		} else {
			surfaceType = Blocka.Snow_Block;
			biome = Biome.IcePlains;
		}
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-2][z][x] = surfaceType;
		chunk.Blocks[h-1][z][x] = surfaceType;

	}

}
