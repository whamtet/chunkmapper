package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.writer.MCWriter;

public class ForestChunk extends PlainsChunk {
	
	public ForestChunk(int x, int z, int[][] heights) {
		super(x, z, heights);

		//now just set biomes and place some trees
		setBiome(Biome.FOREST.val);
		for (int i = 0; i < 15; i++) {
			int a = RANDOM.nextInt(12) + 2;
			int b = RANDOM.nextInt(12) + 2;
//			TreeWriter.placeForestTree(a, b, Blocks, Data, heights, false);
		}
	}
	
}
