package com.chunkmapper.chunk;

import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.writer.MCWriter;

public class JungleChunk extends GrassBaseChunk {
	public JungleChunk(int chunkX, int chunkZ, int[][] heights) {
		super(chunkX, chunkZ, heights);
		setBiome(Biome.JUNGLE.val);
		//place leaves and ferns on ground
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int leafHeight = RANDOM.nextInt(3);
				int h = heights[x][z];
				for (int y = h; y < h + leafHeight; y++) {
					Blocks[y][z][x] = Block.Leaves.val;
					Data[y][z][x] = DataSource.Jungle.val;
				}
				if (leafHeight == 0) {//place some ferns
					Blocks[h+leafHeight][z][x] = Block.Long_Grass.val;
					Data[h+leafHeight][z][x] = DataSource.Fern.val;
				}
			}
		}
		//add the jungle trees
		for (int i = 0; i < 20; i++) {
			int rad = 3;
			int x = rad + RANDOM.nextInt(16 - 2 * rad);
			int z = rad + RANDOM.nextInt(16 - 2 * rad);
//			TreeWriter.placeJungleTree(x, z, heights, Blocks, Data, true);
		}
	}
	

}
