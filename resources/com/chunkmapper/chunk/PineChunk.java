package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.writer.MCWriter;

public class PineChunk extends GrassBaseChunk {
	public PineChunk(int chunkX, int chunkZ, int[][] heights, boolean hasSnow) {
		super(chunkX, chunkZ, heights);
		setBiome(Biome.TAIGA.val);
		if (hasSnow) {
			for (int x = 0; x < 16; x++) {
				for (int z = 0; z < 16; z++) {
					int h = heights[x][z];
					Blocks[h][z][x] = Block.Snow.val;
					if (RANDOM.nextInt(500) == 0) Blocks[h][z][x] = Block.Red_Flower.val;
				}
			}
		}
		for (int i = 0; i < 15; i++) {
			int x = RANDOM.nextInt(12) + 2;
			int z = RANDOM.nextInt(12) + 2;
//			TreeWriter.placePineTree(x, z, heights, hasSnow, Blocks, Data);
		}

	}
	

}
