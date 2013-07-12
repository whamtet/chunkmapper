package com.chunkmapper.process;

import java.util.HashMap;
import java.util.HashSet;

import com.chunkmapper.Point;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.math.Matthewmatics;

public class ChunkCache {
	public HashSet<Point> regionPoints = new HashSet<Point>();
	public HashMap<Point, Chunk> chunks = new HashMap<Point, Chunk>();
	public Chunk getChunk(int chunkx, int chunkz) {
		Point chunkPoint = new Point(chunkx, chunkz);
		if (chunks.containsKey(chunkPoint)) {
			return chunks.get(chunkPoint);
		}
		regionPoints.add(new Point(Matthewmatics.div(chunkx, 32), Matthewmatics.div(chunkz, 32)));
		
		Chunk chunk = new Chunk(chunkx, chunkz, null, chunkx, chunkz);
		chunks.put(chunkPoint, chunk);
		return chunk;
	}

}
