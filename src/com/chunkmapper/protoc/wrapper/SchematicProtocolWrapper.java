package com.chunkmapper.protoc.wrapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import com.chunkmapper.Zip;
import com.chunkmapper.protoc.SchematicProtocol;

public class SchematicProtocolWrapper {
	public final int xmax, ymax, zmax;
	public final byte[][][] data, blocks;
	private static SchematicProtocol.Schematic getSchematic(File f) throws IOException, DataFormatException {
		byte[] data = Zip.inflate(f);
		return SchematicProtocol.Schematic.parseFrom(data);
	}
	private static SchematicProtocol.Schematic getSchematic(InputStream in) throws IOException, DataFormatException {
		byte[] data = Zip.inflate(in);
		return SchematicProtocol.Schematic.parseFrom(data);
	}
	public SchematicProtocolWrapper(File f) throws IOException, DataFormatException {
		this(getSchematic(f));
	}
	public SchematicProtocolWrapper(String s) throws IOException, DataFormatException {
		this(getSchematic(SchematicProtocolWrapper.class.getResource(s).openStream()));
	}
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
	public void stainBlock(byte targetBlock, byte targetData) {
		for (int y = 0; y < ymax; y++) {
			for (int z = 0; z < zmax; z++) {
				for (int x = 0; x < xmax; x++) {
					if (blocks[y][z][x] == targetBlock) {
						data[y][z][x] = targetData;
					}
				}
			}
		}
	}
	public static void main(String[] args) throws Exception {
		SchematicProtocolWrapper wrapper = new SchematicProtocolWrapper("/buildings/hut.myschematic");
		System.out.println(wrapper.xmax + ", " + wrapper.zmax + ", " + wrapper.ymax);
	}
}
