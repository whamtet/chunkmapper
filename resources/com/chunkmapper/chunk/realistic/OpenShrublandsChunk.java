package com.chunkmapper.chunk.realistic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.TreeWriter;

public class OpenShrublandsChunk extends Chunk {

	public static TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(4000, 70);
		zonator.addPoint(5500, 80);
	}

	private static final int SHRUBLAND_ZONE = 0, SNOWY_SHRUBLAND_ZONE = 1, SNOW_ZONE = 2;

	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		int h = chunk.getHeights(x, z);
		final int zone = zonator.checkVal(h, approxLat);
		
		if (zone == SNOW_ZONE) {
			chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
			chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
		}
		if (zone == SHRUBLAND_ZONE) {
			chunk.Blocks[h-2][z][x] = Block.Sandstone.val;
			chunk.Blocks[h-1][z][x] = Block.Sandstone.val;

			//add dead bush
			if (RANDOM.nextInt(200) == 0) {
				chunk.Blocks[h][z][x] = Block.Dead_Bush.val;
				chunk.Blocks[h-1][z][x] = Block.Sand.val;
			}
		}

		if (zone == SNOWY_SHRUBLAND_ZONE) {
			chunk.Blocks[h-2][z][x] = RANDOM.nextInt(2) == 0 ? Block.Snow_Block.val : 
				RANDOM.nextInt(3) > 0 ? Block.Stone.val : Block.Gravel.val;
				chunk.Blocks[h-1][z][x] = RANDOM.nextInt(2) == 0 ? Block.Snow_Block.val : 
				RANDOM.nextInt(3) > 0 ? Block.Stone.val : Block.Gravel.val;
			if (RANDOM.nextInt(2) == 0) {
				chunk.Blocks[h][z][x] = Block.Snow.val;						
			}
		}
		
		if (zone != SNOW_ZONE && RANDOM.nextInt(1000) == 0) {
			boolean hasSnow = zone == SNOWY_SHRUBLAND_ZONE;
			TreeWriter.placeShrub(x, z, chunk, hasSnow);
		}
	}
		
	public OpenShrublandsChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		final int soilThickness = 2;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);
		GenericWriter.setBiomes(Biomes, heights, zonator, new Biome[] {Biome.DESERT, Biome.DESERT, Biome.ICE}, approxLat);

		boolean hasBushes = RANDOM.nextInt(5) == 0;
		int bushFactor = 256 / (1 + RANDOM.nextInt(10));

		//now add the topsoil
		boolean containsTrees = false;
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int h = heights[x][z];
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
				if (zone == SHRUBLAND_ZONE) {
					Blocks[h-2][z][x] = Block.Sandstone.val;
					Blocks[h-1][z][x] = Block.Sandstone.val;

					//add dead bush
					if (hasBushes && RANDOM.nextInt(bushFactor) == 0) {
						Blocks[h][z][x] = Block.Dead_Bush.val;
						Blocks[h-1][z][x] = Block.Sand.val;
					}
				}

				if (zone == SNOWY_SHRUBLAND_ZONE) {
					Blocks[h-2][z][x] = RANDOM.nextInt(2) == 0 ? Block.Snow_Block.val : 
						RANDOM.nextInt(3) > 0 ? Block.Stone.val : Block.Gravel.val;
					Blocks[h-1][z][x] = RANDOM.nextInt(2) == 0 ? Block.Snow_Block.val : 
						RANDOM.nextInt(3) > 0 ? Block.Stone.val : Block.Gravel.val;
					if (RANDOM.nextInt(2) == 0) {
						Blocks[h][z][x] = Block.Snow.val;						
					}
				}
			}
		}
		if (containsTrees) {
			if (RANDOM.nextInt(3) == 0) {
				int x = RANDOM.nextInt(14);
				int z = RANDOM.nextInt(14);
				int zone = zonator.checkVal(heights[x][z], approxLat);
				if (zone != SNOW_ZONE) {
					boolean hasSnow = zone == SNOWY_SHRUBLAND_ZONE;
//					TreeWriter.placeShrub(x, z, Blocks, Data, heights, hasSnow);
				}
			}
		}
	}


}
