package com.chunkmapper.protoc.wrapper;

import com.chunkmapper.protoc.SchematicProtocol;

public class SchematicProtocolWrapper {
	public final int xmax, ymax, zmax;
	public final byte[][][] data, blocks;
	public SchematicProtocolWrapper(SchematicProtocol.Schematic schematic) {
		xmax = schematic.getX();
		ymax = schematic.getY();
		zmax = schematic.getZ();
		int i = 0;
		data = new byte[ymax][zmax][xmax];
		blocks = new byte[ymax][zmax][xmax];
		byte[] rawData = schematic.getDataData().toByteArray(), rawBlocks = schematic.getBlockData().toByteArray();
		
		for (int y = 0; y < ymax; y++) {
			for (int z = 0; z < zmax; z++) {
				for (int x = 0; x < xmax; x++) {
					data[y][z][x] = rawData[i];
					blocks[y][z][x] = rawBlocks[i];
					i++;
				}
			}
		}
	}
}
