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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zip {
	//storage needs full length and then zipped lengths

	public static void zipOver(File src, File dest) throws IOException, DataFormatException {
		byte[] data = new byte[(int) src.length()], data2 = new byte[(int) src.length()];
		FileInputStream in = new FileInputStream(src);
		in.read(data);
		in.close();

		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(dest)));
		out.writeInt(data.length);
		out.writeInt(cl);
		out.write(data2, 0, cl);
		out.close();

	}
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
	public static byte[] zipToArray(byte[] data) throws IOException {
		byte[] data2 = new byte[data.length];
		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(out2);
		out.writeInt(data.length);
		out.writeInt(cl);
		out.write(data2, 0, cl);
		
		out.close();
		return out2.toByteArray();
	}
	public static InputStream zipToStream(byte[] data) throws IOException {
		byte[] data2 = new byte[data.length];
		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
		PipedInputStream in = new PipedInputStream();
		DataOutputStream out = new DataOutputStream(new PipedOutputStream(in));
		out.writeInt(data.length);
		out.writeInt(cl);
		out.write(data2, 0, cl);
		
		return in;
	}
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
	public static byte[] inflate(byte[] dataOrig) throws IOException, DataFormatException {
		ByteArrayInputStream in = new ByteArrayInputStream(dataOrig);
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

}
