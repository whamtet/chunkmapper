package com.chunkmapper.worldpainter;

import java.io.File;
import java.io.IOException;

import javax.vecmath.Point3i;

import org.pepsoft.minecraft.Material;
import org.pepsoft.worldpainter.layers.bo2.Schematic;

import com.chunkmapper.protoc.SchematicProtocol;
import com.chunkmapper.protoc.wrapper.SchematicProtocolWrapper;
import com.google.protobuf.ByteString;

public class SchematicToProtocolBuffer {

	private static SchematicProtocol.Schematic getProtocolBuffer(File f) throws IOException {
		Schematic s = Schematic.load(f);
		Point3i d = s.getDimensions();
		int size = d.y*d.z*d.x;
		byte[] rawBlocks = new byte[size], rawData = new byte[size];
		int i = 0;
		for (int y = 0; y < d.z; y++) {
			for (int z = 0; z < d.y; z++) {
				for (int x = 0; x < d.x; x++) {
					if (s.getMask(x, z, y)) {
						Material m = s.getMaterial(x, z, y);
						rawBlocks[i] = (byte) m.getBlockType();
						rawData[i] = (byte) m.getData();
						i++;
					} else {
						rawBlocks[i] = 0;
						rawData[i] = 0;
						i++;
					}
				}
			}
		}
		SchematicProtocol.Schematic.Builder builder = SchematicProtocol.Schematic.newBuilder();
		//note swapped order
		builder.setX(d.x);
		builder.setZ(d.y);
		builder.setY(d.z);
		builder.setBlockData(ByteString.copyFrom(rawBlocks));
		builder.setDataData(ByteString.copyFrom(rawData));
		return builder.build();
	}
	public static void main(String[] args) throws Exception {
		SchematicProtocol.Schematic s = getProtocolBuffer(new File("resources/trees/Douglas Fir.schematic"));
		SchematicProtocolWrapper wrapper = new SchematicProtocolWrapper(s);
		for (int z = 0; z < wrapper.zmax; z++) {
			for (int x = 0; x < wrapper.xmax; x++) {
				if (wrapper.blocks[0][z][x] != 0) {
					System.out.println("hi");
				}
			}
		}
	}
	private static void copyOver() throws IOException {
		File dir = new File("resources/trees");
		File outDir = new File("resources/trees2");
		outDir.mkdirs();
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".schematic")) {
				String name = f.getName().split("\\.")[0];
				System.out.println(name);
				SchematicProtocol.Schematic p = getProtocolBuffer(f);
//				Zip.zipOver(p.toByteArray(), new File(outDir, name));
			}
		}
		System.out.println("done");
	}

}
