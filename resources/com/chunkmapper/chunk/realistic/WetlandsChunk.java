package com.chunkmapper.chunk.realistic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.TreeWriter;

public class WetlandsChunk extends Chunk {
	//	public final boolean isFlat;
	//	public final boolean hasPond;
	//	private Circle[] circles;
	private static final int SWAMP_ZONE = 0, SNOWY_SWAMP_ZONE = 1, SNOW_ZONE = 2;
	private static final TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(3500, 70);
		zonator.addPoint(4500, 80);
	}
	private int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

	
	private void setMinMax(int[][] heights) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int h = heights[x][z];
				if (h > max)
					max = h;
				if (h < min)
					min = h;
			}
		}
	}
	public WetlandsChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		this.heights = heights;
		super.setBiome(Biome.SWAMP.val);
		final int soilThickness = 2;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);

		setMinMax(heights);
		//		int gradient = (max - min) * 4 / 16;
		boolean hasWater = max - min <= 1;
		boolean containsTrees = false;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int h = this.getHeights(x, z);
				if (h < 0) {
					super.writeOceanColumn(x, z);
					continue;
				}
				int zone = zonator.checkVal(h, approxLat);
				containsTrees |= zone != SNOW_ZONE;
				if (zone == SNOW_ZONE) {
					Blocks[h-2][z][x] = Block.Snow_Block.val;
					Blocks[h-1][z][x] = Block.Snow_Block.val;
				}
				if (zone == SWAMP_ZONE) {
					Blocks[h-2][z][x] = Block.Grass.val;
					Blocks[h-1][z][x] = RANDOM.nextInt(3) == 0 ? hasWater ? Block.Water.val : Block.Air.val : Block.Leaves.val;
				}
				if (zone == SNOWY_SWAMP_ZONE) {
					Blocks[h-2][z][x] = Block.Grass.val;
					if (RANDOM.nextInt(3) == 0) {
						if (RANDOM.nextInt(3) > 0)
							Blocks[h-1][z][x] = Block.Snow.val;
					} else {
						Blocks[h-1][z][x] = Block.Leaves.val;
						if (RANDOM.nextInt(3) > 0)
							Blocks[h][z][x] = Block.Snow.val;
					}
//					Blocks[h-1][z][x] = RANDOM.nextInt(3) == 0 ? Block.Ice.val : Block.Leaves.val;
//					Blocks[h][z][x] = RANDOM.nextInt(2) == 0 ? Block.Snow.val : Block.Air.val;
				}
				//				Blocks[h-1][z][x] = hasPond && hasWater(x, z) ? Block.Water.val : Block.Grass.val;
			}
		}
		if (containsTrees) {
			for (int i = 0; i < 5; i++) {
				int x = 2 + RANDOM.nextInt(12);
				int z = 2 + RANDOM.nextInt(12);
				int zone = zonator.checkVal(heights[x][z], approxLat);
				if (zone != SNOW_ZONE) {
					boolean hasSnow = zone == SNOWY_SWAMP_ZONE;
					TreeWriter.placeSwampTree(x, z, this, hasSnow);
				}
			}
		}
		
	}
	

}
