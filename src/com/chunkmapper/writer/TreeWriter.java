package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.reader.HeightsReader;

public class TreeWriter {
	public final static Random RANDOM = new Random();
	
	public static int getJungleTreeHeight() {
		return 5 + RANDOM.nextInt(5);
	}
	public static int getForestTreeHeight() {
		return 3 + RANDOM.nextInt(3);
	}
	public static int getPineTreeHeight() {
		return 4 + RANDOM.nextInt(4);
	}
	public static int getShrubHeight() {
		return 2 + RANDOM.nextInt(3);
	}
	public static int getSavannaTreeHeight() {
		return 3 + RANDOM.nextInt(2);
	}

	//used by placePineTree
	private static void placeStar(int x, int z, int h, int rad, boolean hasSnow, Chunk chunk) {
		if (rad == 0) {
			if (hasSnow && chunk.getBlock(h+1, z, x) == 0) chunk.setBlock(h+1, z, x, Block.Snow.val);
			chunk.setBlock(h, z, x, Block.Leaves.val);
			chunk.setData(h, z, x, DataSource.Spruce.val);
		} else {
			for (int a = -rad; a <= rad; a++) {
				for (int b = -rad; b <= rad; b++) {
					if (a != -rad && a != rad || b != -rad && b != rad) {
						if (hasSnow && chunk.getBlock(h+1, z+a, x+b) == 0) chunk.setBlock(h+1, z+a, x+b, Block.Snow.val);
						chunk.setBlock(h, z+a, x+b, Block.Leaves.val);
						chunk.setData(h, z+a, x+b, DataSource.Spruce.val);
					}
				}
			}
		}

	}
	//used by placeJungleTree
	private static void placeRaggedBox(int x, int z, int rad, int y, int h, int maxVineDrop, Chunk chunk, boolean placeVines) {
		int x0  = x - rad;
		int x2 = x + rad;
		int z0 = z - rad;
		int z2 = z + rad;
		byte leafType = placeVines ? DataSource.Jungle.val : DataSource.Birch.val;
		for (int yd = y; yd < y + h; yd++) {
			for (int z1 = z0; z1 <= z2; z1++) {
				for (int x1 = x0; x1 <= x2; x1++) {
					if (RANDOM.nextInt(3) > 0) {
						chunk.setBlock(yd, z1, x1, Block.Leaves.val);
						chunk.setData(yd, z1, x1, leafType);
					}
				}
			}
		}
		if (placeVines) {
			//add some vines
			int w = 2 * rad + 1;
			int numVines = w*w/3;
			for (int i = 0; i < numVines; i++) {
				//may wish to ask before you interrupt here
				int vineDrop = 1 + RANDOM.nextInt(maxVineDrop - 1);
				int xd = x0 + RANDOM.nextInt(w);
				int zd = z0 + RANDOM.nextInt(w);
				byte directionByte = (byte) (1 << RANDOM.nextInt(4));
				for (int yd = y - vineDrop; yd < y + h; yd++) {
					if (chunk.getBlock(yd, zd, xd) == 0) {
						chunk.setBlock(yd, zd, xd, Block.Vine.val);
						chunk.setData(yd, zd, xd, (byte) (chunk.getData(yd, zd, xd) + directionByte));
					}
				}
			}
		}
	}
	public static void placeJungleTree(int absx, int absz, Chunk chunk, HeightsReader heightsReader, int treeHeight) {
		boolean placeVines = true;

		int rad = 3;
		int trunkWidth = treeHeight < 10 ? 1 : 2;

		int rootHeight = heightsReader.getHeightxz(absx, absz);
		int rootHeight2 = heightsReader.getHeightxz(absx+1, absz+1);
		if (trunkWidth == 2 && rootHeight2 < rootHeight) rootHeight = rootHeight2;
		

		if (rootHeight < 0) //we are over ocean
			return;

		//place crown
		placeRaggedBox(absx, absz, rad, rootHeight + treeHeight - 2, 3, treeHeight, chunk, placeVines);
		if (treeHeight >= 10)
			placeRaggedBox(absx, absz, rad, rootHeight + treeHeight / 2, 3, treeHeight/2, chunk, placeVines);
		//place trunk
		for (int y = rootHeight; y < rootHeight + treeHeight; y++) {
			chunk.setBlock(y, absz, absx, Block.Wood.val);
			chunk.setData(y, absz, absx, DataSource.Jungle.val);
			if (trunkWidth == 2) {
				chunk.setBlock(y, absz+1, absx+1, Block.Wood.val);
				chunk.setData(y, absz+1, absx+1, DataSource.Jungle.val);
			}
		}
		//dirt under trunk
		chunk.setBlock(rootHeight-1, absz, absx, Block.Dirt.val);
		if (trunkWidth == 2) {
			chunk.setBlock(rootHeight-1, absz+1, absx+1, Block.Dirt.val);
		}

	}
	public static void placePineTree(int absx, int absz, Chunk chunk,
			boolean hasSnow, int baseHeight, HeightsReader heightsReader) {

		int h = heightsReader.getHeightxz(absx, absz);
//		if (h < 0)
//			return;//we are over ocean
		int crownHeight = 3;

		//crown
		placeStar(absx, absz, h + baseHeight - 1, crownHeight - 1, false, chunk);
		for (int rad = crownHeight - 1; rad >= 0; rad--) {
			placeStar(absx, absz, h + baseHeight + crownHeight - rad, rad, hasSnow, chunk);
		}
		//trunk
		for (int y = h; y < h + baseHeight + 2; y++) {
			chunk.setBlock(y, absz, absx, Block.Wood.val);
			chunk.setData(y, absz, absx, DataSource.Spruce.val);
		}
		//has dirt under trunk
		chunk.setBlock(h-1, absz, absx, Block.Dirt.val);

		//clear snow around base of tree
		if (hasSnow) {
			int rad = 2;
			for (int a = absx - rad; a <= absx + rad; a++) {
				for (int b = absz - rad; b <= absz + rad; b++) {
					int h2 = chunk.getHeights(a, b);
					if (h2 != -1 && chunk.getBlock(h2, b, a) == Block.Snow.val && RANDOM.nextInt(10) == 0) {
						chunk.setBlock(h2, b, a, Block.Long_Grass.val);
						chunk.setData(h2, b, a, RANDOM.nextInt(2) == 0 ? (byte) 1 : DataSource.Fern.val);
					}
				}
			}
		}
	}
	public static void placeShrub(int absx, int absz, Chunk chunk, boolean hasSnow,
			HeightsReader heightsReader, int shrubHeight) {
		
		int h = Integer.MAX_VALUE;
		for (int xd = absx; xd < absx + 2; xd++) {
			for (int zd = absz; zd < absz + 2; zd++) {
				int hd = heightsReader.getHeightxz(xd, zd);
				if (hd < h) h = hd;
			}
		}
		if (h < 4)
			return;
		for (int y = h; y < h + shrubHeight; y++) {
			for (int zd = absz; zd < absz + 2; zd++) {
				for (int xd = absx; xd < absx + 2; xd++) {
					if (RANDOM.nextInt(3) > 0) {
						chunk.setBlock(y, zd, xd, Block.Leaves.val);
						chunk.setData(y, zd, xd, DataSource.Jungle.val);
					}
				}
			}
		}
		if (hasSnow)
			throw new RuntimeException();
		
	}

//	public static void placeShrub(int x, int z, Chunk chunk, boolean hasSnow) {
//		
//		if (x > 14 || z > 14)
//			return;//no shrubs near edge
//		int shrubHeight = 2 + RANDOM.nextInt(3);
//		int shrubBase = Integer.MAX_VALUE;
//		for (int i = 0; i < 4; i++) {
//			int h = chunk.getHeights(x + i / 2, z + i%2);
//			if (h < shrubBase)
//				shrubBase = h;
//		}
//		if (shrubBase < 0)
//			return;//we're over water
//		
//		for (int y = shrubBase; y < shrubBase + shrubHeight; y++) {
//			for (int z2 = z; z2 < z + 2; z2++) {
//				for (int x2 = x; x2 < x + 2; x2++) {
//					if (RANDOM.nextInt(3) > 0 && chunk.Blocks[y][z2][x2] == 0) {
//						chunk.Blocks[y][z2][x2] = Block.Leaves.val;
//						chunk.Data[y][z2][x2] = DataSource.Jungle.val;
//					}
//				}
//			}
//		}
//		if (hasSnow) {
//			for (int z2 = z; z2 < z + 2; z2++) {
//				for (int x2 = x; x2 < x + 2; x2++) {
//					for (int y = shrubBase + shrubHeight - 1; y >= shrubBase; y--) {
//						if (chunk.Blocks[y][z2][x2] == Block.Leaves.val) {
//							if (RANDOM.nextInt(2) == 0 && chunk.Blocks[y+1][z2][x2] == 0)
//								chunk.Blocks[y+1][z2][x2] = Block.Snow.val;
//							break;
//						}
//					}
//				}
//			}
//		}
//	}

//	public static void placeSwampTree(int x, int z, Chunk chunk, boolean hasSnow) {
//		//make crown
//		int[] rads = new int[4];
//		for (int i = 0; i < 4; i++) {
//			rads[i] = 2 + RANDOM.nextInt(2);
//		}
////		int crownWidth = 5 + RANDOM.nextInt(3);
////		int crownLength = 5 + RANDOM.nextInt(3);
//		int trunkHeight = 2 + RANDOM.nextInt(3);
//		
//		//trunk height
//		int h = chunk.getHeights(x, z);
//		if (h < 0)
//			return;//we're over water
//		for (int y = h - 1; y < h + trunkHeight + 1; y++) {
//			chunk.setBlock(y, z, x, Block.Wood.val);
//			chunk.setData(y, z, x, DataSource.Oak.val);
//		}
//		//first layer of tree
//		for (int i = x - rads[0]; i <= x + rads[1]; i++) {
//			for (int j = z - rads[2]; j <= z + rads[3]; j++) {
//				if (i != x || j != z) {
//					chunk.setBlock(h+trunkHeight, j, i, Block.Leaves.val);
//					if (hasSnow && RANDOM.nextInt(3) > 0)
//						chunk.setBlock(h+trunkHeight+1, j, i, Block.Snow.val);
//				}
//			}
//		}
//		
//		for (int i = x - rads[0] + 1; i <= x + rads[1] - 1; i++) {
//			for (int j = z - rads[2] + 1; j <= z + rads[3] - 1; j++) {
//				if (i != x - rads[0] + 1 && i != x + rads[1] - 1 || j != z - rads[2] + 1 && j != z + rads[3] - 1) {
//					chunk.setBlock(h+trunkHeight+1, j, i, Block.Leaves.val);
//					if (hasSnow && RANDOM.nextInt(3) > 0)
//						chunk.setBlock(h+trunkHeight+2, j, i, Block.Snow.val);
//				}
//			}
//		}
//		
//	}
	public static void placeSavannaTree(int absx, int absz, Chunk chunk,
			HeightsReader heightsReader, int treeHeight) {
		//place trunk
		int h = heightsReader.getHeightxz(absx, absz);
		for (int y = h; y < h + treeHeight; y++) {
			chunk.setBoth(y, absz, absx, Block.Wood.val, DataSource.Jungle.val);
		}
		//crown
		for (int z2 = absz - 1; z2 <= absz + 1; z2++) {
			for (int x2 = absx - 1; x2 <= absx + 1; x2++) {
				chunk.setBoth(h+treeHeight, z2, x2, Block.Leaves.val, DataSource.Jungle.val);
				if (treeHeight == 4) {
					chunk.setBoth(h+treeHeight-1, z2, x2, Block.Leaves.val, DataSource.Jungle.val);
				}
			}
		}
		//bits on the end
		chunk.setBoth(h+treeHeight+1, absz, absx, Block.Leaves.val, DataSource.Jungle.val);
		
		chunk.setBoth(h+treeHeight, absz-2, absx, Block.Leaves.val, DataSource.Jungle.val);
		chunk.setBoth(h+treeHeight, absz+2, absx, Block.Leaves.val, DataSource.Jungle.val);
		chunk.setBoth(h+treeHeight, absz, absx-2, Block.Leaves.val, DataSource.Jungle.val);
		chunk.setBoth(h+treeHeight, absz, absx+2, Block.Leaves.val, DataSource.Jungle.val);

		if (treeHeight == 4) {
			
			chunk.setBoth(h+treeHeight-1, absz-2, absx, Block.Leaves.val, DataSource.Jungle.val);
			chunk.setBoth(h+treeHeight-1, absz+2, absx, Block.Leaves.val, DataSource.Jungle.val);
			chunk.setBoth(h+treeHeight-1, absz, absx-2, Block.Leaves.val, DataSource.Jungle.val);
			chunk.setBoth(h+treeHeight-1, absz, absx+2, Block.Leaves.val, DataSource.Jungle.val);
		}

	}
	public static void placeForestTree(int absx, int absz, Chunk chunk,
			HeightsReader heightsReader, int crownHeight) {
		
		int h = heightsReader.getHeightxz(absx, absz);
		boolean hasSnow = false;

		//crown
		for (int c = absx - 2; c <= absx + 2; c++) {
			for (int d = absz - 2; d <= absz + 2; d++) {
				if (RANDOM.nextInt(5) > 0) {
					chunk.setBlock(h+crownHeight, d, c, Block.Leaves.val);
					chunk.setBlock(h+crownHeight+1, d, c, Block.Leaves.val);
				}
			}
		}
		//cap
		for (int c = absx - 1; c <= absx + 1; c++) {
			for (int d = absz - 1; d <= absz + 1; d++) {
				if (RANDOM.nextInt(5) > 0) chunk.setBlock(h+crownHeight+2, d, c, Block.Leaves.val);
			}
		}
		//now we iterate over the crown dropping snow
		if (hasSnow) {
			for (int c = absx - 2; c <= absx + 2; c++) {
				for (int d = absz - 2; d <= absz + 2; d++) {
					//work down, dropping snow on top of first leaves
					for (int y = h + crownHeight + 3; y >= h + crownHeight; y--) {
						if (chunk.getBlock(y, d, c) == Block.Leaves.val) {
							if (chunk.getBlock(y+1, d, c) == 0)
								chunk.setBlock(y+1, d, c, Block.Snow.val);
							break;
						}
					}
				}
			}
			//remove snow around base of t
		}
		//trunk
		for (int y = h; y <= h + crownHeight+1; y++) {
			chunk.setBlock(y, absz, absx, Block.Wood.val);
			chunk.setData(y, absz, absx, (byte) 0);
		}
		//grass doesn't grow under the trunk!
		chunk.setBlock(h-1, absz, absx, Block.Dirt.val);

		//clear some of the snow around the trunk
		if (hasSnow) {
			int rad = 2;
			for (int a = absx - rad; a <= absx + rad; a++) {
				for (int b = absz - rad; b <= absz + rad; b++) {
					int h2 = chunk.getHeights(a, b);
					if (h2 != -1 && chunk.getBlock(h2, b, a) == Block.Snow.val && RANDOM.nextInt(3) == 0) {
						chunk.setBlock(h2, b, a, Block.Long_Grass.val);
						chunk.setData(h2, b, a, RANDOM.nextInt(2) == 0 ? (byte) 1 : DataSource.Fern.val);
					}
				}
			}
		}

	}
	
	
	

	


}
