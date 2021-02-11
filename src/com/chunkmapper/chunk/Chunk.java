package com.chunkmapper.chunk;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import com.chunkmapper.admin.Utila;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.writer.BlockLighter;

public class Chunk {
	/*
	 * Low level class that contains Minecraft Chunks ready to be written to disc.
	 * Each chunk is a grid 16 x 16 x 256 blocks high.
	 */
	
	public final int xPos;
	public final int zPos;
	public final int abschunkx, abschunkz;
	public final int x0, z0, xr, zr;
	public static final Random RANDOM = new Random();
	public final Rectangle bbox;
	public long LastUpdate = 0;
	public byte TerrainPopulated = 1;
	public byte[] Biomes = new byte[256];
	public int[][] heights;
	public ListTag<CompoundTag> TileEntities = new ListTag<CompoundTag>(), Entities = new ListTag<CompoundTag>();

	public int[] HeightMap = new int[256];

	public byte[][][] Blocks = new byte[256][16][16];
	public byte[][][] Data = new byte[256][16][16];

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
		if (0 <= z && z < 16 && 0 <= x && x < 16 && y > 0)
			Blocks[y][z][x] = val;
	}
	
	public void setData(int y, int z, int x, byte val) {
		x -= x0; z -= z0;
		if (0 <= z && z < 16 && 0 <= x && x < 16 && y > 0)
			Data[y][z][x] = val;
	}
	public void setBoth(int y, int z, int x, byte block, byte data) {
		x -= x0; z -= z0;
		if (0 <= z && z < 16 && 0 <= x && x < 16 && y > 0) {
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
	
	public void setBiome(byte biome) {
		for (int i = 0; i < 256; i++) Biomes[i] = biome;
	}

	private static int[][] defaultHeights() {
		int len = Utila.CHUNK_START + Utila.CHUNK_END;
		int[][] out = new int[len][len];
		for (int i = 0; i < len; i++) {
			for(int j = 0; j < len; j++) {
				out[i][j] = 4;
			}
		}
		return out;
	}
	public Chunk(int abschunkx, int abschunkz, int relchunkx, int relchunkz) {
		this(abschunkx, abschunkz, defaultHeights(), relchunkx, relchunkz);
		//create an ocean Chunk
		for (int z = 0; z < 16; z++) {
			for (int x = 0; x < 16; x++) {
				Blocks[0][z][x] = Blocka.Bedrock;
				Blocks[1][z][x] = Blocka.Sand;
				Blocks[2][z][x] = Blocka.Water;
				Blocks[3][z][x] = Blocka.Water;
			}
		}
	}
	public Chunk(int abschunkx, int abschunkz, int[][] heights, int relchunkx, int relchunkz) {
		this.abschunkx = abschunkx; this.abschunkz = abschunkz;
		this.heights = heights;
		xPos = relchunkx;
		zPos = relchunkz;
		x0 = abschunkx*16;
		z0 = abschunkz*16;
		xr = relchunkx*16;
		zr = relchunkz*16;
		bbox = new Rectangle(x0, z0, 16, 16);
	}

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
			for (int i = 0; i < 2048; i++) {
				Data[i] = (byte) (this.Data[i/128 + y*16][i/8%16][i*2%16 + 1] << 4
						| this.Data[i/128 + y*16][i/8%16][i*2%16]);
				BlockLight[i] = (byte) (allBlockLights[i/128 + y*16][i/8%16][i*2%16 + 1] << 4
						| allBlockLights[i/128 + y*16][i/8%16][i*2%16]);
			}
			thisSection.putByte("Y", y);
			thisSection.putByteArray("Blocks", Blocks);
			thisSection.putByteArray("Data", Data);
			thisSection.putByteArray("BlockLight", BlockLight);
			thisSection.putByteArray("SkyLight", SkyLight);
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

	public void setBiome(int x, int z, byte biome) {
		Biomes[z*16+x] = biome;
	}

	public int getMaxBlock(int x, int z) {
		for (int y = 255; y >= 0; y--) {
			if (Blocks[y][z][x] != 0) {
				return y;
			}
		}
		return 0;
	}

}
