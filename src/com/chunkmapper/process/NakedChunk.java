package com.chunkmapper.process;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.ListTag;

public class NakedChunk {
	
	public byte[][][] Data = new byte[256][16][16], Blocks = new byte[256][16][16];
	public NakedChunk(CompoundTag root) {
		CompoundTag Level = root.getCompound("Level");
		ListTag<CompoundTag> sections = (ListTag<CompoundTag>) Level.getList("Sections");
		for (CompoundTag section : sections.getList()) {
			byte y = section.getByte("Y");
			byte[] Blocks = section.getByteArray("Blocks");
			byte[] Data = section.getByteArray("Data");
			for (int i = 0; i < 4096; i++) {
				this.Blocks[i/256 + y*16][i/16%16][i%16] = Blocks[i];
			}
			for (int i = 0; i < 2048; i++) {
				byte datum = Data[i];
				this.Data[i/128 + y * 16][i/8%16][i*2%16] = (byte) (datum & 15);
				this.Data[i/128 + y * 16][i/8%16][i*2%16 + 1] = (byte) (datum >> 4 & 15);
			}
		}
	}

}
