package com.chunkmapper.process;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.chunkmapper.Zip;

public class CopyHeights {
	public static class StreamObject {
		private byte[] data; 
		private String name;
		
		public byte[] getData() {
			return data;
		}
		public String getName() {
			return name;
		}
		public StreamObject(String name, byte[] data) {
			this.name = name;
			this.data = data;
		}
	}
	public static ArrayList<StreamObject> readHeightsStream(InputStream in) throws IOException {
		byte[] data = Zip.readFully(in);
		in.close();
		ZipInputStream in2 = new ZipInputStream(new ByteArrayInputStream(data));
		ZipEntry entry;
		ArrayList<StreamObject> out = new ArrayList<StreamObject>();
		while ((entry = in2.getNextEntry()) != null) {
			if (entry.getName().endsWith(".hgt")) {
				String name = entry.getName().split("/")[1];

				byte[] uncompressed = Zip.readFully(in2);
				out.add(new StreamObject(name, Zip.zipToArray(uncompressed)));
			}
		}
		in2.close();
		return out;
	}
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Downloads/U15.zip");
		readHeightsStream(new FileInputStream(f));
	}

}
