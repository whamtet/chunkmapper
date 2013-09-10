package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.FloatTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.reader.NameReader;

public class MobWriter {
	public static final Random RANDOM = new Random();
	private static void shit() {
		CompoundTag c = new CompoundTag();
		//		:TAG_End :TAG_Byte :TAG_Short :TAG_Int :TAG_Long
		//        :TAG_Float :TAG_Double :TAG_Byte_Array :TAG_String
		//        :TAG_List :TAG_Compound :TAG_Int_Array])
		ListTag l;
	}
	public static void addVillager(Chunk chunk, int profession, double x, double y, double z, boolean isChild) {

		CompoundTag anon4 = new CompoundTag();

		ListTag<FloatTag> DropChances1 = new ListTag<FloatTag>();
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		DropChances1.add(new FloatTag("", (float) 0.085));
		anon4.put("DropChances", DropChances1);


		ListTag<CompoundTag> Attributes1 = new ListTag<CompoundTag>();

		CompoundTag anon14 = new CompoundTag();
		anon14.putString("Name", "generic.maxHealth");
		anon14.putDouble("Base", 20.0);
		Attributes1.add(anon14);


		CompoundTag anon17 = new CompoundTag();
		anon17.putString("Name", "generic.knockbackResistance");
		anon17.putDouble("Base", 0.0);
		Attributes1.add(anon17);


		CompoundTag anon20 = new CompoundTag();
		anon20.putString("Name", "generic.movementSpeed");
		anon20.putDouble("Base", 0.5);
		Attributes1.add(anon20);


		CompoundTag anon23 = new CompoundTag();
		anon23.putString("Name", "generic.followRange");
		anon23.putDouble("Base", 16.0);

		ListTag<CompoundTag> Modifiers1 = new ListTag<CompoundTag>();

		CompoundTag anon26 = new CompoundTag();
		anon26.putString("Name", "Random spawn bonus");
		anon26.putDouble("Amount", -0.011943203854364171);
		anon26.putInt("Operation", 1);
		anon26.putLong("UUIDLeast", -5682554311724075710L);
		anon26.putLong("UUIDMost", 5260858663443319483L);
		Modifiers1.add(anon26);

		anon23.put("Modifiers", Modifiers1);

		Attributes1.add(anon23);

		anon4.put("Attributes", Attributes1);

		anon4.putLong("UUIDLeast", -6310649377590585080L);
		anon4.putInt("Age", isChild ? -1000000000 : 0);

		ListTag<DoubleTag> Motion1 = new ListTag<DoubleTag>();
		Motion1.add(new DoubleTag("", 0.0));
		Motion1.add(new DoubleTag("", -0.0784000015258789));
		Motion1.add(new DoubleTag("", 0.0));
		anon4.put("Motion", Motion1);

		anon4.putString("CustomName", "");
		anon4.putShort("Health", (short) 20);
		anon4.putFloat("HealF", (float) 20.0);
		anon4.putByte("CustomNameVisible", (byte) 0);
		anon4.putInt("Riches", 0);
		anon4.putString("id", "Villager");
		anon4.putShort("AttackTime", (short) 0);
		anon4.putShort("Fire", (short) -1);
		anon4.putByte("Invulnerable", (byte) 0);
		anon4.putShort("DeathTime", (short) 0);
		anon4.putFloat("AbsorptionAmount", (float) 0.0);

		ListTag<CompoundTag> Equipment1 = new ListTag<CompoundTag>();

		CompoundTag anon38 = new CompoundTag();
		Equipment1.add(anon38);


		CompoundTag anon41 = new CompoundTag();
		Equipment1.add(anon41);


		CompoundTag anon44 = new CompoundTag();
		Equipment1.add(anon44);


		CompoundTag anon47 = new CompoundTag();
		Equipment1.add(anon47);


		CompoundTag anon50 = new CompoundTag();
		Equipment1.add(anon50);

		anon4.put("Equipment", Equipment1);

		anon4.putByte("OnGround", (byte) 1);
		anon4.putShort("HurtTime", (short) 0);
		anon4.putInt("Profession", profession);
		anon4.putLong("UUIDMost", -3278007056630527295L);
		anon4.putInt("Dimension", 0);
		anon4.putShort("Air", (short) 300);

		ListTag<DoubleTag> Pos1 = new ListTag<DoubleTag>();
		Pos1.add(new DoubleTag("", x));
		Pos1.add(new DoubleTag("", y));
		Pos1.add(new DoubleTag("", z));
		anon4.put("Pos", Pos1);

		anon4.putByte("CanPickUpLoot", (byte) 0);
		anon4.putInt("PortalCooldown", 0);
		anon4.putByte("PersistenceRequired", (byte) 0);
		anon4.putByte("Leashed", (byte) 0);
		anon4.putFloat("FallDistance", (float) 0.0);

		ListTag<FloatTag> Rotation1 = new ListTag<FloatTag>();
		Rotation1.add(new FloatTag("", (float) -0.53445435));
		Rotation1.add(new FloatTag("", (float) -1.921));
		anon4.put("Rotation", Rotation1);

//		Entities1.add(anon4);
		
		chunk.Entities.add(anon4);

	}

