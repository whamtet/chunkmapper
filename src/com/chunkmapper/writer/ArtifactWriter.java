package com.chunkmapper.writer;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.CircleRail;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.LadderWallsignFurnaceChest;
import com.chunkmapper.enumeration.Stairs;
import com.chunkmapper.enumeration.StraightRail;
import com.chunkmapper.enumeration.WoolColor;
import com.chunkmapper.math.Matthewmatics;
import com.mojang.nbt.CompoundTag;

public class ArtifactWriter {
	private int spacesTillNextPoweredRail = 1;
	private static void addWool(Chunk chunk, int h, int z, int x) {
		chunk.Blocks[h][z][x] = Block.Wool.val;
		chunk.Data[h][z][x] = WoolColor.Light_Gray.val;
	}
	//	public static void addLotsOfWool(Chunk chunk) {
	//		for (int y = 0; y < 5; y++) {
	//			for (int z = 0; z < 16; z++) {
	//				for (int x = 0; x < 16; x++) {
	//					addWool(chunk, y, z, x);
	//				}
	//			}
	//		}
	//	}
	private static void addArch(Chunk chunk, int x0, int z0, int h, boolean lastArch) {
		for (int y = h; y < h + 5; y++) {
			for (int x = x0; x < x0 + 3; x++) {
				chunk.Blocks[y][z0][x] = Blocka.Stone_Brick;
			}
		}
		if (lastArch)
			return;
		chunk.Blocks[h][z0][x0+1] = Blocka.Stone_Stairs;
		chunk.Data[h][z0][x0+1] = Stairs.Ascending_North.val;
		for (int y = h + 1; y < h + 4; y++) {
			chunk.Blocks[y][z0][x0+1] = 0;
		}
		//		chunk.Blocks[h+3][z0][x0+1] = Blocka.Torch;
		//		chunk.Data[h+3][z0][x0+1] = Torch.POINTING_WEST;

	}
	private static void clearOut(Chunk chunk, int x0, int z0, int h0, int width, int length, int h) {
		for (int y = h0; y < h0 + h; y++) {
			for (int z = z0; z < z0 + length; z++) {
				for (int x = x0; x < x0 + width; x++) {
					chunk.Blocks[y][z][x] = 0;
					chunk.Data[y][z][x] = 0;
				}
			}
		}
	}
	public static void addGallows(Chunk chunk) {
		int x0 = 6, z0 = 5;
		int width = 5, length = 5;
		int h = ArtifactWriter.getMeanHeight(chunk, x0, z0, width, length);
		clearOut(chunk, x0, z0, h, width, length, 7);

		//platform
		for (int z = z0; z < z0 + length; z++) {
			for (int x = x0; x < x0 + width; x++) {
				chunk.Blocks[h][z][x] = Blocka.Planks;
			}
		}
		int x = x0 + 2;
		int z = z0;
		for (int y = h + 1; y < h + 6; y++) {
			chunk.Blocks[y][z][x] = Blocka.Planks;
		}
		z++;
		chunk.Blocks[h+1][z][x] = Blocka.Planks;
		chunk.Blocks[h+5][z][x] = Blocka.Planks;
		z++;
		chunk.Blocks[h][z][x] = 0;
		chunk.Blocks[h+5][z][x] = Blocka.Planks;

		chunk.Blocks[h+1][z][x] = Blocka.Trapdoor;
		chunk.Data[h+1][z][x] = 9;

		//lastly, a warning
		String[] warning = new String[4];
		switch(chunk.RANDOM.nextInt(12)) {
		case 0:
			warning[0] = "Whoso sheddeth"; warning[1] = "man's blood";
			warning[2] = "by man shall"; warning[3] = "have blood shed";
			break;
		case 1:
			warning[0] = "He that killeth"; warning[1] = "with the sword";
			warning[2] = "must be killed"; warning[3] = "";
			break;
		case 2:
//			warning[0]
			warning[0] = "Whoever takes a";
			warning[1] = "human life shall";
			warning[2] = "surely be put";
			warning[3] = "to death";
			break;
		case 3:
			warning[0] = "Your eye";
			warning[1] = "shall not";
			warning[2] = "pity him";
			warning[3] = " ";
			break;
		case 4:
			warning[0] = "If anyone kills";
			warning[1] = "a person the";
			warning[2] = "murderer shall be";
			warning[3] = "put to death";
			break;
		case 5:
			warning[0] = "You";
			warning[1] = "shall";
			warning[2] = "not";
			warning[3] = "murder";
			break;
		case 6:
			warning[0] = "The authorities";
			warning[1] = "that exist have";
			warning[2] = "been instituted";
			warning[3] = "by God";
			break;
		case 7:
			warning[0] = "I say to";
			warning[1] = "the wicked:";
			warning[2] = "'You shall";
			warning[3] = "surely die'";
			break;
		case 8:
			warning[0] = "You shall";
			warning[1] = "not permit";
			warning[2] = "a sorceress";
			warning[3] = "to live";
			break;
		case 9:
			warning[0] = "Whoever curses";
			warning[1] = "his father or";
			warning[2] = "his mother shall";
			warning[3] = "be put to death";
			break;
		case 10:
			warning[0] = "Everyone who";
			warning[1] = "hates his";
			warning[2] = "brother is";
			warning[3] = "a murderer";
			break;
		case 11:
			warning[0] = "Let a woman";
			warning[1] = "learn quietly";
			warning[2] = "with all";
			warning[3] = "submissiveness";
			break;
		}
		String[] warning2 = {"ok"};
		ArtifactWriter.addSign(chunk, h, chunk.zr + z0 + length + 1, chunk.xr + x0 + 2, warning);

	}
	public static void addTunnelIntoTheUnknown(Chunk chunk) {
		int x0 = 7;
		int z0 = 2;
		int h0 = chunk.getHeights(x0, z0);
		int hd = chunk.getHeights(x0+1, z0);
		if (hd < h0) h0 = hd;
		hd = chunk.getHeights(x0+2, z0);
		if (hd < h0) h0 = hd;

		for (int z = z0, h = h0 - 2; z < 16 && h > 0; z++, h--) {
			addArch(chunk, x0, z, h, h == 1 || z == 15);
		}
	}
	public static void addSign(Chunk chunk, int h, int z, int x, String[] lines) {
		CompoundTag sign = new CompoundTag();
		sign.putString("id", "Sign");
		sign.putInt("x", x);
		sign.putInt("y", h);
		sign.putInt("z", z);

		x = Matthewmatics.mod(x, 16);
		z = Matthewmatics.mod(z, 16);
		chunk.Blocks[h][z][x] = Block.Sign_Placed_Floor.val;

		String Text1 = lines.length > 0 ? lines[0] : "";
		String Text2 = lines.length > 1 ? lines[1] : "";
		String Text3 = lines.length > 2 ? lines[2] : "";
		String Text4 = lines.length > 3 ? lines[3] : "";

		sign.putString("Text1", Text1);
		sign.putString("Text2", Text2);
		sign.putString("Text3", Text3);
		sign.putString("Text4", Text4);

		chunk.TileEntities.add(sign);
	}

