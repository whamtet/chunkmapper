package com.chunkmapper.chunk.realistic;

import com.chunkmapper.writer.TreeWriter;

public class WoodySavannasChunk extends SavannasChunk {

	public WoodySavannasChunk(int absChunkX, int absChunkZ, int[][] heights) {

		super(absChunkX, absChunkZ, heights);

		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		if (containsTrees) {
			for (int i = 0; i < 5; i++) {
				int x = 2 + RANDOM.nextInt(12);
				int z = 2 + RANDOM.nextInt(12);
				int zone = zonator.checkVal(heights[x][z], approxLat);
				if (zone == SAVANNA_ZONE) {
//					TreeWriter.placeSavannaTree(x, z, Blocks, Data, this);
				}
			}
		}
	}

}
