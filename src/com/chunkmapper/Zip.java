package com.chunkmapper;

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

	public static void zipOver(File src, File dest) throws IOException, DataFormatException {
		byte[] data = new byte[(int) src.length()], data2 = new byte[(int) src.length()];
		FileInputStream in = new FileInputStream(src);
		in.read(data);
		in.close();

		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
		FileOutputStream out = new FileOutputStream(dest);
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
		out.write(data2, 0, cl);
		out.close();
	}
	private static class InputBuffer {
		public final int bytesRead;
		public final byte[] data = new byte[8192];
		public InputBuffer(InputStream in) throws IOException {
			bytesRead = in.read(data);
		}
	}
	public static byte[] inflate(InputStream in) throws IOException, DataFormatException {
		int fullLength = (new DataInputStream(in)).readInt();
		ArrayList<InputBuffer> buffers = new ArrayList<InputBuffer>();
		while (true) {
			InputBuffer b = new InputBuffer(in);
			if (b.bytesRead == -1) {
				break;
			}
			buffers.add(b);
		}
		in.close();
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
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		data = new byte[fullLength];
		inflater.inflate(data);
		inflater.end();
		
		return data;
	}
	public static byte[] inflate(File f) throws IOException, DataFormatException {
		byte[] data = new byte[(int) f.length() - 4];
		
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		int fullLength = in.readInt();
		in.readFully(data);
		in.close();
		
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		data = new byte[fullLength];
		inflater.inflate(data);
		inflater.end();
		
		return data;
		
		
	}

}