	public static void addHouse(Chunk chunk) {
		int x0 = 1, z0 = 2;
		int houseWidth = 6, houseLength = 7;

		//first we need to get house height
		int s = 0, n = 0;
		for (int i = z0; i < z0 + houseLength; i++) {
			for (int j = x0; j < x0+houseWidth; j++) {
				int h = chunk.getHeights(j, i);
				if (h < 0) h = 4;
				s += h; n++;
			}
		}
		int h = s / n;

		//then we need to clear out space around it
		for (int y = h; y < h + 6; y++) {
			for (int z = z0 - 1; z < z0 + houseLength + 1; z++) {
				for (int x = x0 - 1; x < x0 + houseWidth + 1; x++) {
					chunk.Blocks[y][z][x] = Block.Air.val;
				}
			}
		}

		//walls
		byte wallBlock = 0, wallData = 0;
		switch(chunk.RANDOM.nextInt(2)) {
		case 0:
			wallBlock = Blocka.Bricks;
			break;
		case 1:
			wallBlock = Blocka.Wood;
			wallData = (byte) chunk.RANDOM.nextInt(4);
		}

		for (int y = h; y < h + 3; y++) {
			for (int z = z0; z < z0+houseLength; z++) {
				//				chunk.Blocks[y][z][x0] = Block.Bricks.val;
				//				chunk.Blocks[y][z][x0+houseWidth-1] = Block.Bricks.val;
				chunk.Blocks[y][z][x0] = wallBlock;
				chunk.Blocks[y][z][x0+houseWidth-1] = wallBlock;
				chunk.Data[y][z][x0] = wallData;
				chunk.Data[y][z][x0+houseWidth-1] = wallData;
			}
			for (int x = x0+1; x < x0+houseWidth-1; x++) {
				//				chunk.Blocks[y][z0][x] = Block.Bricks.val;
				//				chunk.Blocks[y][z0+houseLength-1][x] = Block.Bricks.val;
				chunk.Blocks[y][z0][x] = wallBlock;
				chunk.Blocks[y][z0+houseLength-1][x] = wallBlock;
				chunk.Data[y][z0][x] = wallData;
				chunk.Data[y][z0+houseLength-1][x] = wallData;
			}
		}
		//house may potentially be floating
		//floors
		for (int z = z0+1; z < z0+houseLength - 1; z++) {
			for (int x = x0+1; x < x0+houseWidth - 1; x++) {
				chunk.Blocks[h][z][x] = Block.Planks.val;
				chunk.Data[h][z][x] = DataSource.Birch.val;
			}
		}
		//ends of roof
		for (int x = x0+1; x < x0+5; x++) {
			addWool(chunk, h+3, z0, x);
			addWool(chunk, h+3, z0+houseLength-1, x);
		}
		addWool(chunk, h+4, z0, x0+2);
		addWool(chunk, h+4, z0, x0+3);
		addWool(chunk, h+4, z0+houseLength-1, x0+2);
		addWool(chunk, h+4, z0+houseLength-1, x0+3);

		//main roof
		for (int z = z0 - 1; z < z0 + houseLength + 1; z++) {
			for (int c = 0; c < 3; c++) {
				addWool(chunk, h+3+c, z, x0+c);
				addWool(chunk, h+3+c, z, x0+5-c);
			}
			//side eves
			addWool(chunk, h+3, z, x0-1);
			addWool(chunk, h+3, z, x0 + 6);
		}

		//door
		int doorPos = houseWidth / 2;
		chunk.Blocks[h+1][z0+houseLength-1][x0+doorPos] = Block.Wooden_Door.val;
		chunk.Blocks[h+2][z0+houseLength-1][x0+doorPos] = Block.Wooden_Door.val;

		//8 for right hinged, 9 for left hinged (of double)
		chunk.Data[h+2][houseLength-1+z0][doorPos+x0] = 8;
		//0 for closed (4 open) + 0, 1, 2, 3 for west, north east south
		chunk.Data[h+1][houseLength-1+z0][doorPos+x0] = 1;

		//stairs
		chunk.Blocks[h][houseLength+z0][doorPos+x0] = Block.Cobblestone_Steps.val;
		chunk.Data[h][houseLength+z0][doorPos+x0] = 3;

		//front window
		chunk.Blocks[h+1][houseLength+z0-1][x0+1] = Block.Window.val;
		chunk.Blocks[h+2][houseLength+z0-1][x0+1] = Block.Window.val;


		for (int y = h+1; y < h+4; y++) {
			//west window
			chunk.Blocks[y][z0+2][x0] = Block.Window.val;
			chunk.Blocks[y][z0+3][x0] = Block.Window.val;

			//east window
			chunk.Blocks[y][z0+2][x0+5] = Block.Window.val;
			chunk.Blocks[y][z0+3][x0+5] = Block.Window.val;

			//north window
			chunk.Blocks[y][z0][x0+2] = Block.Window.val;
			chunk.Blocks[y][z0][x0+3] = Block.Window.val;

		}
		//knock out the eves
		chunk.Blocks[h+3][z0+2][x0-1] = Block.Air.val;
		chunk.Blocks[h+3][z0+3][x0-1] = Block.Air.val;
		chunk.Blocks[h+3][z0+2][x0+houseWidth] = Block.Air.val;
		chunk.Blocks[h+3][z0+3][x0+houseWidth] = Block.Air.val;

		//and throw some new ones on top
		addWool(chunk, h+4, z0+2, x0);
		addWool(chunk, h+4, z0+3, x0);
		addWool(chunk, h+4, z0+2, x0+houseWidth-1);
		addWool(chunk, h+4, z0+3, x0+houseWidth-1);

		//and add a bed
		//		chunk.Blocks[4][0][0] = Block.Bed.val;
		//		chunk.Blocks[4][1][0] = Block.Bed.val;
		//		chunk.Data[4][1][0] = 2;
		//		chunk.Data[4][0][0] = 10;
		chunk.Blocks[h+1][z0+1][x0+1] = Blocka.Bed;
		chunk.Blocks[h+1][z0+2][x0+1] = Blocka.Bed;
		chunk.Blocks[h+1][z0+1][x0+2] = Blocka.Bed;
		chunk.Blocks[h+1][z0+2][x0+2] = Blocka.Bed;
		chunk.Data[h+1][z0+1][x0+1] = 10;
		chunk.Data[h+1][z0+1][x0+2] = 10;
		chunk.Data[h+1][z0+2][x0+1] = 2;
		chunk.Data[h+1][z0+2][x0+2] = 2;

		//and a small table
		chunk.Blocks[h+1][z0+1][x0+4] = Blocka.Fence;
		chunk.Blocks[h+2][z0+1][x0+4] = Blocka.Heavy_Plate;
	}
	//	private static void addPainting(Chunk chunk, int y, int z, int x) {
	//		chunk.Data[y][z][x] = 65;
	//		chunk.Add[y][z][x] = 1;
	//	}