	public static void addAnimal(Chunk chunk, String animalType) {
		CompoundTag animal = new CompoundTag();
		ListTag<FloatTag> DropChances = new ListTag<FloatTag>();
		for (int i = 0; i < 5; i++) {
			DropChances.add(new FloatTag("", (float) 0.085));
		}
		animal.put("DropChances", DropChances);

		animal.putInt("Age", 0);
		//skip UUIDLeast
		ListTag<DoubleTag> Motion = new ListTag<DoubleTag>();
		for (int i = 0; i < 3; i++) {
			Motion.add(new DoubleTag("", 0));
		}
		animal.put("Motion", Motion);
		animal.putString("CustomName", "");
		animal.putShort("Health", (short) 10);
		animal.putByte("CustomNameVisible", (byte) 0);
		animal.putString("id", animalType);
		animal.putShort("AttackTime", (short) 0);
		animal.putShort("Fire", (short) -1);
		animal.putByte("Invulnerable", (byte) 0);
		animal.putShort("DeathTime", (short) 0);

		ListTag<CompoundTag> Equipment = new ListTag<CompoundTag>();
		for (int i = 0; i < 5; i++) {
			Equipment.add(new CompoundTag());
		}
		animal.put("Equipment", Equipment);

		animal.putByte("OnGround", (byte) 1);
		animal.putShort("HurtTime", (short) 0);
		animal.putInt("InLove", 0);
		//skip UUIDMost
		animal.putInt("Dimension", 0);
		animal.putShort("Air", (short) 300);

		//random location
		double x = 16 * RANDOM.nextDouble(), z = 16 * RANDOM.nextDouble();
		double y = chunk.getHeights((int) x, (int) z);
		x += chunk.xr; z += chunk.zr;
		//		double x = 10, y = 4, z = 10;

		ListTag<DoubleTag> Pos = new ListTag<DoubleTag>();
		Pos.add(new DoubleTag("", x));
		Pos.add(new DoubleTag("", y));
		Pos.add(new DoubleTag("", z));
		animal.put("Pos", Pos);

		animal.putByte("CanPickUpLoot", (byte) 0);
		animal.putInt("PortalCooldown", (byte) 0);
		animal.putByte("PersistenceRequired", (byte) 0);
		animal.putFloat("FallDistance", 0);
		animal.putString("CustomName", NameReader.getName());
		animal.putByte("CustomNameVisible", (byte) 0);

		ListTag<FloatTag> Rotation = new ListTag<FloatTag>();
		Rotation.add(new FloatTag("", 0));
		Rotation.add(new FloatTag("", 0));
		animal.put("Rotation", Rotation);

		chunk.Entities.add(animal);
	}

}
