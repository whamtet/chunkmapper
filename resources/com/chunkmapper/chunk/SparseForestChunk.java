package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.writer.MCWriter;

public class SparseForestChunk extends PlainsChunk {
	
	public SparseForestChunk(int x, int z, int[][] heights) {
		super(x, z, heights);

		//now just set biomes and place some trees
		setBiome(Biome.PLAINS.val);
		for (int i = 0; i < 1; i++) {
			int a = RANDOM.nextInt(12) + 2;
			int b = RANDOM.nextInt(12) + 2;
//			TreeWriter.placeForestTree(a, b, Blocks, Data, heights, false);
		}
	}
	
}
