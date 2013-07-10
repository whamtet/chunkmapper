package com.chunkmapper.writer;

import com.mojang.nbt.ByteTag;
import com.mojang.nbt.CompoundTag;
import com.mojang.nbt.DoubleTag;
import com.mojang.nbt.FloatTag;
import com.mojang.nbt.ListTag;

public class Test {
public static void main(String[] args) {

CompoundTag root1 = new CompoundTag();

 CompoundTag Level1 = new CompoundTag();
//      TAG_Byte_Array("Biomes"): [256 bytes]

  ListTag<CompoundTag> Entities1 = new ListTag<CompoundTag>();

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

    anon4.putString("CustomName", "");
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
    anon4.putByte("Tame", (byte) 0);
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

    ListTag<DoubleTag> Pos1 = new ListTag<DoubleTag>();
     Pos1.add(new DoubleTag("", 7.5));
     Pos1.add(new DoubleTag("", 4.0));
     Pos1.add(new DoubleTag("", 10.5));
    anon4.put("Pos", Pos1);

    anon4.putInt("PortalCooldown", 0);
    anon4.putByte("PersistenceRequired", (byte) 0);
    anon4.putByte("Leashed", (byte) 0);
    anon4.putFloat("FallDistance", (float) 0.0);

    ListTag<FloatTag> Rotation1 = new ListTag<FloatTag>();
     Rotation1.add(new FloatTag("", (float) 71.963646));
     Rotation1.add(new FloatTag("", (float) -4.940376));
    anon4.put("Rotation", Rotation1);

    anon4.putInt("Variant", 1026);
    anon4.putByte("EatingHaystack", (byte) 0);
   Entities1.add(anon4);


   CompoundTag anon67 = new CompoundTag();

    ListTag<FloatTag> DropChances2 = new ListTag<FloatTag>();
     DropChances2.add(new FloatTag("", (float) 0.085));
     DropChances2.add(new FloatTag("", (float) 0.085));
     DropChances2.add(new FloatTag("", (float) 0.085));
     DropChances2.add(new FloatTag("", (float) 0.085));
     DropChances2.add(new FloatTag("", (float) 0.085));
    anon67.put("DropChances", DropChances2);


    ListTag<CompoundTag> Attributes2 = new ListTag<CompoundTag>();

     CompoundTag anon77 = new CompoundTag();
      anon77.putString("Name", "generic.maxHealth");
      anon77.putDouble("Base", 4.0);
     Attributes2.add(anon77);


     CompoundTag anon80 = new CompoundTag();
      anon80.putString("Name", "generic.knockbackResistance");
      anon80.putDouble("Base", 0.0);
     Attributes2.add(anon80);


     CompoundTag anon83 = new CompoundTag();
      anon83.putString("Name", "generic.movementSpeed");
      anon83.putDouble("Base", 0.25);
     Attributes2.add(anon83);


     CompoundTag anon86 = new CompoundTag();
      anon86.putString("Name", "generic.followRange");
      anon86.putDouble("Base", 16.0);
     Attributes2.add(anon86);

    anon67.put("Attributes", Attributes2);

    anon67.putLong("UUIDLeast", -5936000497583808125L);
    anon67.putInt("Age", 0);

    ListTag<DoubleTag> Motion2 = new ListTag<DoubleTag>();
     Motion2.add(new DoubleTag("", 0.0));
     Motion2.add(new DoubleTag("", -0.0784000015258789));
     Motion2.add(new DoubleTag("", 0.0));
    anon67.put("Motion", Motion2);

    anon67.putString("CustomName", "");
    anon67.putShort("Health", (short) 4);
    anon67.putFloat("HealF", (float) 4.0);
    anon67.putByte("CustomNameVisible", (byte) 0);
    anon67.putString("id", "Chicken");
    anon67.putShort("AttackTime", (short) 0);
    anon67.putShort("Fire", (short) -1);
    anon67.putByte("Invulnerable", (byte) 0);
    anon67.putShort("DeathTime", (short) 0);
    anon67.putFloat("AbsorptionAmount", (float) 0.0);

    ListTag<CompoundTag> Equipment2 = new ListTag<CompoundTag>();

     CompoundTag anon96 = new CompoundTag();
     Equipment2.add(anon96);


     CompoundTag anon99 = new CompoundTag();
     Equipment2.add(anon99);


     CompoundTag anon102 = new CompoundTag();
     Equipment2.add(anon102);


     CompoundTag anon105 = new CompoundTag();
     Equipment2.add(anon105);


     CompoundTag anon108 = new CompoundTag();
     Equipment2.add(anon108);

    anon67.put("Equipment", Equipment2);

    anon67.putInt("InLove", 0);
    anon67.putByte("OnGround", (byte) 1);
    anon67.putShort("HurtTime", (short) 0);
    anon67.putLong("UUIDMost", -4804960920834060920L);
    anon67.putInt("Dimension", 0);
    anon67.putShort("Air", (short) 300);

    ListTag<DoubleTag> Pos2 = new ListTag<DoubleTag>();
     Pos2.add(new DoubleTag("", 11.452473758610019));
     Pos2.add(new DoubleTag("", 4.0));
     Pos2.add(new DoubleTag("", 2.4368649541857437));
    anon67.put("Pos", Pos2);

    anon67.putByte("CanPickUpLoot", (byte) 0);
    anon67.putInt("PortalCooldown", 0);
    anon67.putByte("PersistenceRequired", (byte) 0);
    anon67.putByte("Leashed", (byte) 0);
    anon67.putFloat("FallDistance", (float) 0.0);

    ListTag<FloatTag> Rotation2 = new ListTag<FloatTag>();
     Rotation2.add(new FloatTag("", (float) 325.23978));
     Rotation2.add(new FloatTag("", (float) 0.0));
    anon67.put("Rotation", Rotation2);

   Entities1.add(anon67);

  Level1.put("Entities", Entities1);

  Level1.putInt("xPos", 0);
  Level1.putLong("LastUpdate", 9061L);
  Level1.putInt("zPos", 0);
  Level1.putByte("TerrainPopulated", (byte) 1);

  ListTag<ByteTag> TileEntities1 = new ListTag<ByteTag>();
  Level1.put("TileEntities", TileEntities1);

  Level1.putLong("InhabitedTime", 0L);
//      TAG_Int_Array("HeightMap"): [256 bytes]

  ListTag<CompoundTag> Sections1 = new ListTag<CompoundTag>();

   CompoundTag anon126 = new CompoundTag();
//            TAG_Byte_Array("Data"): [2048 bytes]
//            TAG_Byte_Array("SkyLight"): [2048 bytes]
//            TAG_Byte_Array("BlockLight"): [2048 bytes]
    anon126.putByte("Y", (byte) 0);
//            TAG_Byte_Array("Blocks"): [4096 bytes]
   Sections1.add(anon126);

  Level1.put("Sections", Sections1);

 root1.put("Level", Level1);


}}
