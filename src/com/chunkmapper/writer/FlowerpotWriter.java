package com.chunkmapper.writer;

import java.util.Random;

import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.ListTag;

public class FlowerpotWriter {

	private static ListTag<CompoundTag> TileEntities1 = new ListTag<CompoundTag>();
	private static final Random RANDOM = new Random();
	
	public static CompoundTag getRandomFlowerpot(int x, int y, int z) {
		CompoundTag out = new CompoundTag();
		out.putInt("x", x);
		out.putInt("y", y);
		out.putInt("z", z);
		
		CompoundTag model = TileEntities1.get(RANDOM.nextInt(TileEntities1.size()));
		
		out.putString("id", "FlowerPot");
		out.putInt("Data", model.getInt("Data"));
		out.putInt("Item", model.getInt("Item"));
		
		return out;
	}

	static {
		CompoundTag anon385 = new CompoundTag();
		anon385.putString("id", "FlowerPot");
		anon385.putInt("Data", 3);
		anon385.putInt("Item", 38);
		anon385.putInt("z", 10);
		anon385.putInt("y", 15);
		anon385.putInt("x", 8);
		TileEntities1.add(anon385);


		CompoundTag anon388 = new CompoundTag();
		anon388.putString("id", "FlowerPot");
		anon388.putInt("Data", 2);
		anon388.putInt("Item", 38);
		anon388.putInt("z", 9);
		anon388.putInt("y", 15);
		anon388.putInt("x", 8);
		TileEntities1.add(anon388);


		CompoundTag anon391 = new CompoundTag();
		anon391.putString("id", "FlowerPot");
		anon391.putInt("Data", 1);
		anon391.putInt("Item", 38);
		anon391.putInt("z", 8);
		anon391.putInt("y", 15);
		anon391.putInt("x", 8);
		TileEntities1.add(anon391);


		CompoundTag anon394 = new CompoundTag();
		anon394.putString("id", "FlowerPot");
		anon394.putInt("Data", 0);
		anon394.putInt("Item", 38);
		anon394.putInt("z", 7);
		anon394.putInt("y", 15);
		anon394.putInt("x", 8);
		TileEntities1.add(anon394);


		CompoundTag anon397 = new CompoundTag();
		anon397.putString("id", "FlowerPot");
		anon397.putInt("Data", 0);
		anon397.putInt("Item", 37);
		anon397.putInt("z", 6);
		anon397.putInt("y", 15);
		anon397.putInt("x", 8);
		TileEntities1.add(anon397);


		CompoundTag anon400 = new CompoundTag();
		anon400.putString("id", "FlowerPot");
		anon400.putInt("Data", 2);
		anon400.putInt("Item", 31);
		anon400.putInt("z", 5);
		anon400.putInt("y", 15);
		anon400.putInt("x", 8);
		TileEntities1.add(anon400);


		CompoundTag anon403 = new CompoundTag();
		anon403.putString("id", "FlowerPot");
		anon403.putInt("Data", 5);
		anon403.putInt("Item", 6);
		anon403.putInt("z", 4);
		anon403.putInt("y", 15);
		anon403.putInt("x", 8);
		TileEntities1.add(anon403);


		CompoundTag anon406 = new CompoundTag();
		anon406.putString("id", "FlowerPot");
		anon406.putInt("Data", 4);
		anon406.putInt("Item", 6);
		anon406.putInt("z", 3);
		anon406.putInt("y", 15);
		anon406.putInt("x", 8);
		TileEntities1.add(anon406);


		CompoundTag anon409 = new CompoundTag();
		anon409.putString("id", "FlowerPot");
		anon409.putInt("Data", 2);
		anon409.putInt("Item", 6);
		anon409.putInt("z", 3);
		anon409.putInt("y", 15);
		anon409.putInt("x", 6);
		TileEntities1.add(anon409);


		CompoundTag anon412 = new CompoundTag();
		anon412.putString("id", "FlowerPot");
		anon412.putInt("Data", 0);
		anon412.putInt("Item", 6);
		anon412.putInt("z", 3);
		anon412.putInt("y", 15);
		anon412.putInt("x", 4);
		TileEntities1.add(anon412);


		CompoundTag anon415 = new CompoundTag();
		anon415.putString("id", "FlowerPot");
		anon415.putInt("Data", 6);
		anon415.putInt("Item", 38);
		anon415.putInt("z", 8);
		anon415.putInt("y", 15);
		anon415.putInt("x", 7);
		TileEntities1.add(anon415);


		CompoundTag anon418 = new CompoundTag();
		anon418.putString("id", "FlowerPot");
		anon418.putInt("Data", 8);
		anon418.putInt("Item", 38);
		anon418.putInt("z", 7);
		anon418.putInt("y", 15);
		anon418.putInt("x", 7);
		TileEntities1.add(anon418);


		CompoundTag anon421 = new CompoundTag();
		anon421.putString("id", "FlowerPot");
		anon421.putInt("Data", 7);
		anon421.putInt("Item", 38);
		anon421.putInt("z", 6);
		anon421.putInt("y", 15);
		anon421.putInt("x", 7);
		TileEntities1.add(anon421);


		CompoundTag anon424 = new CompoundTag();
		anon424.putString("id", "FlowerPot");
		anon424.putInt("Data", 0);
		anon424.putInt("Item", 39);
		anon424.putInt("z", 5);
		anon424.putInt("y", 15);
		anon424.putInt("x", 7);
		TileEntities1.add(anon424);


		CompoundTag anon427 = new CompoundTag();
		anon427.putString("id", "FlowerPot");
		anon427.putInt("Data", 0);
		anon427.putInt("Item", 40);
		anon427.putInt("z", 4);
		anon427.putInt("y", 15);
		anon427.putInt("x", 7);
		TileEntities1.add(anon427);


		CompoundTag anon430 = new CompoundTag();
		anon430.putString("id", "FlowerPot");
		anon430.putInt("Data", 3);
		anon430.putInt("Item", 6);
		anon430.putInt("z", 3);
		anon430.putInt("y", 15);
		anon430.putInt("x", 7);
		TileEntities1.add(anon430);


		CompoundTag anon433 = new CompoundTag();
		anon433.putString("id", "FlowerPot");
		anon433.putInt("Data", 4);
		anon433.putInt("Item", 38);
		anon433.putInt("z", 10);
		anon433.putInt("y", 15);
		anon433.putInt("x", 7);
		TileEntities1.add(anon433);


		CompoundTag anon436 = new CompoundTag();
		anon436.putString("id", "FlowerPot");
		anon436.putInt("Data", 1);
		anon436.putInt("Item", 6);
		anon436.putInt("z", 3);
		anon436.putInt("y", 15);
		anon436.putInt("x", 5);
		TileEntities1.add(anon436);


		CompoundTag anon439 = new CompoundTag();
		anon439.putString("id", "FlowerPot");
		anon439.putInt("Data", 5);
		anon439.putInt("Item", 38);
		anon439.putInt("z", 9);
		anon439.putInt("y", 15);
		anon439.putInt("x", 7);
		TileEntities1.add(anon439);
	}
	
	
}
