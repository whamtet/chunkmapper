package com.chunkmapper.enumeration;

public enum Block {
	Air ((byte) 0), Stone ((byte) 1), Grass ((byte) 2), Dirt ((byte) 3), Cobblestone ((byte) 4), Planks ((byte) 5), Sapling ((byte) 6), Bedrock ((byte) 7), Water ((byte) 8), Lava ((byte) 10), Sand ((byte) 12), Gravel ((byte) 13), Gold_Ore ((byte) 14), Iron_Ore ((byte) 15), Coal_Ore ((byte) 16), Logs ((byte) 17), Leaves ((byte) 18), Sponge ((byte) 19), Glass ((byte) 20), Lapis_Lazuli_Ore ((byte) 21), LL_Block ((byte) 22), Dispenser ((byte) 23), Sandstone ((byte) 24), Note_Block ((byte) 25), Bed ((byte) 26), Wool ((byte) 35), Yellow_Flower ((byte) 37), Red_Flower ((byte) 38), Brown_Mushroom ((byte) 39), Red_Mushroom ((byte) 40), Gold_Block ((byte) 41), Iron_Block ((byte) 42), Double_Slab ((byte) 43), Half_Slab ((byte) 44), Bricks ((byte) 45), TNT ((byte) 46), Bookshelf ((byte) 47), Mossy_Cobblestone ((byte) 48), Obsidian ((byte) 49), Torch ((byte) 50), Fire ((byte) 51), Monster_Spawner ((byte) 52), Wooden_Steps ((byte) 53), Chest ((byte) 54), Redstone_Dust ((byte) 55), Diamond_Ore ((byte) 56), Diamond_Block ((byte) 57), Crafting_Table ((byte) 58), Crops ((byte) 59), Farmland ((byte) 60), Furnace ((byte) 61), Furnace_Lit ((byte) 62), Sign_Placed_Floor ((byte) 63), Wooden_Door_Placed ((byte) 64), Ladder ((byte) 65), Minecart_Track ((byte) 66), Cobblestone_Steps ((byte) 67), Sign_Placed_Wall ((byte) 68), Lever ((byte) 69), Stone_Pressure_Plate ((byte) 70), Metal_Door_Placed ((byte) 71), Wooden_Pressure_Plate ((byte) 72), Redstone_Ore_Type_1 ((byte) 73), Redstone_Ore_Type_2 ((byte) 74), Redstone_Torch_Unlit ((byte) 75), Redstone_Torch_Lit ((byte) 76), Button ((byte) 77), Snow ((byte) 78), Ice ((byte) 79), Snow_Block ((byte) 80), Cactus ((byte) 81), Clay ((byte) 82), Reeds__Sugar_Canes ((byte) 83), Juke_Box ((byte) 84), Fence ((byte) 85), Unlit_JackOLantern ((byte) 86), Netherrack ((byte) 87), Soul_Sand ((byte) 88), Glow_Stone ((byte) 89), Portal ((byte) 90), Lit_JackOLantern ((byte) 91), Cake ((byte) 92),
	Long_Grass ((byte) 31), Dandelion ((byte) 37), Wood ((byte) 17), 
	Dead_Bush ((byte) 32), Vine ((byte) 106), End_Stone ((byte) 121),
	Wheat ((byte) 59), Carrots ((byte) 141), Potatoes ((byte) 142),
	Rail ((byte) 66), Powered_Rail ((byte) 27), Stone_Brick ((byte) 98),
	Wooden_Door ((byte) 64), Window ((byte) 102), Nether_Brick ((byte) 112),
	Birch_Stairs ((byte) 135), Nether_Fence ((byte) 113), Block_Of_Quartz ((byte) 155),
	Stone_Stairs ((byte) 109);
	public final byte val;
	private Block(byte b) {
		val = b;
	}

}
