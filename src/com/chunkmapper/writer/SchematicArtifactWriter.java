package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.BlockColor;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.Villager;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.protoc.wrapper.SchematicProtocolWrapper;

public class SchematicArtifactWriter {
	private static SchematicProtocolWrapper hut, apartmentFloor, apartmentFloor2;
	private static SchematicProtocolWrapper[] apartmentRoofs = new SchematicProtocolWrapper[3];
	private static final Random RANDOM = new Random();
	static {
		try {
			hut = new SchematicProtocolWrapper("/buildings/hut.myschematic");
			apartmentFloor = new SchematicProtocolWrapper("/buildings/apartment-floor.myschematic");
			apartmentFloor2 = new SchematicProtocolWrapper("/buildings/apartment-floor2.myschematic");
			for (int i = 0; i < apartmentRoofs.length; i++) {
				apartmentRoofs[i] = new SchematicProtocolWrapper(String.format("/buildings/apartment-roof%s.myschematic", i));
			}

		} catch (Exception e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	public static void addHut(Chunk chunk, String name) {
		int h = ArtifactWriter.getMeanHeight(chunk);
		for (int y = 0; y < hut.ymax; y++) {
			for (int z = 0; z < hut.zmax; z++) {
				for (int x = 0; x < hut.xmax; x++) {
					chunk.Blocks[y+h][z][x] = hut.blocks[y][z][x];
					chunk.Data[y+h][z][x] = hut.data[y][z][x];
				}
			}
		}
		for (int y = h + hut.ymax; y < 256; y++) {
			for (int z = 0; z < hut.zmax; z++) {
				for (int x = 0; x < hut.xmax; x++) {
					chunk.Blocks[y][z][x] = 0;
				}
			}
		}
		//add hut name
		if (name != null) {
			ArtifactWriter.addSign(chunk, h, 2, 2, name.split(" "), (byte) 0);
		}
	}
	//	  TAG_Compound: 6 entries
	//      {
	//         TAG_String("id"): FlowerPot
	//         TAG_Int("Data"): 0
	//         TAG_Int("Item"): 6
	//         TAG_Int("z"): 19
	//         TAG_Int("y"): 10
	//         TAG_Int("x"): 19
	//      }
	private static CompoundTag getFlowerPot(int x, int y, int z) {
		CompoundTag out = new CompoundTag();
		out.putString("id", "FlowerPot");
		out.putInt("Data", 0);
		out.putInt("Item", 6);
		out.putInt("x", x);
		out.putInt("y", y);
		out.putInt("z", z);

		return out;
	}
	private static <A> A randomNth(A[] s) {
		return s[RANDOM.nextInt(s.length)];
	}
	private static void addFunkyRoof(Chunk chunk, int h) {
		SchematicProtocolWrapper apartmentFloor = apartmentRoofs[1];
		for (int z = 0; z < apartmentFloor.zmax; z++) {
			for (int x = 0; x < apartmentFloor.xmax; x++) {

				chunk.Blocks[h][z][x] = apartmentFloor.blocks[0][z][x];
				chunk.Data[h][z][x] = apartmentFloor.data[0][z][x];

				if (chunk.Blocks[h][z][x] == Blocka.Block_Of_Quartz) {
					if (RANDOM.nextInt(2) == 0) {
						chunk.Blocks[h+1][z][x] = Blocka.Flower_Pot;
						chunk.TileEntities.add(FlowerpotWriter.getRandomFlowerpot(x, h+1, z));
					} else if (RANDOM.nextInt(40) == 0) {
						chunk.Blocks[h+1][z][x] = Blocka.Juke_Box;
					}
				}

			}
		}
	}
	public static void addApartment(Chunk chunk, int numFloors) {
		int h = ArtifactWriter.getMeanHeight(chunk);
		Byte[] possibleColors = {BlockColor.White.val, BlockColor.Gray.val, BlockColor.Light_Gray.val, BlockColor.Cyan.val, BlockColor.Red.val, BlockColor.Black.val};
		
		byte houseColor = randomNth(possibleColors);
		boolean gayHouse = RANDOM.nextInt(1000) == 0;
		for (int floor = 0; floor < numFloors; floor++) {
			SchematicProtocolWrapper apartmentFloor = floor % 2 == 1 ?
					SchematicArtifactWriter.apartmentFloor : SchematicArtifactWriter.apartmentFloor2;
			for (int y = 0; y < apartmentFloor.ymax; y++) {
				byte levelColor = gayHouse ? BlockColor.cyclicColor(y + h) : houseColor;
				for (int z = 0; z < apartmentFloor.zmax; z++) {
					for (int x = 0; x < apartmentFloor.xmax; x++) {
						chunk.Blocks[y+h][z][x] = apartmentFloor.blocks[y][z][x];
						chunk.Data[y+h][z][x] = apartmentFloor.data[y][z][x];
						
						if (chunk.Blocks[y+h][z][x] == Blocka.Stained_Clay) {
							chunk.Data[y+h][z][x] = levelColor;
						}
					}
				}
			}
			//finally add a couple of occupants
			int numChildren = RANDOM.nextInt(5);
			int numParents = RANDOM.nextInt(3);
			for (int i = 0; i < numChildren; i++) {
				double x = 3 + 6 * RANDOM.nextDouble();
				double z = 3 + 8 * RANDOM.nextDouble();
				double y = h + 1;
				MobWriter.addVillager(chunk, Villager.Farmer, x + chunk.xr, y, z + chunk.zr, true);
			}
			for (int i = 0; i < numParents; i++) {
				double x = 3 + 6 * RANDOM.nextDouble();
				double z = 3 + 8 * RANDOM.nextDouble();
				double y = h + 1;
				MobWriter.addVillager(chunk, Villager.Farmer, x + chunk.xr, y, z + chunk.zr, false);
			}
			h += 5;
		}

		//roof
		if (RANDOM.nextInt(2) == 0) {
			addFunkyRoof(chunk, h);
		} else {
			SchematicProtocolWrapper apartmentFloor = randomNth(apartmentRoofs);
			for (int y = 0; y < apartmentFloor.ymax; y++) {
				for (int z = 0; z < apartmentFloor.zmax; z++) {
					for (int x = 0; x < apartmentFloor.xmax; x++) {

						chunk.Blocks[y+h][z][x] = apartmentFloor.blocks[y][z][x];
						chunk.Data[y+h][z][x] = apartmentFloor.data[y][z][x];

						if (apartmentFloor.blocks[y][z][x] == Blocka.Flower_Pot) {
							chunk.TileEntities.add(getFlowerPot(x + chunk.xr, y+h, z + chunk.zr));
						}
					}
				}
			}
		}
		for (int y = h + 2; y < 256; y++) {
			for (int z = 0; z < apartmentFloor.zmax; z++) {
				for (int x = 0; x < apartmentFloor.xmax; x++) {
					chunk.Blocks[y][z][x] = 0;
				}
			}
		}
	}



}
