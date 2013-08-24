package com.chunkmapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.NbtIo;



public class Test {

	private static void spit(File f) throws IOException {
		InputStream in = new BufferedInputStream(new FileInputStream(f));
		CompoundTag root = NbtIo.readCompressed(in);
		in.close();
		File outf = new File(f.getAbsolutePath() + ".txt");
		PrintStream out = new PrintStream(outf);
		root.print(out);
		out.close();
	}
	public static void main(String[] args) throws Exception {
		File players = new File("/Users/matthewmolloy/Downloads/world/players");
		System.out.println(players.exists());
		for (File f : players.listFiles()) {
			if (f.getName().endsWith(".dat")) {
				spit(f);
			}
		}
		//also need to spit level.dat;
		File levelDat = new File("/Users/matthewmolloy/Downloads/world/level.dat");
		spit(levelDat);
		System.out.println("done");
	}

}
