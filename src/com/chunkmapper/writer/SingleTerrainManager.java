package com.chunkmapper.writer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.vecmath.Point3i;

import net.minecraft.world.level.chunk.storage.RegionFile;

import org.apache.commons.io.FileUtils;
import org.pepsoft.worldpainter.layers.bo2.Schematic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.column2.AbstractColumn;
import com.chunkmapper.column2.Shrubland;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.UniformHeightsReader;
import com.mojang.nbt.NbtIo;

public class SingleTerrainManager {
	public final Random RANDOM = new Random();
	private final HeightsReader heightsReader = new UniformHeightsReader();

	private final AbstractColumn[][] columns = new AbstractColumn[512][512];

	public SingleTerrainManager() {		
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				columns[i][j] = new Shrubland(j, i, heightsReader);
//				columns[i][j] = new Coast(j, i);
			}
		}

	}

	public Chunk getChunk(int abschunkx, int abschunkz) throws IOException {
		int relchunkx = abschunkx, relchunkz = abschunkz;
		int chunkx = Matthewmatics.mod(abschunkx, 32);
		int chunkz = Matthewmatics.mod(abschunkz, 32);
		Chunk chunk = new Chunk(abschunkx, abschunkz, heightsReader.getHeights(chunkx, chunkz), relchunkx, relchunkz);

		//add bedrock
		GenericWriter.addBedrock(chunk);
		//draw basic columns
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				AbstractColumn col = columns[i + chunkz*16][j + chunkx*16];
				col.addColumn(chunk);
			}
		}
		//now add trees
		int i1 = chunkz*16 - 20, i2 = chunkz*16 + 36;
		int j1 = chunkx*16 - 20, j2 = chunkx*16 + 36;
		if (i1 < 0) i1 = 0;
		if (j1 < 0) j1 = 0;
		for (int i = i1; i < i2; i++) {
			for (int j = j1; j < j2; j++) {
				AbstractColumn col = columns[i][j];
					col.addTree(chunk, heightsReader);
			}
		}
		return chunk;
	}
	public static void main(String[] args) throws Exception {
		makeWorld("test2");
	}
	private static void makeWorld(String name) throws IOException {

		File gameFolder = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/" + name);
		FileUtils.deleteDirectory(gameFolder);
		File regionFolder = new File(gameFolder, "region");
		regionFolder.mkdirs();
		File src = new File("resources/level.dat");
		File loadedLevelDatFile = new File(gameFolder, "level.dat");
		FileUtils.copyFile(src, loadedLevelDatFile);
		LoadedLevelDat levelDat = new LoadedLevelDat(loadedLevelDatFile);
		levelDat.setPlayerPosition(0, 10, 0);
		levelDat.setName(name);
		levelDat.save();
		
		
		RegionFile regionFile = new RegionFile(new File(regionFolder, "r.0.0.mca"));
		SingleTerrainManager manager = new SingleTerrainManager();
		
		for (int chunkx = 0; chunkx < 10; chunkx++) {
			for (int chunkz = 0; chunkz < 10; chunkz++) {
				Chunk chunk = manager.getChunk(chunkx, chunkz);
				DataOutputStream out = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), out);
				out.close();
			}
		}
		regionFile.close();
		System.out.println("done");
	}
	private static void getMaximumDimensions() throws IOException {
		//gets the maximum dimension of a lente tree
		File dir = new File("resources/trees");
		int x = 0, z = 0;
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".schematic")) {
				Schematic s = Schematic.load(f);
				Point3i d = s.getDimensions();
				if (d.x > x)
					x = d.x;
				if (d.z > z)
					z = d.z;
			}
		}
		System.out.println(x + ", " + z);
	}

}
