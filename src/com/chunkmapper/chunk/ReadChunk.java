package com.chunkmapper.chunk;

import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.ListTag;

public class ReadChunk {
	public byte[][][] data = new byte[256][16][16], blocks = new byte[256][16][16];
	private CompoundTag root;
	public ReadChunk(CompoundTag root) {
		this.root = root;
		CompoundTag Level = root.getCompound("Level");
		ListTag<CompoundTag> Sections = (ListTag<CompoundTag>) Level.getList("Sections");
		for (int i = 0; i < Sections.size(); i++) {
			CompoundTag section = Sections.get(i);
			int Y = section.getByte("Y");
			byte[] Data = section.getByteArray("Data");
			byte[] Blocks = section.getByteArray("Blocks");
			for (int j = 0; j < 4096; j += 2) {
				int y = Y * 16 + j / 256;
				int z = (j / 16) % 16;
				int x = j % 16;
				
				blocks[y][z][x] = Blocks[j];
				blocks[y][z][x+1] = Blocks[j+1];
				data[y][z][x] = (byte) (Data[j/2] & 15);
				data[y][z][x+1] = (byte) (Data[j/2] >> 4);
			}
		}
	}
	
	public CompoundTag getRoot() {
		//just need to update sections
		CompoundTag Level = root.getCompound("Level");
		ListTag<CompoundTag> Sections = new ListTag<CompoundTag>();
		byte[] empty = new byte[2048];
		for (int i = 0; i < 2048; i++) {
			empty[i] = (byte) 0xFF;
		}
		
		for (byte Y = 0; Y < 16; Y++) {
			byte[] Data = new byte[2048];
			byte[] Blocks = new byte[4096];
			boolean hasBlock = false;
			for (int j = 0; j < 4096; j += 2) {
				int y = Y * 16 + j / 256;
				int z = (j / 16) % 16;
				int x = j % 16;
				
				Data[j/2] = (byte) (data[y][z][x+1] << 4 | data[y][z][x]);
				Blocks[j] = blocks[y][z][x];
				Blocks[j+1] = blocks[y][z][x+1];
				hasBlock |= Blocks[j] != 0 || Blocks[j+1] != 0;
			}
			if (hasBlock) {
				CompoundTag section = new CompoundTag();
				section.putByteArray("Data", Data);
				section.putByteArray("SkyLight", empty);
				section.putByteArray("BlockLight", empty);
				section.putByteArray("Blocks", Blocks);
				section.putByte("Y", Y);
				Sections.add(section);
			}
		}
		Level.put("Sections", Sections);
		return root;
	}

}
