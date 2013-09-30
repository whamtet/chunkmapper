package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.Zip;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassParser;
import com.chunkmapper.protoc.OSMContainer;
import com.chunkmapper.protoc.admin.BucketInfo;

public class OsmosisParser {
	public static final int NODE = 0, WAY = 1, RELATION = 2;
	private static final ConcurrentHashMap<Point, OverpassObject> cache = new ConcurrentHashMap<Point, OverpassObject>();
	private static ArrayList<Rectangle> rectangles;
	private static Object key = new Object();
	private static void setRectangles() throws InterruptedException {
		synchronized(key) {
			while (rectangles == null) {
				rectangles = getRectangles();
				if (rectangles == null)
					Thread.sleep(1000);
			}
		}
	}
	private static ArrayList<Rectangle> getRectangles() {
		try {
			URL url = new URL("http://chunkbackend.appspot.com/static/osm.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			ArrayList<Rectangle> out = new ArrayList<Rectangle>();
			String line;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("f_")) {
					String[] split = line.split("_");

					int x = Integer.parseInt(split[1]), z = Integer.parseInt(split[2]);
					int width = Integer.parseInt(split[3]), height = Integer.parseInt(split[4]);
					out.add(new Rectangle(x, z, width, height));
				}
			}
			return out;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("london");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		System.out.println(getFileContents(regionx, regionz));
		System.out.println(OverpassParser.getObject(regionx, regionz));
	}
	private static FileContents getFileContents(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		setRectangles();
		FileContents out = new FileContents();
		int x = regionx * 512, z = regionz * 512 - 512;
		Rectangle myRectangle = new Rectangle(x, z, 512, 512);
		String rootAddress = BucketInfo.getBucket("chunkmapper-osm") + "/f_";
		for (Rectangle r : rectangles) {
			if (myRectangle.intersects(r)) {
				URL url = new URL(rootAddress + r.x + "_" + r.y + "_" + r.width + "_" + r.height);
				readFile(url, out);
			}
		}
		return out;
	}

	private static FileContents readFile(URL url, FileContents out) throws IOException, DataFormatException {
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(Zip.inflate(url.openStream())));
		try {
			while(true) {
				byte type = in.readByte();
				int len = in.readInt();
				byte[] data = new byte[len];
				in.readFully(data);
				switch(type) {
				case NODE:
					out.nodes.add(OSMContainer.Node.parseFrom(data));
					break;
				case WAY:
					out.ways.add(OSMContainer.Way.parseFrom(data));
					break;
				case RELATION:
					out.relations.add(OSMContainer.Relation.parseFrom(data));
					break;
				}
			}
		} catch (EOFException e) {

		}
		in.close();
		return out;
	}

}
