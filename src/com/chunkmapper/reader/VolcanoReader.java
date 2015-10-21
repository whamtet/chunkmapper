package com.chunkmapper.reader;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.binaryparser.OSMRouter;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.writer.ArtifactWriter;

public class VolcanoReader {
	private final ArrayList<Node> nodes = new ArrayList<Node>();
	public VolcanoReader(OverpassObject o) {
		for (Node node : o.nodes) {
			if ("volcano".equals(node.map.get("natural"))) {
				nodes.add(node);
			}
		}
	}
	public void addHotRocks(Chunk chunk) {
		for (Node node : nodes) {
			if (chunk.bbox.contains(node.point)) {
				String status = node.map.get("status"), condition = node.map.get("condition");
				boolean active = "dormant".equals(status) || "dormant".equals(condition)
						|| "active".equals(status) || "active".equals(condition);
				byte rockType = active ? Blocka.Lava : Blocka.Obsidian;
				int x0 = node.point.x - chunk.x0 - 1, x1 = x0 + 3;
				int z0 = node.point.z - chunk.z0 - 1, z1 = z0 + 3;
				if (x0 < 0) x0 = 0;
				if (z0 < 0) z0 = 0;
				if (x1 > 16) x1 = 16;
				if (z1 > 16) z1 = 16;
				
				int h = chunk.getHeights(x0, z0) - 2;
				for (int y = 1; y < h; y++) {
					for (int z = z0; z < z1; z++) {
						for (int x = x0; x < x1; x++) {
							chunk.Blocks[y][z][x] = rockType;
						}
					}
				}
				String sign = node.map.get("name");
				if (sign == null) sign = "Volcano";
				ArtifactWriter.addSign(chunk, node.point.z - chunk.z0, node.point.x - chunk.x0, sign.split(" "));
				
			}
		}
	}
//	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("Mt Taranaki, nz");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		
//		OverpassObject o = OSMRouter.getObject(regionx, regionz);
//		VolcanoReader reader = new VolcanoReader(o);
//		System.out.println(reader.nodes.size());
//	}

}
