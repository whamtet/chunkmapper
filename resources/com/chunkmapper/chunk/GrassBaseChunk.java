package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Block;

public abstract class GrassBaseChunk extends Chunk {
	public GrassBaseChunk(int thisX, int thisZ, int[][] heights) {
		super(thisX, thisZ);
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int h = heights[x][z];
				int soilThickness = 1 + RANDOM.nextInt(5);
				int topRock = h - soilThickness;
				if (topRock < 1) topRock = 1;
				Blocks[0][z][x] = Block.Bedrock.val;
				for (int y = 1; y < topRock; y++) Blocks[y][z][x] = Block.Stone.val;
				for (int y = topRock; y < h - 1; y++) Blocks[y][z][x] = Block.Dirt.val; 
				Blocks[h-1][z][x] = Block.Grass.val;
			}
		}
		this.heights = heights;
	}

}
