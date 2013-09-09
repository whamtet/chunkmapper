package com.chunkmapper.writer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.chunkmapper.Point;
import com.chunkmapper.nbt.ByteTag;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.FloatTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.nbt.NbtIo;

public class LevelDat {
	private final CompoundTag data;
	public final File store;

	public LevelDat(File loadedLevelDatFile) throws IOException {
		this.store = loadedLevelDatFile;
		//		CompoundTag data;
		//			InputStream in = new BufferedInputStream(new FileInputStream(store));
		//			data = NbtIo.readCompressed(in);
		//			in.close();
		this.data = getRoot();
	}
	public void save() throws IOException {
		File parent = store.getParentFile();
		parent.mkdirs();
		OutputStream out = new BufferedOutputStream(new FileOutputStream(store));
		NbtIo.writeCompressed(data, out);
		out.close();
	}
	public void setPlayerPosition(double x, double y, double z) {
		System.out.println("setting player position to " + x + ", " + y + ", " + z);
		CompoundTag Data = data.getCompound("Data");
		Data.putInt("SpawnX", (int) x);
		Data.putInt("SpawnY", (int) y);
		Data.putInt("SpawnZ", (int) z);
		CompoundTag Player = Data.getCompound("Player");
		Player.putInt("SpawnX", (int) x);
		Player.putInt("SpawnY", (int) y);
		Player.putInt("SpawnZ", (int) z);

		ListTag<DoubleTag> Pos = new ListTag<DoubleTag>("Pos");
		Pos.add(new DoubleTag("", x));
		Pos.add(new DoubleTag("", y));
		Pos.add(new DoubleTag("", z));
		Player.put("Pos", Pos);
	}
	public Point getPlayerPosition() {
		CompoundTag Data = data.getCompound("Data");
		CompoundTag Player = Data.getCompound("Player");
		ListTag<DoubleTag> Pos = (ListTag<DoubleTag>) Player.getList("Pos");
		double x = Pos.get(0).data;
		double z = Pos.get(2).data;
		return new Point((int) x, (int) z);
	}
	public void setName(String name) {
		System.out.println("setting game name to " + name);
		CompoundTag Data = data.getCompound("Data");
		Data.putString("LevelName", name);
	}
	public String getGameName() {
		CompoundTag Data = data.getCompound("Data");
		return Data.getString("LevelName");
	}
	private static CompoundTag getRoot() {
		CompoundTag root1 = new CompoundTag();

		CompoundTag Data1 = new CompoundTag();
		Data1.putByte("thundering", (byte) 0);
		Data1.putLong("DayTime", 1456L);
		Data1.putLong("LastPlayed", System.currentTimeMillis());

		CompoundTag Player1 = new CompoundTag();
		Player1.putInt("SelectedItemSlot", 0);

		ListTag<FloatTag> DropChances1 = new ListTag<FloatTag>();
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		Player1.put("DropChances", DropChances1);

		Player1.putLong("UUIDLeast", -4690821792555694401L);

		ListTag<DoubleTag> Motion1 = new ListTag<DoubleTag>();
		Motion1.add(new DoubleTag("", 0.0));
		Motion1.add(new DoubleTag("", 0.0));
		Motion1.add(new DoubleTag("", 0.0));
		Player1.put("Motion", Motion1);

		Player1.putString("CustomName", "");
		Player1.putFloat("foodExhaustionLevel", (float) 0.0);
		Player1.putInt("foodTickTimer", 0);
		Player1.putInt("XpLevel", 0);
		Player1.putShort("Health", (short) 20);

		ListTag<ByteTag> Inventory1 = new ListTag<ByteTag>();
		Player1.put("Inventory", Inventory1);

		Player1.putByte("CustomNameVisible", (byte) 0);
		Player1.putShort("AttackTime", (short) 0);
		Player1.putByte("Sleeping", (byte) 0);
		Player1.putShort("Fire", (short) -20);
		Player1.putInt("playerGameType", 1);
		Player1.putInt("foodLevel", 20);
		Player1.putInt("Score", 0);
		Player1.putByte("Invulnerable", (byte) 0);
		Player1.putShort("DeathTime", (short) 0);

		ListTag<ByteTag> EnderItems1 = new ListTag<ByteTag>();
		Player1.put("EnderItems", EnderItems1);

		Player1.putFloat("XpP", (float) 0.0);
		Player1.putShort("SleepTimer", (short) 0);

		ListTag<CompoundTag> Equipment1 = new ListTag<CompoundTag>();

		CompoundTag anon21 = new CompoundTag();
		Equipment1.add(anon21);


		CompoundTag anon24 = new CompoundTag();
		Equipment1.add(anon24);


		CompoundTag anon27 = new CompoundTag();
		Equipment1.add(anon27);


		CompoundTag anon30 = new CompoundTag();
		Equipment1.add(anon30);


		CompoundTag anon33 = new CompoundTag();
		Equipment1.add(anon33);

		Player1.put("Equipment", Equipment1);

		Player1.putByte("OnGround", (byte) 0);
		Player1.putShort("HurtTime", (short) 0);
		Player1.putLong("UUIDMost", 8633419034662685699L);
		Player1.putInt("Dimension", 0);
		Player1.putShort("Air", (short) 300);
		Player1.putByte("CanPickUpLoot", (byte) 0);

		ListTag<DoubleTag> Pos1 = new ListTag<DoubleTag>();
		Pos1.add(new DoubleTag("", -1362.3328758265166));
		Pos1.add(new DoubleTag("", 6.981310257287419));
		Pos1.add(new DoubleTag("", 304.7759157670519));
		Player1.put("Pos", Pos1);

		Player1.putInt("PortalCooldown", 0);
		Player1.putFloat("foodSaturationLevel", (float) 5.0);

		CompoundTag abilities1 = new CompoundTag();
		abilities1.putByte("flying", (byte) 1);
		abilities1.putByte("mayfly", (byte) 1);
		abilities1.putByte("instabuild", (byte) 1);
		abilities1.putByte("invulnerable", (byte) 1);
		abilities1.putByte("mayBuild", (byte) 1);
		abilities1.putFloat("flySpeed", (float) 0.05);
		abilities1.putFloat("walkSpeed", (float) 0.1);
		Player1.put("abilities", abilities1);

		Player1.putByte("PersistenceRequired", (byte) 0);
		Player1.putFloat("FallDistance", (float) 0.0);
		Player1.putInt("XpTotal", 0);

		ListTag<FloatTag> Rotation1 = new ListTag<FloatTag>();
		Rotation1.add(new FloatTag("", (float) 277.34998));
		Rotation1.add(new FloatTag("", (float) 13.950021));
		Player1.put("Rotation", Rotation1);

		Data1.put("Player", Player1);

		Data1.putByte("initialized", (byte) 1);
		Data1.putLong("RandomSeed", 6329512828970730748L);
		Data1.putInt("GameType", 1);
		Data1.putByte("MapFeatures", (byte) 0);
		Data1.putInt("version", 19133);
		Data1.putByte("allowCommands", (byte) 1);
		Data1.putLong("Time", 1456L);
		Data1.putByte("raining", (byte) 0);
		Data1.putInt("SpawnX", -1354);
		Data1.putInt("thunderTime", 48369);
		Data1.putInt("SpawnY", 4);
		Data1.putByte("hardcore", (byte) 0);
		Data1.putInt("SpawnZ", 307);
		Data1.putString("LevelName", "base");
		Data1.putString("generatorOptions", "2;7,12,8,8;1;village");
		Data1.putLong("SizeOnDisk", 0L);
		Data1.putString("generatorName", "flat");

		CompoundTag GameRules1 = new CompoundTag();
		GameRules1.putString("doFireTick", "true");
		GameRules1.putString("doMobLoot", "true");
		GameRules1.putString("mobGriefing", "true");
		GameRules1.putString("doMobSpawning", "false");
		GameRules1.putString("doTileDrops", "true");
		GameRules1.putString("keepInventory", "false");
		GameRules1.putString("commandBlockOutput", "true");
		Data1.put("GameRules", GameRules1);

		Data1.putInt("generatorVersion", 0);
		Data1.putInt("rainTime", 135506);
		root1.put("Data", Data1);
		return root1;

	}

}
