package com.chunkmapper.writer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.pepsoft.worldpainter.layers.bo2.Schematic;

import com.chunkmapper.Zip;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.LenteTree;
import com.chunkmapper.protoc.SchematicProtocol;
import com.chunkmapper.protoc.wrapper.SchematicProtocolWrapper;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.UniformHeightsReader;

public class LenteTreeWriter {
	private static final ConcurrentHashMap<LenteTree, Schematic> schematics = new ConcurrentHashMap<LenteTree, Schematic>();
	private static final HashMap<LenteTree, SchematicProtocolWrapper> protocols =
			new HashMap<LenteTree, SchematicProtocolWrapper>();
	static {
		for (LenteTree lenteTree : LenteTree.values()) {
			String name = lenteTree.toString().replace("_", " ");
			File f = new File("/Users/matthewmolloy/workspace/chunkmapper2/resources/trees2/" + name);
			try {
			byte[] data = Zip.inflate(f);
			SchematicProtocol.Schematic p = SchematicProtocol.Schematic.parseFrom(data);
			protocols.put(lenteTree, new SchematicProtocolWrapper(p));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws Exception {

		Chunk chunk = new Chunk();
		LenteTree l = LenteTree.randomTree(LenteTree.values());
		HeightsReader heightsReader = new UniformHeightsReader();
		placeLenteTree(0, 0, chunk, heightsReader, l);
	}

	public static void placeLenteTree(int absx, int absz, Chunk chunk,
			HeightsReader heightsReader, LenteTree lenteTree) throws IOException {
		SchematicProtocolWrapper wrapper = protocols.get(lenteTree);
//		Schematic s;
//		if (schematics.contains(lenteTree)) {
//			s = schematics.get(lenteTree);
//		} else {
//			String name = lenteTree.toString().replace("_", " ");
//			File f = new File("/Users/matthewmolloy/workspace/chunkmapper2/resources/trees/" + name + ".schematic");
//			s = Schematic.load(f);
//			schematics.put(lenteTree, s);
//		}
//		Point3i d = s.getDimensions();
		int xmax = wrapper.xmax, ymax = wrapper.ymax, zmax = wrapper.zmax;
		int minHeight = Integer.MAX_VALUE;
		for (int x = 0; x < xmax; x++) {
			for (int z = 0; z < zmax; z++) {
				if (wrapper.blocks[0][z][x] != 0) {
					int h = heightsReader.getHeightxz(absx + x, absz + z);
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
