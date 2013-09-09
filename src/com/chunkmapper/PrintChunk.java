package com.chunkmapper;

import java.io.DataInputStream;
import java.io.File;
import java.io.PrintStream;


import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;

public class PrintChunk {

	public static void main(String[] args) throws Exception {
		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/house");
		File regionFolder = new File(gameFolder, "region");
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		DataInputStream in = regionFile.getChunkDataInputStream(1, 0);
		CompoundTag root = NbtIo.read(in);
		in.close();
		regionFile.close();
		
		PrintStream out = new PrintStream("horse.txt");
		root.print(out);
		out.close();
		
		System.out.println("done");
		
	}
}
