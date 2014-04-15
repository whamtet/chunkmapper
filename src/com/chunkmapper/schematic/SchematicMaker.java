package com.chunkmapper.schematic;

import java.io.DataInputStream;
import java.io.File;

import com.chunkmapper.Zip;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.process.NakedChunk;
import com.chunkmapper.protoc.SchematicProtocol;
import com.google.protobuf.ByteString;

public class SchematicMaker {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		int chunkx = 0, chunkz = 0;
		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/house/region/r.0.0.mca");
		RegionFile region = new RegionFile(f);
		DataInputStream in = region.getChunkDataInputStream(chunkx, chunkz);
		CompoundTag root = NbtIo.read(in);
		in.close();
		region.close();
		
		NakedChunk chunk = new NakedChunk(root);
		int x = 12, y = 2, z = 14;
		int size = x*y*z;
		byte[] blocks = new byte[size], data = new byte[size];
		int c = 0;
		int yOffset = 9;
		for (int yd = 0; yd < y; yd++) {
			for (int zd = 0; zd < z; zd++) {
				for (int xd = 0; xd < x; xd++) {
					blocks[c] = chunk.Blocks[yd+yOffset][zd][xd];
					data[c] = chunk.Data[yd+yOffset][zd][xd];
					c++;
				}
			}
		}
		SchematicProtocol.Schematic.Builder builder = SchematicProtocol.Schematic.newBuilder();
		builder.setX(x);
		builder.setY(y);
		builder.setZ(z);
		builder.setBlockData(ByteString.copyFrom(blocks));
		builder.setDataData(ByteString.copyFrom(data));
		
		File g = new File("chunkmapperResources/buildings/apartment-roof2.myschematic");
		Zip.zipOver(builder.build().toByteArray(), g);
		File h = new File("target/classes/buildings/apartment-roof2.myschematic");
		Zip.zipOver(builder.build().toByteArray(), h);
		System.out.println("done");
	}

}
