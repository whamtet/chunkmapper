package com.chunkmapper.column;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class FloodedGrassland extends AbstractColumn {
	public static final Globcover TYPE = Globcover.FloodedGrassland;
	public final boolean hasWater;
	public byte biome = Biome.Swampland;

	public FloodedGrassland(int absx, int absz, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		hasWater = RANDOM.nextInt(2) == 0;
		super.HAS_WATER = hasWater;
	}
	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
		if (hasWater) {
			chunk.Blocks[h-1][z][x] = Block.Water.val;
		} else {
			chunk.Blocks[h-1][z][x] = Block.Grass.val;
			int i = RANDOM.nextInt(3);
			if (i > 0) {
				chunk.Blocks[h][z][x] = Block.Long_Grass.val;
				chunk.Data[h][z][x] = i == 1 ? DataSource.Fern.val : DataSource.Long_Grass.val; 
			}
		}
	}



}
