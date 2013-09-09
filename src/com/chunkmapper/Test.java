package com.chunkmapper;

import com.chunkmapper.nbt.ByteTag;
import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.DoubleTag;
import com.chunkmapper.nbt.FloatTag;
import com.chunkmapper.nbt.ListTag;

public class Test {
public static void main(String[] args) {

CompoundTag root1 = new CompoundTag();

 CompoundTag Level1 = new CompoundTag();
//      TAG_Byte_Array("Biomes"): [256 bytes]


  Level1.putInt("xPos", 1);
  Level1.putLong("LastUpdate", 1749L);
  Level1.putInt("zPos", 0);
  Level1.putByte("TerrainPopulated", (byte) 1);

  ListTag<ByteTag> TileEntities1 = new ListTag<ByteTag>();
  Level1.put("TileEntities", TileEntities1);

  Level1.putLong("InhabitedTime", 0L);
//      TAG_Int_Array("HeightMap"): [256 bytes]

  ListTag<CompoundTag> Sections1 = new ListTag<CompoundTag>();

   CompoundTag anon68 = new CompoundTag();
//            TAG_Byte_Array("Data"): [2048 bytes]
//            TAG_Byte_Array("SkyLight"): [2048 bytes]
//            TAG_Byte_Array("BlockLight"): [2048 bytes]
    anon68.putByte("Y", (byte) 0);
//            TAG_Byte_Array("Blocks"): [4096 bytes]
   Sections1.add(anon68);

  Level1.put("Sections", Sections1);

 root1.put("Level", Level1);


}}
