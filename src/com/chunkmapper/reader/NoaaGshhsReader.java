package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;

public class NoaaGshhsReader {
	private final boolean[][] oceanMask = new boolean[512][512];

	public NoaaGshhsReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException {
		NoaaGshhsResourceInfo info = new NoaaGshhsResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file))
			throw new FileNotYetAvailableException();
		
		BufferedImage image = ImageIO.read(info.file);
		
		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		for (int i = 0; i < 12; i++) {
//			System.out.println(data[i]);
//		}
//		HashSet<Point3d> s = new HashSet<Point3d>();
//		PrintWriter pw = new PrintWriter("/Users/matthewmolloy/python/wms/data.csv");
		for (int i = 0; i < 512*512; i++) {
//			pw.println(data[i*4] == -1 ? 1 : 0);
			oceanMask[i/512][i%512] = data[i*4] != -1;
		}
//		pw.close();
	}
	public boolean hasOceanij(int i, int j) {
		return oceanMask[i][j];
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) throws Exception {
//		double[] latlon = geocode.core.placeToCoords("brisbane");
//		int regionx = (int) (latlon[1] * 3600 / 512), regionz = -(int) (latlon[0] * 3600 / 512);
//		NoaaGshhsReader reader = new NoaaGshhsReader(regionx + 1, regionz - 1);
//		
//
//	}

}
