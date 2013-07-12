package com.chunkmapper.chunk;

import java.awt.Point;
import java.util.Random;

import com.chunkmapper.Utila;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.StraightRail;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.writer.BlockLighter;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;

public class Chunk {
	public final int xPos;
	public final int zPos;
	public final int abschunkx, abschunkz;
	public final int x0, z0, xr, zr;
	public static final Random RANDOM = new Random();
	public long LastUpdate = 0;
	public byte TerrainPopulated = 1;
	public byte[] Biomes = new byte[256];
	public int[][] heights;
	private static int spacesTillNextPoweredRail = 1;
	public ListTag<CompoundTag> TileEntities = new ListTag<CompoundTag>(), Entities = new ListTag<CompoundTag>();

	public int[] HeightMap = new int[256];

	public byte[][][] Blocks = new byte[256][16][16];
	public byte[][][] Data = new byte[256][16][16];
//	public byte[][][] Add = new byte[256][16][16];

//	public Chunk(DataInputStream in) throws IOException {
//		this(NbtIo.read(in).getCompound("Level"));
//		in.close();
//	}

	public void writeOceanColumn(int x, int z) {
		Blocks[0][z][x] = Block.Bedrock.val;
		Blocks[1][z][x] = Block.Sand.val;
		Blocks[2][z][x] = Block.Water.val;
		Blocks[3][z][x] = Block.Water.val;
	}

	public int getHeights(int x, int z) {
		try {
			return heights[x + Utila.CHUNK_START][z + Utila.CHUNK_START];
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1;
		}
	}
	public void setBlock(int y, int z, int x, byte val) {
		x -= x0; z -= z0;
		x = Matthewmatics.mod(x, 512);
		z = Matthewmatics.mod(z, 512);
		if (0 <= z && z < 16 && 0 <= x && x < 16)
			Blocks[y][z][x] = val;
	}
	
	public void setData(int y, int z, int x, byte val) {
		x -= x0; z -= z0;
		x = Matthewmatics.mod(x, 512);
		z = Matthewmatics.mod(z, 512);
		if (0 <= z && z < 16 && 0 <= x && x < 16)
			Data[y][z][x] = val;
	}
	public void setBoth(int y, int z, int x, byte block, byte data) {
		x -= x0; z -= z0;
		if (0 <= z && z < 16 && 0 <= x && x < 16) {
			Blocks[y][z][x] = block;
			Data[y][z][x] = data;
		}
	}
	public byte getBlock(int y, int z, int x) {
		x -= x0; z -= z0;
		if (0 <= z && z < 16 && 0 <= x && x < 16)
			return Blocks[y][z][x];
		return -1;
	}
	public byte getData(int y, int z, int x) {
		x -= x0; z -= z0;
		if (0 <= z && z < 16 && 0 <= x && x < 16)
			return Blocks[y][z][x];
		return -1;
	}

//	public Chunk(CompoundTag Level) throws IOException {
//		xPos = Level.getInt("xPos");
//		zPos = Level.getInt("zPos");
//		LastUpdate = Level.getLong("LastUpdate");
//		TerrainPopulated = Level.getByte("TerrainPopulated");
//		Biomes = Level.getByteArray("Biomes");
//		HeightMap = Level.getIntArray("HeightMap");
//
//		ListTag<CompoundTag> Sections = (ListTag<CompoundTag>) Level.getList("Sections");
//		for (int i = 0; i < Sections.size(); i++) {
//			CompoundTag thisSection = Sections.get(i);
//			Byte Y = thisSection.getByte("Y");
//			byte[] Blocks = thisSection.getByteArray("Blocks");
//			byte[] Data = thisSection.getByteArray("Data");
//			for (int j = 0; j < 4096; j += 2) {
//				int y = j >> 8;
//			int z = j/16%16;
//			int x = j%16;
//
//			this.Data[y + Y*16][z][x] = (byte) (Data[j>>1] & 0x0f);
//			this.Data[y + Y*16][z][x + 1] = (byte) (Data[j>>1]>>4 & 0x0f);
//			this.Blocks[y + Y*16][z][x] = Blocks[j];
//			this.Blocks[y + Y*16][z][x + 1]  = Blocks[j+1];
//			}
//
//		}
//	}
	public void setBiome(byte biome) {
		for (int i = 0; i < 256; i++) Biomes[i] = biome;
	}

//	public Chunk(int x, int z, byte biome) {
//		this(x, z);
//		for (int i = 0; i < 256; i++) {
//			Biomes[i] = biome;
//		}
//	}
//
//	public Chunk(Point p) {
//		this(p.x, p.y);
//	}
	public Chunk(int abschunkx, int abschunkz, int[][] heights, int relchunkx, int relchunkz) {
		this.abschunkx = abschunkx; this.abschunkz = abschunkz;
		this.heights = heights;
		xPos = relchunkx;
		zPos = relchunkz;
		x0 = abschunkx*16;
		z0 = abschunkz*16;
		xr = relchunkx*16;
		zr = relchunkz*16;
	}


//	public Chunk(int[][] heights2) {
//		this.heights = heights2;
//	}

	public Point getPoint() {
		return new Point(xPos, zPos);
	}