	public static void placeLookout(Chunk chunk) {
		int lookoutHeight = 12;
		//need to get base height
		int h = Integer.MAX_VALUE;
		if (chunk.getHeights(14, 1) < h) h = chunk.getHeights(14, 1);
		if (chunk.getHeights(12, 1) < h) h = chunk.getHeights(12, 1);
		if (chunk.getHeights(12, 3) < h) h = chunk.getHeights(12, 3);
		if (chunk.getHeights(14, 3) < h) h = chunk.getHeights(14, 3);
		if (h < 4) h = 4;

		//clear space around platform
		for (int y = h; y < h + lookoutHeight + 2; y++) {
			for (int z = 0; z < 5; z++) {
				for (int x = 11; x < 16; x++) {
					chunk.Blocks[y][z][x] = Block.Air.val;
				}
			}
		}
		//foundation
		for (int y = h - 3; y < h; y++) {
			chunk.Blocks[y][1][14] = Block.Cobblestone.val;
			chunk.Blocks[y][1][12] = Block.Cobblestone.val;
			chunk.Blocks[y][3][12] = Block.Cobblestone.val;
			chunk.Blocks[y][3][14] = Block.Cobblestone.val;
		}
		//legs
		for (int y = h; y < h + lookoutHeight - 1; y++) {
			chunk.Blocks[y][1][14] = Block.Planks.val;
			chunk.Blocks[y][1][12] = Block.Planks.val;
			chunk.Blocks[y][3][12] = Block.Planks.val;
			chunk.Blocks[y][3][14] = Block.Planks.val;
		}
		//platform
		for (int z = 1; z < 4; z++) {
			for (int x = 12; x < 15; x++) {
				chunk.Blocks[h+lookoutHeight-1][z][x] = Block.Planks.val;
				//fence
				if ((x != 13 || z != 2) && (x != 12 || z != 3)) {
					chunk.Blocks[h+lookoutHeight][z][x] = Block.Fence.val;
				}
			}
		}
		//ladder up there
		for (int y = h; y < h + lookoutHeight; y++) {
			chunk.Blocks[y][4][12] = Block.Ladder.val;
			chunk.Data[y][4][12] = LadderWallsignFurnaceChest.Facing_South.val;
		}


	}
	public void placeRail(int x, int z, Chunk chunk, int railHeight, byte railType, boolean usePlanks,
			boolean placeSpecial) {
		byte foundation = usePlanks ? Block.Planks.val : Block.Cobblestone.val;

		//now need to clear out some space
		int x1 = x - 1, x2 = x + 1;
		int z1 = z - 1, z2 = z + 1;
		if (x1 < 0) x1 = 0;
		if (z1 < 0) z1 = 0;
		if (x2 > 15) x2 = 15;
		if (z2 > 15) z2 = 15;


		for (int xd = x1; xd <= x2; xd++) {
			for (int zd = z1; zd <= z2; zd++) {
				for (int h = railHeight; h < railHeight + 3; h++) {
					byte b = chunk.Blocks[h][zd][xd];
					if (b != Block.Rail.val && b != Block.Powered_Rail.val 
							&& b != Block.Redstone_Torch_Lit.val && b != Block.Cobblestone.val
							&& b != Block.Planks.val) {
						chunk.Blocks[h][zd][xd] = 0;
					}
				}
				//				//now add railing
				//				int h = railHeight;
				//				byte b = chunk.Blocks[h][zd][xd];
				//				if (b != Block.Rail.val && b != Block.Powered_Rail.val 
				//						&& b != Block.Redstone_Torch_Lit.val && b != Block.Cobblestone.val
				//						&& b != Block.Planks.val
				//						&& h <= chunk.getHeights(xd, zd)) {
				//					chunk.Blocks[h][zd][xd] = Block.Fence.val;
				//				}
			}

		}
		if (placeSpecial) {
			for (int h = railHeight + 5; h < railHeight + 10; h++) {
				chunk.Blocks[h][z][x] = Block.Gold_Block.val;
			}
		}
		if (spacesTillNextPoweredRail > 0)
			spacesTillNextPoweredRail--;
		boolean canBePlaced = railType == StraightRail.North.val || railType == StraightRail.East.val;

		if (spacesTillNextPoweredRail == 0 && canBePlaced) {
			if (railType == StraightRail.North.val) {
				chunk.Blocks[railHeight-1][z][x] = foundation;
				chunk.Blocks[railHeight][z][x] = Block.Powered_Rail.val;
				chunk.Data[railHeight][z][x] = (byte) (railType + 8);
				
				int xPosition = x == 15 ? 14 : x + 1;
				chunk.Blocks[railHeight-1][z][xPosition] = foundation;
				chunk.Blocks[railHeight][z][xPosition] = Block.Redstone_Torch_Lit.val;
				chunk.Data[railHeight][z][xPosition] = 0;
				spacesTillNextPoweredRail = 6;
			}
			if (railType == StraightRail.East.val) {
				chunk.Blocks[railHeight-1][z][x] = foundation;
				chunk.Blocks[railHeight][z][x] = Block.Powered_Rail.val;
				chunk.Data[railHeight][z][x] = (byte) (railType + 8);
				
				int zPosition = z == 15 ? 14 : z + 1;
				chunk.Blocks[railHeight-1][zPosition][x] = foundation;
				chunk.Blocks[railHeight][zPosition][x] = Block.Redstone_Torch_Lit.val;
				chunk.Data[railHeight][zPosition][x] = 0;
				spacesTillNextPoweredRail = 6;
			}
			
			
		} else {
			chunk.Blocks[railHeight-1][z][x] = foundation;
			chunk.Blocks[railHeight][z][x] = Block.Rail.val;
			chunk.Data[railHeight][z][x] = railType;
		}
	}

