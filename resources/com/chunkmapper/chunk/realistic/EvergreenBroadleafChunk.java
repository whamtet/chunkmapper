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

public class EvergreenBroadleafChunk extends Chunk {

	private static Sobol sobol = new Sobol(15, new TreeGenerator() {
		public byte getTreeHeight() {
						return (byte) (5 + RANDOM.nextInt(10));
		}
	});

	public static TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(4000, 30);
		zonator.addPoint(4000, 70);
		zonator.addPoint(4500, 80);
	}
	private static final int JUNGLE_AND_VINES = 0, JUNGLE = 1, GRASS = 2, SNOW = 3;
	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		int h = chunk.getHeights(x, z);
		final int zone = zonator.checkVal(h, approxLat);
		if (zone == SNOW) {
			chunk.Blocks[h-3][z][x] = Block.Stone.val;
			chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
			chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
		} else {
			chunk.Blocks[h-3][z][x] = Block.Dirt.val;
			chunk.Blocks[h-2][z][x] = Block.Dirt.val;
			chunk.Blocks[h-1][z][x] = Block.Grass.val;
		}
		if (zone == GRASS) {
			if (RANDOM.nextInt(4) == 0) {
				chunk.Blocks[h][z][x] = Block.Long_Grass.val;
				chunk.Data[h][z][x] = 1;
			}
		}
		if (zone == JUNGLE_AND_VINES || zone == JUNGLE) {
			if (RANDOM.nextInt(2) == 0) {
				chunk.Blocks[h][z][x] = Block.Long_Grass.val;
				chunk.Data[h][z][x] = DataSource.Fern.val;
			} else {
				chunk.Blocks[h][z][x] = Block.Leaves.val;
				chunk.Data[h][z][x] = zone == JUNGLE_AND_VINES ? DataSource.Jungle.val : DataSource.Birch.val;
			}
		}
		int absX = x + absChunkX * 16;
		int absZ = z + absChunkZ * 16;
		byte treeHeight = sobol.getTreeHeight(absX, absZ);
		if (treeHeight != 0) {
			if (zone == JUNGLE_AND_VINES || zone == JUNGLE) {
				boolean placeVines = zone == JUNGLE_AND_VINES;
				TreeWriter.placeJungleTree(x, z, chunk, placeVines, treeHeight);
			}
		}
	}

	public EvergreenBroadleafChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		this.heights = heights;
		final int soilThickness = 3;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);
		GenericWriter.setBiomes(Biomes, heights, zonator, new Biome[] {Biome.JUNGLE, Biome.JUNGLE, Biome.PLAINS, Biome.ICE}, approxLat);

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
				containsTrees |= zone == JUNGLE_AND_VINES || zone == JUNGLE;

				if (zone == SNOW) {
					Blocks[h-3][z][x] = Block.Stone.val;
					Blocks[h-2][z][x] = Block.Snow_Block.val;
					Blocks[h-1][z][x] = Block.Snow_Block.val;
				} else {
					Blocks[h-3][z][x] = Block.Dirt.val;
					Blocks[h-2][z][x] = Block.Dirt.val;
					Blocks[h-1][z][x] = Block.Grass.val;
				}
				if (zone == GRASS) {
					if (RANDOM.nextInt(4) == 0) {
						Blocks[h][z][x] = Block.Long_Grass.val;
						Data[h][z][x] = 1;
					}
				}
				if (zone == JUNGLE_AND_VINES || zone == JUNGLE) {
					if (RANDOM.nextInt(2) == 0) {
						Blocks[h][z][x] = Block.Long_Grass.val;
						Data[h][z][x] = DataSource.Fern.val;
					} else {
						Blocks[h][z][x] = Block.Leaves.val;
						Data[h][z][x] = zone == JUNGLE_AND_VINES ? DataSource.Jungle.val : DataSource.Birch.val;
					}
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
						if (zone == JUNGLE_AND_VINES || zone == JUNGLE) {
							boolean placeVines = zone == JUNGLE_AND_VINES;
							TreeWriter.placeJungleTree(x, z, this, placeVines, treeHeight);
						}
					}
				}
			}
		}
		//		if (containsTrees) {
		//			for (int i = 0; i < 20; i++) {
		//				int rad = 3;
		//				int x = rad + RANDOM.nextInt(16 - 2 * rad);
		//				int z = rad + RANDOM.nextInt(16 - 2 * rad);
		//				int zone = zonator.checkVal(heights[x][z], approxLat);
		//				if (zone == JUNGLE_AND_VINES || zone == JUNGLE) {
		//					boolean placeVines = zone == JUNGLE_AND_VINES;
		//					TreeWriter.placeJungleTree(x, z, heights, Blocks, Data, placeVines, rad);
		//				}
		//			}
		//		}
	}

}
