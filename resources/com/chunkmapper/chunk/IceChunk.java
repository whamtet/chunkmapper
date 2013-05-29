package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.writer.MCWriter;

public class IceChunk extends Chunk {
	public IceChunk(int x, int z, int[][] heights) {
		super(x, z);
		setBiome(Biome.ICE.val);
		this.heights = heights;
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				boolean hasSoil = RANDOM.nextInt(3) > 0;
				int h = heights[i][j];
				for (int y = 1; y < h - 3; y++) Blocks[y][j][i] = Block.Stone.val;
				Blocks[h-3][j][i] = hasSoil ? Block.Dirt.val : Block.Stone.val;
				Blocks[h-2][j][i] = Block.Snow_Block.val;
				Blocks[h-1][j][i] = Block.Snow_Block.val;
				Blocks[0][j][i] = Block.Bedrock.val;
			}
		}
	}

}
