package com.chunkmapper.writer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.ManagingThread;
import com.chunkmapper.Zip;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.protoc.SchematicProtocol;
import com.chunkmapper.protoc.wrapper.SchematicProtocolWrapper;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.UniformHeightsReader;

public class LenteTreeWriter {
	private static final HashMap<LenteTree, SchematicProtocolWrapper> protocols =
			new HashMap<LenteTree, SchematicProtocolWrapper>();
	static {
		for (LenteTree lenteTree : LenteTree.values()) {
			try {
				URL src = ManagingThread.class.getResource("/images/" + lenteTree.toString() + ".myschematic");
				InputStream in = src.openStream();
				byte[] data = Zip.inflate(in);
				SchematicProtocol.Schematic p = SchematicProtocol.Schematic.parseFrom(data);
				protocols.put(lenteTree, new SchematicProtocolWrapper(p));
			} catch (Exception e) {
				System.out.println(lenteTree);
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception {

		System.out.println(protocols.size());
	}

	public static void placeLenteTree(int absx, int absz, Chunk chunk,
			HeightsReader heightsReader, LenteTree lenteTree) throws IOException {
		SchematicProtocolWrapper wrapper = protocols.get(lenteTree);

		int xmax = wrapper.xmax, ymax = wrapper.ymax, zmax = wrapper.zmax;
		int minHeight = Integer.MAX_VALUE;
		for (int x = 0; x < xmax; x++) {
			for (int z = 0; z < zmax; z++) {
				if (wrapper.blocks[0][z][x] != 0) {
					int h = heightsReader.getHeightxz(absx + x - xmax/2, absz + z - zmax/2);
					if (h < minHeight)
						minHeight = h;
				}
			}
		}
		if (minHeight == Integer.MAX_VALUE)
			throw new RuntimeException("no roots");

		for (int y = 0; y < ymax; y++) {
			for (int z = 0; z < zmax; z++) {
				for (int x = 0; x < xmax; x++) {
					if (wrapper.blocks[y][z][x] != 0) {
						chunk.setBlock(y + minHeight - lenteTree.rootDepth, z + absz - zmax/2, x + absx - xmax/2, wrapper.blocks[y][z][x]);
						chunk.setData(y + minHeight - lenteTree.rootDepth, z + absz - zmax/2, x + absx - xmax/2, wrapper.data[y][z][x]);
					}
				}
			}
		}
	}


}
