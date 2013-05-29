package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.writer.MCWriter;

//in order to be height h, top solid block at h - 1
public class PlainsChunk extends GrassBaseChunk {
	public PlainsChunk(int thisX, int thisZ, int[][] heights) {
		super(thisX, thisZ, heights);
		setBiome(Biome.PLAINS.val);
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int h = heights[x][z];
				
				if (RANDOM.nextInt(4) == 0) {
					Blocks[h][z][x] = Block.Long_Grass.val;
					Data[h][z][x] = 1;
				}
				if (RANDOM.nextInt(100) == 0) Blocks[h][z][x] = Block.Dandelion.val;
				if (RANDOM.nextInt(100) == 0) Blocks[h][z][x] = Block.Red_Flower.val;
			}
		}
	}

}
