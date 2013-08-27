package com.chunkmapper.multiplayer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.NbtIo;

public class MPLevelDat {
	public static void writeLevelDat(File parentFolder, String levelName) throws IOException {

		//check everything is going on this alright, after that, should be good
		CompoundTag root1 = new CompoundTag();

		CompoundTag Data1 = new CompoundTag();
		Data1.putByte("thundering", (byte) 0);
		Data1.putLong("LastPlayed", 1377251185404L);
		Data1.putLong("DayTime", 0L);
		Data1.putByte("initialized", (byte) 1);
		Data1.putLong("RandomSeed", 6074314354624945167L);
		Data1.putInt("GameType", 1); //Survival, creative, adventure
		Data1.putByte("MapFeatures", (byte) 1); //Structures?  1=true
		Data1.putInt("version", 19133);
		Data1.putByte("allowCommands", (byte) 1);
		Data1.putLong("Time", 91258L);
		Data1.putByte("raining", (byte) 0);
		Data1.putInt("thunderTime", 59749);
		Data1.putInt("SpawnX", 12);
		Data1.putInt("SpawnY", 64);
		Data1.putByte("hardcore", (byte) 0);
		Data1.putInt("SpawnZ", 256);
		Data1.putString("LevelName", levelName);
		Data1.putString("generatorOptions", ServerProperties.GENERATOR_SETTINGS);
		Data1.putLong("SizeOnDisk", 0L);
		Data1.putString("generatorName", "flat");

		CompoundTag GameRules1 = new CompoundTag();
		GameRules1.putString("doFireTick", "true");
		GameRules1.putString("doMobLoot", "true");
		GameRules1.putString("mobGriefing", "true");
		GameRules1.putString("doMobSpawning", "false");
		GameRules1.putString("doTileDrops", "true");
		GameRules1.putString("keepInventory", "false");
		GameRules1.putString("naturalRegeneration", "true");
		GameRules1.putString("commandBlockOutput", "true");
		GameRules1.putString("doDaylightCycle", "true");
		Data1.put("GameRules", GameRules1);

		Data1.putInt("generatorVersion", 1);
		Data1.putInt("rainTime", 44943);
		root1.put("Data", Data1);

		File f = new File(parentFolder, "level.dat");
		if (!f.exists()) {
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(f));
			NbtIo.writeCompressed(root1, out);
			out.close();
		}
	}}