	public static void placeLibrary(Chunk chunk) {
		int x0 = 2, z0 = 1;
		int s = 0, n = 0;

		int width = 9, length = 12;

		//get height
		for (int z = z0; z < z0 + length; z++) {
			for (int x = x0; x < x0 + width; x++) {
				s += chunk.getHeights(x, z);
				n++;
			}
		}
		int h = s / n;
		if (h < 4) h = 4;

		//clear out
		for (int y = h; y < h + 7; y++) {
			for (int z = z0; z < z0 + length + 1; z++) {
				for (int x = x0; x < x0 + width; x++) {
					chunk.Blocks[y][z][x] = 0;
					chunk.Data[y][z][x] = 0;
				}
			}
		}

		//set base and walls
		int wallHeight = 5;

		for (int x = x0; x < x0 + width; x++) {
			for (int z = z0; z < z0 + length; z++) {
				//foundation
				for (int y = h - 3; y < h; y++)
					chunk.Blocks[y][z][x] = Block.Cobblestone.val;
				//floor
				//				chunk.Blocks[h][z][x] = Block.Sandstone.val;
				//				chunk.Data[h][z][x] = 2; //smooth
				chunk.Blocks[h][z][x] = Block.Block_Of_Quartz.val;

				if (x == x0 || z == z0 || x == x0 + width - 1) {
					//do wall
					for (int y = h + 1; y < h + wallHeight; y++) {
						chunk.Blocks[y][z][x] = Block.Wool.val;
						chunk.Data[y][z][x] = WoolColor.Brown.val;
					}
					chunk.Blocks[h + wallHeight][z][x] = Block.Nether_Brick.val;
				} else {
					//uppermost part of roof
					chunk.Blocks[h + wallHeight + 1][z][x] = Block.Nether_Brick.val;
				}
				//and also the roof
				//				chunk.Blocks[h + wallHeight][z][x] = Block.Nether_Brick.val;
			}
			//also foundation out front
			for (int y = h - 3; y < h; y++)
				chunk.Blocks[h][z0+length][x] = Block.Cobblestone.val;
		}
		//side windows
		makeSideWindow(chunk, h, z0 + 2, x0, width);
		makeSideWindow(chunk, h, z0 + 5, x0, width);
		makeSideWindow(chunk, h, z0 + 8, x0, width);

		//back windows
		makeBackWindow(chunk, h, z0, x0 + 2);
		makeBackWindow(chunk, h, z0, x0 + 5);

		//lighting

		chunk.Blocks[h+wallHeight][z0+4][x0+3] = Block.Glow_Stone.val;
		chunk.Blocks[h+wallHeight][z0+6][x0+3] = Block.Glow_Stone.val;
		chunk.Blocks[h+wallHeight][z0+8][x0+3] = Block.Glow_Stone.val;

		chunk.Blocks[h+wallHeight][z0+4][x0+5] = Block.Glow_Stone.val;
		chunk.Blocks[h+wallHeight][z0+6][x0+5] = Block.Glow_Stone.val;
		chunk.Blocks[h+wallHeight][z0+8][x0+5] = Block.Glow_Stone.val;

		//bookshelves
		for (int y = h + 1; y < h + 3; y++) {
			chunk.Blocks[y][z0+4][x0+1] = Block.Bookshelf.val;
			chunk.Blocks[y][z0+7][x0+1] = Block.Bookshelf.val;

			chunk.Blocks[y][z0+4][x0+7] = Block.Bookshelf.val;
			chunk.Blocks[y][z0+7][x0+7] = Block.Bookshelf.val;

			for (int z = z0 + 2; z < z0 + 9; z++) {
				chunk.Blocks[y][z][x0+3] = Block.Bookshelf.val;
				chunk.Blocks[y][z][x0+5] = Block.Bookshelf.val;
			}
		}
		//study chairs
		chunk.Blocks[h+1][z0+1][x0+1] = Block.Birch_Stairs.val;
		chunk.Data[h+1][z0+1][x0+1] = Stairs.Ascending_North.val;

		chunk.Blocks[h+1][z0+3][x0+1] = Block.Birch_Stairs.val;
		chunk.Data[h+1][z0+3][x0+1] = Stairs.Ascending_South.val;

		chunk.Blocks[h+1][z0+1][x0+7] = Block.Birch_Stairs.val;
		chunk.Data[h+1][z0+1][x0+7] = Stairs.Ascending_North.val;

		chunk.Blocks[h+1][z0+3][x0+7] = Block.Birch_Stairs.val;
		chunk.Data[h+1][z0+3][x0+7] = Stairs.Ascending_South.val;

		//tables
		chunk.Blocks[h+1][z0+2][x0+1] = Block.Nether_Fence.val;
		chunk.Blocks[h+2][z0+2][x0+1] = Block.Wooden_Pressure_Plate.val;

		chunk.Blocks[h+1][z0+2][x0+7] = Block.Nether_Fence.val;
		chunk.Blocks[h+2][z0+2][x0+7] = Block.Wooden_Pressure_Plate.val;

		//on to the facade
		//first place glass
		for (int y = h + 1; y < h + 6; y++) {
			for (int x = x0 + 1; x < x0 + width - 1; x++) {
				chunk.Blocks[y][z0 + length - 1][x] = Block.Window.val;
			}
		}
		//door frame
		for (int y = h + 1; y < h + 4; y++) {
			chunk.Blocks[y][z0+length-1][x0+3] = Block.Wool.val;
			chunk.Data[y][z0+length-1][x0+3] = 0;

			chunk.Blocks[y][z0+length-1][x0+5] = Block.Wool.val;
			chunk.Data[y][z0+length-1][x0+5] = 0;
		}
		//top panel
		for (int y = h + 4; y < h + 6; y++) {
			for (int x = x0 + 1; x < x0 + width - 1; x++) {
				if (x == x0 + 3 || x == x0 + 4 || x == x0 + 5)
					continue;
				chunk.Blocks[y][z0+length-1][x] = Block.Wool.val;
				chunk.Data[y][z0+length-1][x] = 0;
			}
		}
		//front stairs
		for (int x = x0; x < x0 + width; x++) {
			chunk.Blocks[h][z0+length][x] = Block.Stone_Stairs.val;
			chunk.Data[h][z0+length][x] = Stairs.Ascending_North.val;
		}
		//lastly the door

		chunk.Blocks[h+1][z0+length-1][x0+4] = 0;
		chunk.Blocks[h+2][z0+length-1][x0+4] = 0;
		//		
		//		chunk.Data[h+1][z0+length][x0+4] = 1;
		//		chunk.Data[h+2][z0+length][x0+4] = 8;

	}
	public static void makeSideWindow(Chunk chunk, int h, int z0, int x0, int width) {
		for (int y = h + 2; y < h + 5; y++) {
			for (int z = z0; z < z0 + 2; z++) {
				chunk.Blocks[y][z][x0] = Block.Window.val;
				chunk.Blocks[y][z][x0 + width - 1] = Block.Window.val;
			}
		}
	}
	public static void makeBackWindow(Chunk chunk, int h, int z0, int x0) {
		for (int y = h + 2; y < h + 5; y++) {
			for (int x = x0; x < x0 + 2; x++) {
				chunk.Blocks[y][z0][x] = Block.Window.val;
			}
		}
	}
	public static int getMeanHeight(Chunk chunk, int x0, int z0, int width, int length) {
		int s = 0, n = 0;
		for (int x = x0; x < x0 + width; x++) {
			for (int z = z0; z < z0 + length; z++) {
				s += chunk.getHeights(x, z);
				n++;
			}
		}
		return s/n;
	}
	public static void placePrison(Chunk chunk) {
		int x0 = 1, z0 = 1;
		int width = 10, length = 10;
		int h = getMeanHeight(chunk, x0, z0, width, length);
		for (int z = z0; z < z0 + length; z++) {
			for (int x = x0; x < x0 + width; x++) {
				int structureHeight = 1;
				if (z == z0 || z == z0 + length - 1
						|| x == x0 || x == x0 + width - 1)
					structureHeight = 5;
				for (int y = h; y < h + structureHeight; y++) {
					chunk.Blocks[y][z][x] = Blocka.Cobblestone;
				}
			}
		}
		//now an unopenable door
		int z = z0 + length - 1, x = x0 + width/2-1;
		chunk.Blocks[h+1][z][x] = Blocka.Metal_Door_Placed;
		chunk.Blocks[h+1][z][x+1] = Blocka.Metal_Door_Placed;
		chunk.Blocks[h+2][z][x] = Blocka.Metal_Door_Placed;
		chunk.Blocks[h+2][z][x+1] = Blocka.Metal_Door_Placed;

		chunk.Data[h+1][z][x] = 3;
		chunk.Data[h+1][z][x+1] = 3;
		chunk.Data[h+2][z][x] = 9;
		chunk.Data[h+2][z][x+1] = 8;

		//finally stairs
		chunk.Blocks[h][z+1][x] = Blocka.Stone_Stairs;
		chunk.Blocks[h][z+1][x+1] = Blocka.Stone_Stairs;
		chunk.Data[h][z+1][x] = Stairs.Ascending_North.val;
		chunk.Data[h][z+1][x+1] = Stairs.Ascending_North.val;
	}
	public static void placeMarket(Chunk chunk) {
		int x0 = 1, z0 = 1;
		int width = 8, length = 8;
		int h = getMeanHeight(chunk, x0, z0, width, length);

		for (int y = h; y < h + 2; y++) {
			for (int z = z0; z < z0 + length; z++) {
				if (z == z0 + 4)
					continue;

				chunk.Blocks[y][z][x0] = Blocka.Planks;
				chunk.Data[y][z][x0] = DataSource.Jungle.val;

				chunk.Blocks[y][z][x0+3] = Blocka.Planks;
				chunk.Data[y][z][x0+3] = DataSource.Jungle.val;

				chunk.Blocks[y][z][x0+5] = Blocka.Planks;
				chunk.Data[y][z][x0+5] = DataSource.Jungle.val;

				chunk.Blocks[y][z][x0+7] = Blocka.Planks;
				chunk.Data[y][z][x0+7] = DataSource.Jungle.val;
			}
		}
		//supports
		chunk.Blocks[h+2][z0][x0] = Blocka.Planks;
		chunk.Data[h+2][z0][x0] = DataSource.Jungle.val;

		chunk.Blocks[h+2][z0+length-1][x0] = Blocka.Planks;
		chunk.Data[h+2][z0+length-1][x0] = DataSource.Jungle.val;

		chunk.Blocks[h+2][z0][x0+width-1] = Blocka.Planks;
		chunk.Data[h+2][z0][x0+width-1] = DataSource.Jungle.val;

		chunk.Blocks[h+2][z0+length-1][x0+width-1] = Blocka.Planks;
		chunk.Data[h+2][z0+length-1][x0+width-1] = DataSource.Jungle.val;
		//add the roof
		for (int z = z0; z < z0 + length; z++) {
			for (int x = x0; x < x0 + width; x++) {
				chunk.Blocks[h+3][z][x] = Blocka.Wool;
				chunk.Data[h+3][z][x] = WoolColor.Brown.val;
			}
		}
	}
}
