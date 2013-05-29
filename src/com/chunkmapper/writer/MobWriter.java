package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.FloatTag;
import com.mojang.nbt.ListTag;

public class MobWriter {
	public static final Random RANDOM = new Random();

	public static void addAnimal(Chunk chunk, String animalType) {
		CompoundTag cow = new CompoundTag();
		ListTag<FloatTag> DropChances = new ListTag<FloatTag>();
		for (int i = 0; i < 5; i++) {
			DropChances.add(new FloatTag("", (float) 0.085));
		}
		cow.put("DropChances", DropChances);
		
		cow.putInt("Age", 0);
		//skip UUIDLeast
		ListTag<DoubleTag> Motion = new ListTag<DoubleTag>();
		for (int i = 0; i < 3; i++) {
			Motion.add(new DoubleTag("", 0));
		}
		cow.put("Motion", Motion);
		cow.putString("CustomName", "");
		cow.putShort("Health", (short) 10);
		cow.putByte("CustomNameVisible", (byte) 0);
		cow.putString("id", animalType);
		cow.putShort("AttackTime", (short) 0);
		cow.putShort("Fire", (short) -1);
		cow.putByte("Invulnerable", (byte) 0);
		cow.putShort("DeathTime", (short) 0);
		
		ListTag<CompoundTag> Equipment = new ListTag<CompoundTag>();
		for (int i = 0; i < 5; i++) {
			Equipment.add(new CompoundTag());
		}
		cow.put("Equipment", Equipment);
		
		cow.putByte("OnGround", (byte) 1);
		cow.putShort("HurtTime", (short) 0);
		cow.putInt("InLove", 0);
		//skip UUIDMost
//		cow.pu
		cow.putInt("Dimension", 0);
		cow.putShort("Air", (short) 300);
		
		//random location
		double x = 16 * RANDOM.nextDouble(), z = 16 * RANDOM.nextDouble();
		double y = chunk.getHeights((int) x, (int) z);
		x += chunk.xr; z += chunk.zr;
//		double x = 10, y = 4, z = 10;
		
		ListTag<DoubleTag> Pos = new ListTag<DoubleTag>();
		Pos.add(new DoubleTag("", x));
		Pos.add(new DoubleTag("", y));
		Pos.add(new DoubleTag("", z));
		cow.put("Pos", Pos);
		
		cow.putByte("CanPickUpLoot", (byte) 0);
		cow.putInt("PortalCooldown", (byte) 0);
		cow.putByte("PersistenceRequired", (byte) 0);
		cow.putFloat("FallDistance", 0);
		
		ListTag<FloatTag> Rotation = new ListTag<FloatTag>();
		Rotation.add(new FloatTag("", 0));
		Rotation.add(new FloatTag("", 0));
		cow.put("Rotation", Rotation);
		
		chunk.Entities.add(cow);
	}

}
