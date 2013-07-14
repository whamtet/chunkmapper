package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.reader.NameReader;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.FloatTag;
import com.mojang.nbt.ListTag;

public class MobWriter {
	public static final Random RANDOM = new Random();
	private static void shit() {
		CompoundTag c = new CompoundTag();
//		:TAG_End :TAG_Byte :TAG_Short :TAG_Int :TAG_Long
//        :TAG_Float :TAG_Double :TAG_Byte_Array :TAG_String
//        :TAG_List :TAG_Compound :TAG_Int_Array])
		ListTag l;
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
