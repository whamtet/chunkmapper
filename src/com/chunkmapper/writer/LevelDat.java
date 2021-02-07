package com.chunkmapper.writer;

import java.io.*;

import com.chunkmapper.Point;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.gui.dialog.NewMapDialog.GameMode;
import com.chunkmapper.gui.dialog.NewMapDialog.NewGameInfo;
import com.chunkmapper.nbt.ByteTag;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.FloatTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.nbt.NbtIo;

public class LevelDat {
	private final CompoundTag data;
	public final File store;

	public LevelDat(File loadedLevelDatFile, NewGameInfo newGameInfo) throws IOException {
		this.store = loadedLevelDatFile;
		if (store.exists()) {
			InputStream in = new BufferedInputStream(new FileInputStream(store));
			data = NbtIo.readCompressed(in);
			in.close();
		} else {
			if (newGameInfo == null) {
				File parentFile = loadedLevelDatFile.getParentFile();
				newGameInfo = new NewGameInfo(parentFile.getName());
			}
			
			this.data = getRootTag(newGameInfo);
		}
	}
	public void save() {
		File parent = store.getParentFile();
		parent.mkdirs();
		try {
			OutputStream out = new BufferedOutputStream(new FileOutputStream(store));
			NbtIo.writeCompressed(data, out);
			out.close();
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
	}
	public void setPlayerPosition(double x, double y, double z) {
		MyLogger.LOGGER.info("setting player position to " + x + ", " + y + ", " + z);
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
		MyLogger.LOGGER.info(store.toString());
		MyLogger.LOGGER.info("***Pos***");
		for (int i = 0; i < 3; i++) {
			MyLogger.LOGGER.info(Pos.get(i).data + "");
		}
		MyLogger.LOGGER.info("***");
		double x = Pos.get(0).data;
		double z = Pos.get(2).data;
		return new Point((int) x, (int) z);
	}
	public void setName(String name) {
		MyLogger.LOGGER.info("setting game name to " + name);
		CompoundTag Data = data.getCompound("Data");
		Data.putString("LevelName", name);
	}
	public String getGameName() {
		CompoundTag Data = data.getCompound("Data");
		return Data.getString("LevelName");
	}
	private static CompoundTag getRootTag(NewGameInfo newGameInfo) {
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
		//Player1.putInt("playerGameType", 1);
		//replaced, see below
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
		Pos1.add(new DoubleTag("", 0));
		Pos1.add(new DoubleTag("", 255));
		Pos1.add(new DoubleTag("", 0));
		Player1.put("Pos", Pos1);

		Player1.putInt("PortalCooldown", 0);
		Player1.putFloat("foodSaturationLevel", (float) 5.0);

//	     walkSpeed: The walking speed, always 0.1.
//	     flySpeed: The flying speed, always 0.05.
//	     mayfly: 1 or 0 (true/false) - true if the player can fly.
//	     flying: 1 or 0 (true/false) - true if the player is currently flying.
//	     invulnerable: 1 or 0 (true/false) - true if the player is immune to all damage and harmful effects except for void damage. (damage caused by the /kill command is void damage)
//	     mayBuild: 1 or 0 (true/false) - true if the player can place and destroy blocks.
//	     instabuild: 1 or 0 (true/false) - true if the player can instantly destroy blocks.
		
		byte playerByte = (byte) (newGameInfo.gameMode == GameMode.Creative_Mode ? 1 : 0);
		
		CompoundTag abilities1 = new CompoundTag();
		abilities1.putByte("flying", (byte) playerByte);
		abilities1.putByte("mayfly", (byte) playerByte);
		abilities1.putByte("instabuild", (byte) playerByte);
		abilities1.putByte("invulnerable", (byte) playerByte);
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
		
		//0 is Survival Mode, 1 is Creative Mode, 2 is Adventure Mode
		int gameType;
		switch(newGameInfo.gameMode) {
		case Survival_Mode:
			gameType = 0; break;
		case Creative_Mode:
			gameType = 1; break;
		default:
			gameType = 2; break;
		}
		
		byte difficulty;
		switch(newGameInfo.difficulty) {
		case Peaceful:
			difficulty = 0; break;
		case Easy:
			difficulty = 1; break;
		case Normal:
			difficulty = 2; break;
		default:
			difficulty = 3; break;
		}
		byte difficultyLocked = (byte) (newGameInfo.gameMode == GameMode.Hardcore_Mode ? 1 : 0);
		
		Data1.putInt("GameType", gameType);
		//and also
		Player1.putInt("playerGameType", gameType);
		
		Data1.putByte("Difficulty", difficulty);
		Data1.putByte("DifficultyLocked", difficultyLocked);
		Data1.putByte("MapFeatures", (byte) 0);
		Data1.putInt("version", 19133);
		byte allowCommandsByte = (byte) (newGameInfo.hasCheats ? 1 : 0);
		Data1.putByte("allowCommands", allowCommandsByte);
		Data1.putLong("Time", 1456L);
		Data1.putByte("raining", (byte) 0);
		Data1.putInt("SpawnX", -1354);
		Data1.putInt("thunderTime", 48369);
		Data1.putInt("SpawnY", 4);
		byte hardcoreByte = (byte) (newGameInfo.gameMode == GameMode.Hardcore_Mode ? 1 : 0);
		Data1.putByte("hardcore", hardcoreByte);
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
	public static LevelDat getFromGameFolder(File gameFolder) throws IOException {
		return new LevelDat(new File(gameFolder, "level.dat"), null);
	}
	public void setPlayerPosition(double lat, double lon, Point rootPoint) {
		double x = lon * 3600 - rootPoint.x * 512;
		double y = 250;
		double z = lat * 3600 - rootPoint.z * 512;
		this.setPlayerPosition(x, y, z);
	}

}
