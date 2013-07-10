package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.reader.NameReader;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.FloatTag;
import com.mojang.nbt.ListTag;

public class HorseWriter  {
	private static Random RANDOM = new Random();
public static void addHorse(Chunk chunk) {

   CompoundTag anon4 = new CompoundTag();

    ListTag<FloatTag> DropChances1 = new ListTag<FloatTag>();
     DropChances1.add(new FloatTag("", (float) 0.085));
     DropChances1.add(new FloatTag("", (float) 0.085));
     DropChances1.add(new FloatTag("", (float) 0.085));
     DropChances1.add(new FloatTag("", (float) 0.085));
     DropChances1.add(new FloatTag("", (float) 0.085));
    anon4.put("DropChances", DropChances1);

    anon4.putInt("Temper", 0);

    ListTag<CompoundTag> Attributes1 = new ListTag<CompoundTag>();

     CompoundTag anon14 = new CompoundTag();
      anon14.putString("Name", "generic.maxHealth");
      anon14.putDouble("Base", 26.0);
     Attributes1.add(anon14);


     CompoundTag anon17 = new CompoundTag();
      anon17.putString("Name", "generic.knockbackResistance");
      anon17.putDouble("Base", 0.0);
     Attributes1.add(anon17);


     CompoundTag anon20 = new CompoundTag();
      anon20.putString("Name", "generic.movementSpeed");
      anon20.putDouble("Base", 0.20290736004080034);
     Attributes1.add(anon20);


     CompoundTag anon23 = new CompoundTag();
      anon23.putString("Name", "generic.followRange");
      anon23.putDouble("Base", 16.0);

      ListTag<CompoundTag> Modifiers1 = new ListTag<CompoundTag>();

       CompoundTag anon26 = new CompoundTag();
        anon26.putString("Name", "Random spawn bonus");
        anon26.putDouble("Amount", -0.0710700511624908);
        anon26.putInt("Operation", 1);
        anon26.putLong("UUIDLeast", -9182063017716530392L);
        anon26.putLong("UUIDMost", 2827944372300303459L);
       Modifiers1.add(anon26);

      anon23.put("Modifiers", Modifiers1);

     Attributes1.add(anon23);


     CompoundTag anon31 = new CompoundTag();
      anon31.putString("Name", "horse.jumpStrength");
      anon31.putDouble("Base", 0.517055380653359);
     Attributes1.add(anon31);

    anon4.put("Attributes", Attributes1);

    anon4.putLong("UUIDLeast", -8055830107210117607L);
    anon4.putInt("Age", 0);
    anon4.putByte("HasReproduced", (byte) 0);

    ListTag<DoubleTag> Motion1 = new ListTag<DoubleTag>();
     Motion1.add(new DoubleTag("", 0.0));
     Motion1.add(new DoubleTag("", -0.0784000015258789));
     Motion1.add(new DoubleTag("", 0.0));
    anon4.put("Motion", Motion1);

    anon4.putString("CustomName", NameReader.getName());
    anon4.putInt("Type", 0);
    anon4.putShort("Health", (short) 26);
    anon4.putFloat("HealF", (float) 26.0);
    anon4.putByte("Bred", (byte) 0);
    anon4.putByte("CustomNameVisible", (byte) 0);
    anon4.putString("id", "EntityHorse");
    anon4.putShort("AttackTime", (short) 0);
    anon4.putShort("Fire", (short) -1);
    anon4.putByte("ChestedHorse", (byte) 0);
    anon4.putByte("Invulnerable", (byte) 0);
    anon4.putShort("DeathTime", (short) 0);
    anon4.putString("OwnerName", "");
    anon4.putByte("Tame", (byte) 1);
    anon4.putFloat("AbsorptionAmount", (float) 0.0);

    ListTag<CompoundTag> Equipment1 = new ListTag<CompoundTag>();

     CompoundTag anon41 = new CompoundTag();
     Equipment1.add(anon41);


     CompoundTag anon44 = new CompoundTag();
     Equipment1.add(anon44);


     CompoundTag anon47 = new CompoundTag();
     Equipment1.add(anon47);


     CompoundTag anon50 = new CompoundTag();
     Equipment1.add(anon50);


     CompoundTag anon53 = new CompoundTag();
     Equipment1.add(anon53);

    anon4.put("Equipment", Equipment1);

    anon4.putShort("HurtTime", (short) 0);
    anon4.putInt("InLove", 0);
    anon4.putByte("OnGround", (byte) 1);
    anon4.putLong("UUIDMost", -19557510837482567L);
    anon4.putInt("Dimension", 0);
    anon4.putShort("Air", (short) 300);
    anon4.putByte("CanPickUpLoot", (byte) 0);

    double x = 16 * RANDOM.nextDouble(), z = 16 * RANDOM.nextDouble();
	double y = chunk.getHeights((int) x, (int) z);
	x += chunk.xr; z += chunk.zr;
	ListTag<DoubleTag> Pos1 = new ListTag<DoubleTag>();
	Pos1.add(new DoubleTag("", x));
	Pos1.add(new DoubleTag("", y));
	Pos1.add(new DoubleTag("", z));
	anon4.put("Pos", Pos1);
//    ListTag<DoubleTag> Pos1 = new ListTag<DoubleTag>();
//     Pos1.add(new DoubleTag("", 7.5));
//     Pos1.add(new DoubleTag("", 4.0));
//     Pos1.add(new DoubleTag("", 10.5));
//    anon4.put("Pos", Pos1);

    anon4.putInt("PortalCooldown", 0);
    anon4.putByte("PersistenceRequired", (byte) 0);
    anon4.putByte("Leashed", (byte) 0);
    anon4.putFloat("FallDistance", (float) 0.0);

    ListTag<FloatTag> Rotation1 = new ListTag<FloatTag>();
     Rotation1.add(new FloatTag("", (float) 71.963646));
     Rotation1.add(new FloatTag("", (float) -4.940376));
    anon4.put("Rotation", Rotation1);

//    anon4.putInt("Variant", 1026);
    anon4.putInt("Variant", 256 * RANDOM.nextInt(5) + RANDOM.nextInt(7));
    anon4.putByte("EatingHaystack", (byte) 0);
    
    chunk.Entities.add(anon4);

}}
