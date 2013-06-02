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
	//currently only abstracts height reader
	protected short[][] cache = new short[HeightsResourceInfo.LEN][HeightsResourceInfo.LEN];
	public final int min, max;
	public final boolean allWater;

	public static ArrayList<Byte> getBlueIndices(File f) throws IOException {
		return getIndices(f, new int[] {62, 205, 253, 2, 183, 242});
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
		if (!FileValidator.checkValid(resourceInfo.file)) {
			uberDownloader.heightsDownloader.addTask(resourceInfo.regionx, resourceInfo.regionz);
			throw new FileNotYetAvailableException();
		}

		//now we're ready to download
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(resourceInfo.file));
			byte[] data = new byte[HeightsResourceInfo.FILE_LENGTH];
			in.read(data);
			in.close();

		ShortBuffer shortBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
		boolean allWater = true;
		int min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;
		for (int i = 0; i < HeightsResourceInfo.LEN; i++) {
			for (int j = 0; j < HeightsResourceInfo.LEN; j++) {
				short s = shortBuffer.get();
				allWater &= s < 0;
				if (s < min) min = s;
				if (s > max) max = s;
				cache[i][j] = s;
				//				cache[i][j] = shortBuffer.get();
			}
		}
		this.min = min;
		this.max = max;
		this.allWater = allWater;

	}
	//	public static void main(String[] args) throws Exception {
	//		File f = new File("/Users/matthewmolloy/Downloads/wmsMap-20130502_013137.png");
	//		BufferedImage image = ImageIO.read(f);
	//		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	//		System.out.println(data.length);
	//		System.out.println(512*512*4);
	//	}

}
