package com.chunkmapper.chunk.realistic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.math.Piecewise;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;

public class BarrenChunk extends Chunk {
	private static final TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(4000, 70);
		zonator.addPoint(5500, 80);
	}
	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int h = chunk.getHeights(x, z);
		byte blockType = RANDOM.nextInt(3) > 0 ? Block.Sand.val : Block.Sandstone.val;
		chunk.Blocks[h-2][z][x] = blockType;
		chunk.Blocks[h-1][z][x] = blockType;
	}
		
	public BarrenChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		setBiome(Biome.DESERT.val);
		this.heights = heights;
		final int soilThickness = 5;
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		int[] criticalHeights = zonator.getCutoffs(approxLat);
		Piecewise snowDensity = new Piecewise();
		snowDensity.addControlPoint(criticalHeights[0], 0);
		snowDensity.addControlPoint(criticalHeights[1], 1);
		
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int h = this.getHeights(i, j);
				if (h < 0) {
					super.writeOceanColumn(i, j);
					continue;
				}
				byte blockType = RANDOM.nextInt(3) > 0 ? Block.Sand.val : Block.Sandstone.val;
				int topBedrock = h - soilThickness;
				if (topBedrock < 1)
					topBedrock = 1;
				for (int y = topBedrock; y < h; y++) {
					Blocks[y][j][i] = blockType;
				}
				if (RANDOM.nextDouble() <= snowDensity.interpolateDouble(h))
					Blocks[h][j][i] = Block.Snow.val;
			}
		}
	}

}
