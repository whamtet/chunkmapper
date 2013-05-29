package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;

public class GlobcoverReader {
	private final short[][] data;
	private final Globcover[] indices;
	private final Random random = new Random();

	//only works for positive, otherwise use sign flipping
	private int probRound(double d) {
		int floor = (int) d;
		if (random.nextDouble() > d - floor) {
			return floor;
		} else {
			return floor + 1;
		}
	}

	public GlobcoverReader(int regionx, int regionz) throws FileNotYetAvailableException, IOException {
		GlobcoverResourceInfo info = new GlobcoverResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file))
			throw new FileNotYetAvailableException();


		indices = Globcover.makeArray(info.file);
		BufferedImage image = ImageIO.read(info.file);
		byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		data = new short[GlobcoverResourceInfo.PICTURE_SIZE][GlobcoverResourceInfo.PICTURE_SIZE];
		for (int i = 0; i < GlobcoverResourceInfo.PICTURE_SIZE; i++) {
			for (int j = 0; j < GlobcoverResourceInfo.PICTURE_SIZE; j++) {
				short k = buffer[i*GlobcoverResourceInfo.PICTURE_SIZE + j];
				if (k < 0) k += 256;
				data[i][j] = k;
			}
		}
		//		
		//		System.out.println(data.length);
		//		System.out.println(512*512);
		//		HashSet<Byte> s = new HashSet<Byte>();
		//		for (byte b : data)
		//			s.add(b);
		//		for (byte b : s)
		//			System.out.println(b);
	}
	//	public Globcover getGlobcover(int x, int z) {
	//		x = com.chunkmapper.math.Math.mod(x, 512);
	//		z = com.chunkmapper.math.Math.mod(z, 512);
	//		
	//		int i = probRound(z*50/512.), j = probRound(x*50/512.);
	//		return indices[data[i][j]];
	//	}
	public Globcover getGlobcover(int i, int j) {
		i = probRound(i * 50 / 512.);
		j = probRound(j * 50 / 512.);
		return indices[data[i][j]];
	}
	//	public static Globcover[][] getAllData(int regionx, int regionz) throws FileNotYetAvailableException, IOException {
	//		GlobcoverReader r = new GlobcoverReader(regionx, regionz);
	//		return r.getAllData();
	//	}
	//	private Globcover[][] getAllData() {
	//		Globcover[][] out = new Globcover[512][512];
	//		for (int i = 0; i < 512; i++) {
	//			for (int j = 0; j < 512; j++) {
	//				int id = probRound(i*50/512.), jd = probRound(j*50/512.);
	//				out[i][j] = indices[data[id][jd]];
	//			}
	//		}
	//		return out;
	//	}


}
