package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;

public class GenericWriter {

	private static final Random RANDOM = new Random();
	//	
	//	public static void addBedrock(byte[][][] blocks, int[][] heights) {
	//		addBedrock(blocks, heights, 2);
	//	}

	public static void addHeavenWaterFall(Chunk chunk) {
		chunk.Blocks[255][8][8] = Blocka.Water;
	}

	public static void addGrass(Chunk chunk) {
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				int h = chunk.getHeights(x, z);
				chunk.Blocks[0][z][x] = Blocka.Bedrock;
				for (int i = 1; i < h - 1; i++) {
					chunk.Blocks[i][z][x] = Blocka.Dirt;
				}
				chunk.Blocks[h-1][z][x] = Blocka.Grass;
			}
		}
	}

	public static void addNorthGlassWall(Chunk chunk) {
		for (int y = 0; y < 255; y++) {
			for (int i = 0; i < 16; i++) {
				chunk.Blocks[y][7][i] = Blocka.Glass;
			}
		}
		for (int x = 0; x < 16; x++) {
			ArtifactWriter.addSign(chunk, chunk.getHeights(x, 8), 8, x, new String[] {"Get rid of", "the glass", "chunkmapper.com", "/account"});
			ArtifactWriter.addSign(chunk, chunk.getHeights(x, 6), 6, x, new String[] {"Get rid of", "the glass", "chunkmapper.com", "/account"}, (byte) 8);
		}
	}
	public static void addWestGlassWall(Chunk chunk) {
		for (int y = 0; y < 255; y++) {
			for (int i = 0; i < 16; i++) {
				chunk.Blocks[y][i][0] = Blocka.Glass;
			}
		}
		for (int z = 0; z < 16; z++) {
			ArtifactWriter.addSign(chunk, chunk.getHeights(1, z), z, 1, new String[] {"Get rid of", "the glass", "chunkmapper.com", "/account"}, (byte) 12);
		}
	}

	public static void addBedrock(Chunk chunk) {
		addBedrock(chunk, 2);
	}
	public static void addBedrock(Chunk chunk, int soilThickness) {
		int c = 0;
		int coalWidth = 10 * 4, ironWidth = 20 * 4, redstoneWidth = 30 * 4, goldWidth = 40 * 4, diamondWidth = 50 * 4;
		int emeraldWidth = 60 * 4;

		int nextCoal = RANDOM.nextInt(coalWidth);
		int nextIron = RANDOM.nextInt(ironWidth);
		int nextRedstone = RANDOM.nextInt(redstoneWidth);
		int nextGold = RANDOM.nextInt(goldWidth);
		int nextDiamond = RANDOM.nextInt(diamondWidth);
		int nextEmerald = RANDOM.nextInt(emeraldWidth);

		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				chunk.Blocks[0][z][x] = Block.Bedrock.val;
				int maxBedrock = chunk.getHeights(x, z) - soilThickness;
				//				byte colType = RANDOM.nextInt(3) == 0 ? Block.Gravel.val : Block.Stone.val;
				for (int y = 1; y < maxBedrock; y++) {
					byte fill = Block.Stone.val;
					if (c == nextCoal) {
						fill = Block.Coal_Ore.val;
						nextCoal += RANDOM.nextInt(coalWidth);
					}
					if (c == nextIron) {
						fill = Block.Iron_Ore.val;
						nextIron += RANDOM.nextInt(ironWidth);
					}
					if (c == nextRedstone) {
						fill = Block.Redstone_Ore_Type_1.val;
						nextRedstone += RANDOM.nextInt(redstoneWidth);
					}
					if (c == nextGold) {
						fill = Block.Gold_Ore.val;
						nextGold += RANDOM.nextInt(goldWidth);
					}
					if (c == nextDiamond) {
						fill = Block.Diamond_Ore.val;
						nextGold += RANDOM.nextInt(diamondWidth);
					}
					if (c == nextEmerald) {
						fill = Blocka.Emerald;
						nextEmerald += RANDOM.nextInt(emeraldWidth);
					}
					chunk.Blocks[y][z][x] = fill;
					c++;
				}
			}
		}
	}
	//	public static void setBlocks(byte[][][] blocks, int[][] heights) {
	//		int topsoilThickness = 3;
	//		for (int z = 0; z < 16; z++) {
	//			for (int x = 0; x < 16; x++) {
	//				blocks[0][z][x] = Block.Bedrock.val;
	//				int h = heights[x + Utila.CHUNK_START][z + Utila.CHUNK_START];
	//				if (h < 0) {
	//					blocks[1][z][x] = Block.Sand.val;
	//					blocks[2][z][x] = Block.Water.val;
	//					blocks[3][z][x] = Block.Water.val;
	//				} else {
	//					int maxBedrock = h - topsoilThickness;
	//					if (maxBedrock < 1)
	//						maxBedrock = 1;
	//					for (int y = 1; y < maxBedrock; y++) {
	//						blocks[y][z][x] = Block.Stone.val;
	//					}
	//					for (int y = maxBedrock; y < h; y++) {
	//						blocks[y][z][x] = Block.Grass.val;
	//					}
	//				}
	//			}
	//		}
	//	}

	public static void placeBeacon(byte[][][] blocks, Chunk chunk) {
		int h = 0;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (chunk.getHeights(i, j) > h) h = chunk.getHeights(i, j);
			}
		}
		for (int i = 0; i < 4; i++) {
			int y = h + i;
			for (int j = i; j < 9 - i; j++) {
				for (int k = i; k < 9 - i; k++) {
					blocks[y][j][k] = Block.Diamond_Block.val;
				}
			}
		}
	}
	//	public static void setBiomes(byte[] biomes, int[][] heights, TriangleZonator zonator, Biome[] biomesList, int approxLat) {
	//		for (int i = 0; i < 256; i++) {
	//			int z = i / 16;
	//			int x = i % 16;
	//			int h = heights[x + Utila.CHUNK_START][z + Utila.CHUNK_START];
	//			biomes[i] = biomesList[zonator.checkVal(h, approxLat)].val;
	//		}
	//	}

}
