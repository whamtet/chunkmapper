package com.chunkmapper.reader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import com.chunkmapper.FileValidator;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;
import com.chunkmapper.resourceinfo.ResourceInfo;

public abstract class Reader {

	public static ArrayList<Byte> getBlueIndices(File f) throws IOException {
		return getIndices(f, new int[] {62, 205, 253, 2, 183, 242});
	}
	public static void printPallette(File f) throws IOException {
		DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
		//read 8 byte header
		dataStream.skip(8);
		while (true) {
			dataStream.readInt();
			char[] nameArr = new char[4];
			nameArr[0] = (char) dataStream.readByte();
			nameArr[1] = (char) dataStream.readByte();
			nameArr[2] = (char) dataStream.readByte();
			nameArr[3] = (char) dataStream.readByte();
			String name = new String(nameArr);

			if (name.equals("PLTE")) {
				for (int i = 0; i < 256; i++) {
					int r = dataStream.readByte();
					int g = dataStream.readByte();
					int b = dataStream.readByte();
					if (r < 0) r += 256;
					if (g < 0) g += 256;
					if (b < 0) b += 256;
					System.out.println(r + ", " + g + ", " + b);
				}
			}
		}
	}
	public static ArrayList<Byte> getIndices(File f, int[] indices) throws IOException {
		//because bloody java is too stupid to implement this
		DataInputStream dataStream = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
		//read 8 byte header
		dataStream.skip(8);
		while (true) {
			int len = dataStream.readInt();
			char[] nameArr = new char[4];
			nameArr[0] = (char) dataStream.readByte();
			nameArr[1] = (char) dataStream.readByte();
			nameArr[2] = (char) dataStream.readByte();
			nameArr[3] = (char) dataStream.readByte();
			String name = new String(nameArr);

			if (name.equals("PLTE")) {
				ArrayList<Byte> out = new ArrayList<Byte>(10);
				for (int i = 0; i < 256; i++) {
					int r = dataStream.readByte();
					int g = dataStream.readByte();
					int b = dataStream.readByte();
					if (r < 0) r += 256;
					if (g < 0) g += 256;
					if (b < 0) b += 256;
					for (int j = 0; j < indices.length/3; j++) {
						if (r == indices[j*3] && g == indices[j*3 + 1] && b == indices[j*3 + 2]) {
							out.add((byte) i);
							break;
						}
					}
					//					if (r != 0 || g != 0 || b != 0) {
					//						out.add((byte) i);
					//					}
				}
				dataStream.close();
				return out;
			} else {
				dataStream.skip(len + 4);
			}

		}
	}

	protected Reader(ResourceInfo resourceInfo, UberDownloader uberDownloader) throws InterruptedException, IOException, FileNotYetAvailableException {
		

	}
	//	public static void main(String[] args) throws Exception {
	//		File f = new File("/Users/matthewmolloy/Downloads/wmsMap-20130502_013137.png");
	//		BufferedImage image = ImageIO.read(f);
	//		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	//		System.out.println(data.length);
	//		System.out.println(512*512*4);
	//	}

}
