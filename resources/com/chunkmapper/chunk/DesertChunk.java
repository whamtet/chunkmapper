package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.writer.MCWriter;

public class DesertChunk extends Chunk {
	public DesertChunk(int x, int z, int[][] heights) {
		super(x, z);
		setBiome(Biome.DESERT.val);
		this.heights = heights;
		
		boolean hasBushes = RANDOM.nextInt(5) == 0;
		int bushFactor = 256 / (1 + RANDOM.nextInt(10));
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int h = heights[i][j];
				byte colType = RANDOM.nextInt(2) == 0 ? Block.Sand.val : Block.Sandstone.val;
				//place bedrock
				Blocks[0][j][i] = Block.Bedrock.val;
				//set column to sand or sandstone
				for (int y = 1; y < h; y++) {
					Blocks[y][j][i] = colType;
				}
				//add dead bush
				if (hasBushes && RANDOM.nextInt(bushFactor) == 0) {
					Blocks[h][j][i] = Block.Dead_Bush.val;
					Blocks[h-1][j][i] = Block.Sand.val;
				}
				//add cactus
				if (RANDOM.nextInt(5000) == 0) {
					int cactusHeight = 2 + RANDOM.nextInt(3);
					for (int y = h; y < h + cactusHeight; y++) Blocks[y][j][i] = Block.Cactus.val;
				}
			}
		}
	}

}
