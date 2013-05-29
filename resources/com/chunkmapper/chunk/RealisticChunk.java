package com.chunkmapper.chunk;

public abstract class RealisticChunk extends Chunk {
	public final double lat;
	public RealisticChunk(int chunkX, int chunkZ, int[][] heights, double lat) {
		super(chunkX, chunkZ);
		this.lat = lat;
	}
	

}
