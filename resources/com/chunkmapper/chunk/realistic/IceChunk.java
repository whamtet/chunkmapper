package com.chunkmapper.chunk.realistic;

import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Biome;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.math.Piecewise;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.writer.GenericWriter;

public class IceChunk extends Chunk {
	private static Piecewise makePiecewise(int[] points, int lat) {
		TriangleZonator zonator = new TriangleZonator();
		zonator.addPoint(points[0], points[1]);
		zonator.addPoint(points[2], points[3]);
		int[] cutOffs = zonator.getCutoffs(lat);

		Piecewise out = new Piecewise();
		out.addControlPoint(cutOffs[0], 0);
		out.addControlPoint(cutOffs[1], 1);
		return out;
	}
	public static void placeColumn(int x, int z, Chunk chunk, int absChunkX, int absChunkZ) {
		//assumes bedrock already placed and the check for ocean block done
		int h = chunk.getHeights(x, z);
		chunk.Blocks[h-2][z][x] = Block.Snow_Block.val;
		chunk.Blocks[h-1][z][x] = Block.Snow_Block.val;
	}
		
	//	private static final TriangleZonator zonator = new TriangleZonator();
	//	static {
	//		zonator.addPoint(3500, 70);
	//		zonator.addPoint(5500, 80);
	//	}
	public IceChunk(int absChunkX, int absChunkZ, int[][] heights) {
		super(absChunkX, absChunkZ);
		setBiome(Biome.ICE.val);
		this.heights = heights;
		int approxLat = Math.abs(absChunkZ * 16 / 3600);
		final int soilThickness = 2;
//		GenericWriter.addBedrock(Blocks, heights, soilThickness);


		Piecewise snowBlockPiecewise = makePiecewise(new int[] {4000, 70, 5500, 80}, approxLat);
		Piecewise snowPiecewise = makePiecewise(new int[] {3500, 70, 5000, 80}, approxLat);

		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				int h = this.getHeights(i, j);
				if (h < 0) {
					super.writeOceanColumn(i, j);
					continue;
				}
//				if (RANDOM.nextDouble() < 0.1)
//					Blocks[h][j][i] = Block.Long_Grass.val;
				if (RANDOM.nextDouble() < 0.01)
					Blocks[h][j][i] = Block.Leaves.val;
				byte surfaceType = RANDOM.nextDouble() < snowBlockPiecewise.interpolateDouble(h) ?
						Block.Snow_Block.val : RANDOM.nextInt(2) == 0 ? Block.Stone.val : Block.Gravel.val;
				Blocks[h-2][j][i] = surfaceType;
				Blocks[h-1][j][i] = surfaceType;
				if (RANDOM.nextDouble() < snowPiecewise.interpolateDouble(h)) {
					Blocks[h][j][i] = Block.Snow.val;
				}
			}
		}
	}

}
