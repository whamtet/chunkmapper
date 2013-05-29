package com.chunkmapper.chunk.realistic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;

public class CroplandsChunk extends Chunk {
	protected static TriangleZonator zonator = new TriangleZonator();
	static {
		zonator.addPoint(3000, 65);
		zonator.addPoint(4000, 75);
	}
	private static final int CROP_ZONE = 0, SNOWY_CROP_ZONE = 1, SNOW_ZONE = 2;
	
	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		int h = chunk.getHeights(x, z);
		final int zone = zonator.checkVal(h, approxLat);
		if (zone == CROP_ZONE || zone == SNOWY_CROP_ZONE) {
			chunk.Blocks[h-3][z][x] = Block.Dirt.val;
			chunk.Blocks[h-2][z][x] = Block.Dirt.val;
			chunk.Blocks[h-1][z][x] = Block.Farmland.val;
			if (zone == SNOWY_CROP_ZONE && RANDOM.nextInt(2) == 0) {
				chunk.Blocks[h][z][x] = Block.Snow.val;
			} else {
				chunk.Blocks[h][z][x] = Block.Wheat.val;
				chunk.Data[h][z][x] = 7;
			}
		} else {
			//snow zone
			chunk.Blocks[h-3][z][x] = Block.Stone.val;
			chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
			chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
		}
	}
	public CroplandsChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		final int soilThickness = 3;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);
		this.heights = heights;
		byte[] possibleCrops = {Block.Wheat.val, Block.Carrots.val, Block.Potatoes.val, Block.Wheat.val,
				Block.Wheat.val, Block.Wheat.val, Block.Wheat.val, Block.Wheat.val};
		byte actualCrop = possibleCrops[RANDOM.nextInt(possibleCrops.length)];

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int h = this.getHeights(x, z);
				if (h < 0) {
					super.writeOceanColumn(x, z);
					continue;
				}
				int zone = zonator.checkVal(h, approxLat);
				if (zone == CROP_ZONE || zone == SNOWY_CROP_ZONE) {
					Blocks[h-3][z][x] = Block.Dirt.val;
					Blocks[h-2][z][x] = Block.Dirt.val;
					Blocks[h-1][z][x] = Block.Farmland.val;
					if (zone == SNOWY_CROP_ZONE && RANDOM.nextInt(2) == 0) {
						Blocks[h][z][x] = Block.Snow.val;
					} else {
						Blocks[h][z][x] = actualCrop;
						Data[h][z][x] = 7;
					}
				} else {
					//snow zone
					Blocks[h-3][z][x] = Block.Stone.val;
					Blocks[h-2][z][x] = Block.Snow_Block.val;
					Blocks[h-1][z][x] = Block.Snow_Block.val;
				}
			}
		}

	}

}
