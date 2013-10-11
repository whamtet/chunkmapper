package com.chunkmapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.NbtIo;

public class Test {
	private static HashMap<Point, Point> map = new HashMap<Point, Point>();
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/nasserhorn/level.dat");
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		CompoundTag root = NbtIo.readCompressed(in);
		in.close();
		
		root.print(System.out);
	}
}
