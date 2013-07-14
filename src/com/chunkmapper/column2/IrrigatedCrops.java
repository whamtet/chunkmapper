package com.chunkmapper.column2;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.reader.HeightsReader;

public class IrrigatedCrops extends AbstractColumn {
	public static final Globcover TYPE = Globcover.IrrigatedCrops;
	public final byte cropType;
	public final boolean hasWater;
	
	public IrrigatedCrops(int absx, int absz, byte cropType, HeightsReader heightsReader) {
		super(absx, absz, heightsReader);
		this.cropType = cropType;
		this.hasWater = RANDOM.nextInt(4) == 0 &&
				heightsReader.getHeightxz(absx-1, absz) >= h && heightsReader.getHeightxz(absx+1, absz) >= h &&
				heightsReader.getHeightxz(absx, absz-1) >= h && heightsReader.getHeightxz(absx, absz+1) >= h;
		super.HAS_WATER = hasWater;
	}
	
//	public void addColumn(Chunk chunk) {
//		int x = com.chunkmapper.math.Matthewmatics.mod(absx, 16);
//		int z = com.chunkmapper.math.Matthewmatics.mod(absz, 16);
//		
//		chunk.Blocks[h-3][z][x] = Block.Dirt.val;
//		chunk.Blocks[h-2][z][x] = Block.Dirt.val;
//		chunk.Blocks[h-1][z][x] = Block.Grass.val;
////		if (this.hasWater) {
////			chunk.Blocks[h-1][z][x] = Block.Water.val;
////		} else {
////			chunk.Blocks[h-1][z][x] = Block.Farmland.val;
////			chunk.Data[h-1][z][x] = 7;
////			chunk.Blocks[h][z][x] = cropType;
////			chunk.Data[h][z][x] = 7; //fully matured crops
////		}
//	}

}