	public CompoundTag getTag() {
		CompoundTag Level = new CompoundTag("Level");
		Level.putInt("xPos", xPos);
		Level.putInt("zPos", zPos);
		Level.putLong("LastUpdate", LastUpdate);
		Level.putByte("TerrainPopulated", TerrainPopulated);
		Level.putByteArray("Biomes", Biomes);
		Level.putIntArray("HeightMap", HeightMap);

		ListTag Sections = new ListTag();
//		byte[] BlockLight = new byte[2048];
		byte[][][] allBlockLights = BlockLighter.getBlockLights(this.Blocks);
		byte[] SkyLight = new byte[2048];
		for (int i = 0; i < 2048; i++) {
			SkyLight[i] = (byte) 0xFF;
		}
		for (byte y = 0; y < 16; y++) {
			CompoundTag thisSection = new CompoundTag();
			byte[] Blocks = new byte[4096];
			boolean allEmpty = true;
			for (int i = 0; i < 4096; i++) {
				byte t = this.Blocks[i/256 + y*16][i/16%16][i%16];
				Blocks[i] = t;
				allEmpty &= t == 0;
				
			}
			if (allEmpty) continue;
			byte[] Data = new byte[2048];
			byte[] BlockLight = new byte[2048];
//			byte[] Add = new byte[2048];
			for (int i = 0; i < 2048; i++) {
				Data[i] = (byte) (this.Data[i/128 + y*16][i/8%16][i*2%16 + 1] << 4
						| this.Data[i/128 + y*16][i/8%16][i*2%16]);
				BlockLight[i] = (byte) (allBlockLights[i/128 + y*16][i/8%16][i*2%16 + 1] << 4
						| allBlockLights[i/128 + y*16][i/8%16][i*2%16]);
//				Add[i] = (byte) (this.Add[i/128 + y*16][i/8%16][i*2%16 + 1] << 4
//						| this.Add[i/128 + y*16][i/8%16][i*2%16]);
			}
			thisSection.putByte("Y", y);
			thisSection.putByteArray("Blocks", Blocks);
			thisSection.putByteArray("Data", Data);
			thisSection.putByteArray("BlockLight", BlockLight);
			thisSection.putByteArray("SkyLight", SkyLight);
//			thisSection.putByteArray("Add", Add);
			Sections.add(thisSection);
		}
		Level.put("Sections", Sections);
		Level.put("Entities", this.Entities);
		Level.put("TileEntities", this.TileEntities);

		CompoundTag root = new CompoundTag();
		root.putCompound("Level", Level);
		return root;
	}

	public void placeRiverWater(int x, int z) {
		int h = this.getHeights(x, z);
		Blocks[h-2][z][x] = RANDOM.nextInt(2) == 0 ? Block.Clay.val : Block.Gravel.val;
		Blocks[h-1][z][x] = Block.Water.val;

	}

	public boolean placeLakeWater(int x, int z, short lakeWaterHeight) {
		int h = this.getHeights(x, z);
		if (h > lakeWaterHeight) return false;
		byte lakeBed = 0;
		switch (RANDOM.nextInt(3)) {
		case 0:
			lakeBed = Block.Sand.val;
			break;
		case 1:
			lakeBed = Block.Gravel.val;
			break;
		case 2:
			lakeBed = Block.Clay.val;
			break;
		}
		Blocks[h-3][z][x] = lakeBed;
		Blocks[h-2][z][x] = Block.Water.val;
		Blocks[h-1][z][x] = Block.Water.val;
		return true;
	}

	public void placeRail(int x, int z, short railHeight, byte railType, boolean overOcean,
			boolean placeSpecial) {
		byte foundation = overOcean ? Block.Planks.val : Block.Cobblestone.val;
		
		//now need to clear out some space
		int x1 = x - 1, x2 = x + 1;
		int z1 = z - 1, z2 = z + 1;
		if (x1 < 0) x1 = 0;
		if (z1 < 0) z1 = 0;
		if (x2 > 15) x2 = 15;
		if (z2 > 15) z2 = 15;
		
		for (int h = railHeight; h < railHeight + 3; h++) {
			for (int xd = x1; xd <= x2; xd++) {
				for (int zd = z1; zd <= z2; zd++) {
					byte b = Blocks[h][zd][xd];
					if (b != Block.Rail.val && b != Block.Powered_Rail.val 
							&& b != Block.Redstone_Torch_Lit.val && b != Block.Cobblestone.val
							&& b != Block.Planks.val) {
						Blocks[h][zd][xd] = 0;
					}
				}
			}
		}
		if (placeSpecial) {
			for (int h = railHeight + 5; h < railHeight + 10; h++) {
				Blocks[h][z][x] = Block.Gold_Block.val;
			}
		}
		if (railType == StraightRail.North.val || railType == StraightRail.East.val)
			spacesTillNextPoweredRail--;
		
		if (spacesTillNextPoweredRail == 0) {
			spacesTillNextPoweredRail = 6;
			
			Blocks[railHeight-1][z][x] = foundation;
			Blocks[railHeight][z][x] = Block.Powered_Rail.val;
			Data[railHeight][z][x] = (byte) (railType + 8);
			
			if (railType == StraightRail.North.val){
				int xPosition = x == 15 ? 14 : x + 1;
				Blocks[railHeight-1][z][xPosition] = foundation;
				Blocks[railHeight][z][xPosition] = Block.Redstone_Torch_Lit.val;
				Data[railHeight][z][xPosition] = 0;
			} else {
				int zPosition = z == 15 ? 14 : z + 1;
				Blocks[railHeight-1][zPosition][x] = foundation;
				Blocks[railHeight][zPosition][x] = Block.Redstone_Torch_Lit.val;
				Data[railHeight][zPosition][x] = 0;
			}
		} else {
			Blocks[railHeight-1][z][x] = foundation;
			Blocks[railHeight][z][x] = Block.Rail.val;
			Data[railHeight][z][x] = railType;
		}
	}

}
