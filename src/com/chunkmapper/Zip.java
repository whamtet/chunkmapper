package com.chunkmapper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zip {
	/*
	 * Unzips byte[] data to File dest.  dest is stored as 
	 * 
	 * - an int containing zipped length.
	 * - an int containing unzipped length.
	 * - unzipped data.
	 */
	public static void zipOver(byte[] data, File dest) throws IOException {
		byte[] data2 = new byte[data.length];
		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
		DataOutputStream out = new DataOutputStream(new FileOutputStream(dest));
		out.writeInt(data.length);
		out.writeInt(cl);
		out.write(data2, 0, cl);
		out.close();
	}

	/*
	 * Helper Class
	 */
	private static class InputBuffer {
		public final int bytesRead;
		public final byte[] data = new byte[8192];

		public InputBuffer(InputStream in) throws IOException {
			bytesRead = in.read(data);
		}
	}
	
	public static byte[] readFully(InputStream in) throws IOException {
		ArrayList<InputBuffer> buffers = new ArrayList<InputBuffer>();
		while (true) {
			InputBuffer b = new InputBuffer(in);
			if (b.bytesRead == -1) {
				break;
			}
			buffers.add(b);
		}
//		in.close();
		int totalSize = 0;
		for (InputBuffer b : buffers) {
			totalSize += b.bytesRead;
		}
		byte[] data = new byte[totalSize];
		int i = 0;
		for (InputBuffer b : buffers) {
			for (int j = 0; j < b.bytesRead; j++) {
				data[i] = b.data[j];
				i++;
			}
		}
		return data;
	}
	
	public static byte[] inflate(InputStream in) throws IOException, DataFormatException {
		DataInputStream in2 = new DataInputStream(new BufferedInputStream(in));
		int fullLength = in2.readInt();
		int cl = in2.readInt();
		byte[] compressed = new byte[cl], uncompressed = new byte[fullLength];
		in2.readFully(compressed);
		in2.close();
		
		Inflater inflater = new Inflater();
		inflater.setInput(compressed);
		inflater.inflate(uncompressed);
		inflater.end();
		
		return uncompressed;
	}
	public static byte[] inflate(File f) throws IOException, DataFormatException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(f));
		DataInputStream in2 = new DataInputStream(in);
		int fullLength = in2.readInt();
		int cl = in2.readInt();
		byte[] compressed = new byte[cl], uncompressed = new byte[fullLength];
		in2.readFully(compressed);
		in2.close();
		
		Inflater inflater = new Inflater();
		inflater.setInput(compressed);
		inflater.inflate(uncompressed);
		inflater.end();
		
		return uncompressed;
	}
	public static void writeFully(File f, byte[] data) throws IOException {
		//FileOutputStream and DataOutputStream do not support integer return of successful bytes written
		//Presumably complete write is guaranteed
		FileOutputStream out = new FileOutputStream(f);
		out.write(data);
		out.close();
	}

}
