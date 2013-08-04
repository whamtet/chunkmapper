package com.chunkmapper;

import com.mojang.nbt.CompoundTag;



public class Test {

	public static void main(String[] args) throws Exception {
		CompoundTag t = new CompoundTag();
		byte y = 2;
		t.putByte("hi", y);
		y = 3;
		System.out.println(t.getByte("hi"));
	}

}
