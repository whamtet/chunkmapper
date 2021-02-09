package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.DataFormatException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.Zip;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.URLs;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.downloader.Downloader;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.OverpassObject.Node;
import com.chunkmapper.parser.OverpassObject.Relation;
import com.chunkmapper.parser.OverpassObject.Way;
import com.chunkmapper.protoc.OSMContainer;

public class OsmosisParser implements OverpassObjectSource {
	
	/*
	 * OverpassObjectSource for live Open Street Map Maps.
	 * Queries Open Street Map using the Osmosis Web Service.
	 * Turned off by default in order to respect Open Street Map servers.
	 */
	
	public static final int NODE = 0, WAY = 1, RELATION = 2;
	private static final HashMap<URL, URL> lockMap = new HashMap<URL, URL>();
	private static Object masterLock = new Object();
	private static DefaultHttpClient httpclient = Downloader.getHttpClient();
	private static ConcurrentHashMap<URL, FileContents> cache2 = new ConcurrentHashMap<URL, FileContents>();
	private static ArrayList<Rectangle> rectangles;
	private static Object key = new Object();
	public static final File CACHE = new File(Utila.CACHE, "Osmosis");
	
	public static void flushCache() {
		cache2 = new ConcurrentHashMap<>();
	}
	
	static {
		CACHE.mkdirs();
	}

	private static URL getLock(URL url) {
		synchronized(masterLock) {
			if (lockMap.containsKey(url)) {
				return lockMap.get(url);
			} else {
				lockMap.put(url, url);
				return url;
			}
		}
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.binaryparser.OverpassObjectSource#getObject(int, int)
	 */
	@Override
	public OverpassObject getObject(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		FileContents contents = getFileContents(regionx, regionz);
		OverpassObject out = new OverpassObject();
		for (OSMContainer.Node rawNode : contents.nodes) {
			Node node = new Node(new Point(rawNode.getX(), rawNode.getZ()), rawNode.getId());
			Iterator<String> ki = rawNode.getKeysList().iterator(), vi = rawNode.getValsList().iterator();
			while (ki.hasNext()) {
				node.map.put(ki.next(), vi.next());
			}
			out.nodes.add(node);
		}
		HashMap<Long, Way> wayMap = new HashMap<Long, Way>();
		for (OSMContainer.Way rawWay : contents.ways) {
			Way way = new Way(rawWay.getId());
			Iterator<String> ki = rawWay.getKeysList().iterator(), vi = rawWay.getValsList().iterator();
			while (ki.hasNext()) {
				way.map.put(ki.next(), vi.next());
			}
			Point rootPoint = null;
			Iterator<Integer> xi = rawWay.getXsList().iterator(), zi = rawWay.getZsList().iterator();
			while(xi.hasNext()) {
				Point p = new Point(xi.next(), zi.next());
				if (rootPoint == null) {
					rootPoint = p;
					way.points.add(p);
				} else {
					way.points.add(new Point(p.x + rootPoint.x, p.z + rootPoint.z));
				}
			}
			way.calculateBbox();
			wayMap.put(rawWay.getId(), way);
			out.ways.add(way);
		}
		for (OSMContainer.Relation rawRelation : contents.relations) {
			Relation relation = new Relation(rawRelation.getId());
			Iterator<String> ki = rawRelation.getKeysList().iterator(), vi = rawRelation.getValsList().iterator();
			while (vi.hasNext()) {
				relation.map.put(ki.next(), vi.next());
			}
			for (long id : rawRelation.getWaysList()) {
				Way way = wayMap.get(id);
				if (way != null) {
					relation.ways.add(way);
				}
			}
			relation.calculateBbox();
			out.relations.add(relation);
		}
		return out;
	}

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
			URL url = new URL(URLs.OSM);
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
			MyLogger.LOGGER.warning(MyLogger.printException(e));
			return null;
		}
	}

	private static FileContents getFileContents(int regionx, int regionz) throws IOException, InterruptedException, DataFormatException {
		
		setRectangles();
		FileContents out = new FileContents();
		int x = regionx * 512, z = regionz * 512;
		Rectangle myRectangle = new Rectangle(x, z, 512, 512);
		String rootAddress = BucketInfo.map.get("chunkmapper-osm2") + "/f_";
		for (Rectangle r : rectangles) {
			if (myRectangle.intersects(r)) {
				URL url = new URL(rootAddress + r.x + "_" + r.y + "_" + r.width + "_" + r.height);
				
				out.append(readFile(url));
			}
		}
		return out;
	}

	private static FileContents readFile(URL url) throws IOException, DataFormatException {
		synchronized(getLock(url)) {
			
			if (cache2 != null && cache2.containsKey(url)) {
				return cache2.get(url);
			}
			
			FileContents out = new FileContents();
			byte[] data;
			File cache = new File(CACHE, url.getPath().substring(1));
			if (FileValidator.checkValid(cache)) {
				data = Zip.readFully(new FileInputStream(cache));
			} else {
				HttpGet httpGet = new HttpGet(url.toString());
				HttpResponse response = httpclient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				data = Zip.inflate(entity.getContent());
				
				EntityUtils.consumeQuietly(entity);
				httpGet.releaseConnection();
				
				Zip.writeFully(cache, data);
				FileValidator.setValid(cache);
			}

			DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
			try {
				while(true) {
					byte type = in.readByte();
					int len = in.readInt();
					data = new byte[len];
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
			if (cache2 != null) cache2.put(url, out);
			return out;
		}
	}

}
