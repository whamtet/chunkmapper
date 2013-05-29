package com.chunkmapper.chunk.realistic;

import com.chunkmapper.Util;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.math.Sobol;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.TreeWriter;

public class EvergreenNeedleleafChunk extends Chunk {

	private static final Sobol sobol = new Sobol(30, new TreeGenerator() {
		public byte getTreeHeight() {
			return 0;
//			return (byte) (4 + RANDOM.nextInt(4));
		}
	});

	public static TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(4000, 70);
		zonator.addPoint(4500, 80);
	}

	private static final int PINE_ZONE = 0, TAIGA_ZONE = 1, SNOW_ZONE = 2;
	
	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		int h = chunk.getHeights(x, z);
		final int zone = zonator.checkVal(h, approxLat);
		
		if (zone == SNOW_ZONE) {
			chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
			chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
		} else {
			chunk.Blocks[h-2][z][x] = Block.Dirt.val;
			chunk.Blocks[h-1][z][x] = Block.Grass.val;
		}
		if (zone != PINE_ZONE) {
			chunk.Blocks[h][z][x] = Block.Snow.val;
		} else {
			chunk.Blocks[h][z][x] = Block.Long_Grass.val;
			chunk.Data[h][z][x] = RANDOM.nextInt(2) == 0 ? (byte) 1 : DataSource.Fern.val;
		}
		int absX = x + absChunkX * 16;
		int absZ = z + absChunkZ * 16;
		byte treeHeight = sobol.getTreeHeight(absX, absZ);
		if (treeHeight != 0) {
			if (zone != SNOW_ZONE) {
				boolean hasSnow = zone == TAIGA_ZONE;
				TreeWriter.placePineTree(x, z, chunk, hasSnow, treeHeight);
			}
		}
	}


	public EvergreenNeedleleafChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		this.heights = heights;
		final int soilThickness = 2;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);
		GenericWriter.setBiomes(Biomes, heights, zonator, new Biome[] {Biome.PINE, Biome.TAIGA, Biome.ICE}, approxLat);

		//now add the topsoil
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
				byte topsoilBlock = zone == SNOW_ZONE ? Block.Snow_Block.val : Block.Grass.val;
				Blocks[h-2][z][x] = topsoilBlock;
				Blocks[h-1][z][x] = topsoilBlock;
				if (zone != PINE_ZONE) {
					Blocks[h][z][x] = Block.Snow.val;
				} else {
					Blocks[h][z][x] = Block.Long_Grass.val;
					Data[h][z][x] = RANDOM.nextInt(2) == 0 ? (byte) 1 : DataSource.Fern.val;
				}
			}
		}
		//now we place trees
		if (containsTrees) {
			for (int x = -Util.CHUNK_START; x < Util.CHUNK_END; x++) {
				for (int z = -Util.CHUNK_START; z < Util.CHUNK_END; z++) {
					int absX = x + absChunkX * 16;
					int absZ = z + absChunkZ * 16;
					byte treeHeight = sobol.getTreeHeight(absX, absZ);
					if (treeHeight != 0) {
						int zone = zonator.checkVal(heights[x+Util.CHUNK_START][z+Util.CHUNK_START], approxLat);
						if (zone != SNOW_ZONE) {
							boolean hasSnow = zone == TAIGA_ZONE;
							TreeWriter.placePineTree(x, z, this, hasSnow, treeHeight);
						}
					}
				}
			}
		}

		//		//add up to fifteen trees
		//		if (containsTrees) {
		//			for (int i = 0; i < 15; i++) {
		//				int x = RANDOM.nextInt(12) + 2;
		//				int z = RANDOM.nextInt(12) + 2;
		//				int zone = zonator.checkVal(heights[x][z], approxLat);
		//				if (zone == SNOW_ZONE)
		//					continue;
		//				boolean hasSnow = zone == TAIGA_ZONE;
		//				TreeWriter.placePineTree(x, z, heights, hasSnow, Blocks, Data);
		//			}
		//		}
	}
}
