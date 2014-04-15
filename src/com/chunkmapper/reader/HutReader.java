package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.writer.SchematicArtifactWriter;

public class HutReader {
	public final ArrayList<Hut> huts = new ArrayList<Hut>();
	
	private static class Hut {
		public final Point point;
		public final String name;
		public Hut(Point point, String name) {
			this.point = point;
			this.name = name;
		}
	}
	public HutReader(OverpassObject o, int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		for (Node node : o.nodes) {
			String tourism = node.map.get("tourism");
			if ("alpine_hut".equals(tourism) || "wilderness_hut".equals(tourism)) {
				huts.add(new Hut(node.point, node.map.get("name")));
			}
		}
	}
	public void addHut(Chunk chunk) {
		for (Hut hut : huts) {
			Point p = hut.point;
			if (chunk.x0 <= p.x && p.x < chunk.x0 + 16 && chunk.z0 <= p.z && p.z < chunk.z0 + 16) {
				SchematicArtifactWriter.addHut(chunk, hut.name);
				return;
			}
		}
	}
//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("brown hut, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		HutReader reader = new HutReader(regionx, regionz);
//	}
	

}
