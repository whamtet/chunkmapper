package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.DataSource;
import com.chunkmapper.enumeration.FarmType;
import com.chunkmapper.enumeration.Gate;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.FarmTypeReader;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.writer.HorseWriter;
import com.chunkmapper.writer.MobWriter;

public class RainfedCrops extends AbstractColumn {
	public static final Globcover TYPE = Globcover.RainfedCrops;
	//	public final byte cropType;
	public final FarmType farmType;

	public RainfedCrops(int absx, int absz, FarmType farmType, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		this.farmType = farmType;
	}

	public void addColumn(Chunk chunk) {
		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);

		boolean hasNorthernBoundary = absz % FarmTypeReader.WIDTH == 0;
		boolean hasWesternBoundary = absx % FarmTypeReader.WIDTH == 0;

		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
		chunk.Blocks[h-1][z][x] = Block.Grass.val;
		if (hasNorthernBoundary || hasWesternBoundary) {
			if (x == 7) {
				chunk.Blocks[h][z][x] = Blocka.Gate;
				chunk.Data[h][z][x] = Gate.NORTH;
			} else if (z == 7) {
				chunk.Blocks[h][z][x] = Blocka.Gate;
				chunk.Data[h][z][x] = Gate.EAST;
			} else {
				chunk.Blocks[h][z][x] = Block.Fence.val;
			}
		} else if (farmType == FarmType.Cows || farmType == FarmType.Chicken || 
				farmType == FarmType.Sheep || farmType == FarmType.Horses) {
			if (RANDOM.nextInt(3) == 0) {
				chunk.Blocks[h][z][x] = Block.Long_Grass.val;
				chunk.Data[h][z][x] = DataSource.Long_Grass.val;
			}
			switch (this.farmType) {
			case Cows:
				if (RANDOM.nextInt(256/FarmType.COW_DENSITY) == 0)
					MobWriter.addAnimal(chunk, "Cow");
				break;
			case Chicken:
				if (RANDOM.nextInt(256/FarmType.CHICKEN_DENSITY) == 0)
					MobWriter.addAnimal(chunk, "Chicken");
				break;
			case Sheep:
				if (RANDOM.nextInt(256/FarmType.SHEEP_DENSITY) == 0)
					MobWriter.addAnimal(chunk, "Sheep");
				break;
			case Horses:
				if (RANDOM.nextInt(256/FarmType.COW_DENSITY) == 0)
					HorseWriter.addHorse(chunk);
				break;
			default:
			}
		} else {
			throw new RuntimeException("should not be here");
//			chunk.Blocks[h-1][z][x] = Block.Farmland.val;
//			chunk.Data[h-1][z][x] = 7;
//			byte cropType = 0;
//			switch (farmType) {
//			case Carrots:
//				cropType = Block.Carrots.val;
//				break;
//			case Potatoes:
//				cropType = Block.Potatoes.val;
//				break;
//			case Wheat:
//				cropType = Block.Wheat.val;
//				break;
//			default:
//			}
//			chunk.Blocks[h][z][x] = cropType;
//			chunk.Data[h][z][x] = 7; //fully matured crops
		}
	}
}
