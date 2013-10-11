package com.chunkmapper.heights;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.DataFormatException;
import java.util.zip.ZipInputStream;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Utila;
import com.chunkmapper.Zip;
import com.chunkmapper.admin.BucketInfo;

public class HGTFile2 {

	public static final int SIZE = 1201*1201*2;
	private static Object masterLock = new Object();
	private static HashMap<Point, Point> lockMap = new HashMap<Point, Point>();
	private static final File CACHE_DIR = new File(Utila.CACHE, "srtm");
	static {
		CACHE_DIR.mkdirs();
	}

	private static Point getLock(int lat, int lon) {
		synchronized(masterLock) {
			Point p = lockMap.get(new Point(lat, lon));
			if (p != null)
				return p;
			p = new Point(lat, lon);
			lockMap.put(p, p);
			return p;
		}
	}
	private static String getFilename(int lat, int lon) {
		String latPrefix = lat < 0 ? "S" : "N";
		String lonPrefix = lon < 0 ? "W" : "E";
		if (lat < 0) lat = -lat;
		if (lon < 0) lon = -lon;
		String latStr, lonStr;
		if (lat < 10) {
			latStr = "0" + lat;
		} else {
			latStr = "" + lat;
		}
		if (lon < 10) {
			lonStr = "00" + lon;
		} else if (lon < 100) {
			lonStr = "0" + lon;
		} else {
			lonStr = "" + lon;
		}
		String fileName = latPrefix + latStr + lonPrefix + lonStr + ".hgt";
		return fileName;
	}
	private static byte[] getData(int lat, int lon) throws IOException, InterruptedException, DataFormatException {

		synchronized(getLock(lat, lon)) {
			String fileName = getFilename(lat, lon);
			File f = new File(CACHE_DIR, fileName);
			if (FileValidator.checkValid(f)) {
				byte[] data = new byte[SIZE];
				DataInputStream in = new DataInputStream(new FileInputStream(f));
				in.readFully(data);
				in.close();
				return data;
			}
			//need to read in
			URL url = new URL(BucketInfo.getBucket("chunkmapper-heights2") + "/" + fileName);
//			ZipInputStream in = new ZipInputStream(url.openStream());
//			in.getNextEntry();
//			byte[] uncompressed = Zip.readFully(in);
//			in.close();
			byte[] uncompressed = Zip.inflate(url.openStream());

			FileOutputStream out = new FileOutputStream(f);
			out.write(uncompressed);
			out.close();
			FileValidator.setValid(f);
			return uncompressed;
		}
	}
	public static short[][] getHeights(int lat, int lon) throws IOException, InterruptedException, DataFormatException {
		short[][] out = new short[1201][1201];
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(getData(lat, lon)));
		for (int i = 0; i < 1201; i++) {
			for (int j = 0; j < 1201; j++) {
				short s = in.readShort();
				if (s < -1)
					s = -1;
				out[i][j] = s;
			}
		}
		in.close();
		return out;
	}


}
