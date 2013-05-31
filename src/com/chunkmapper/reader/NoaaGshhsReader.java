package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;

public class NoaaGshhsReader {
	//	private final boolean[][] oceanMask = new boolean[512][512];
	private final byte[][] oceanMask = new byte[512][512];
	public static final int LAND = 0, OCEAN = 1, COAST = 2;

	public NoaaGshhsReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException {
		NoaaGshhsResourceInfo info = new NoaaGshhsResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file))
			throw new FileNotYetAvailableException();

		BufferedImage image = ImageIO.read(info.file);

		byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

		for (int i = 0; i < 512*512; i++) {
			oceanMask[i/512][i%512] = data[i*4] != -1 ? (byte) 1 : (byte) 0;
		}
		//now iterate to add coastlines
		for (int i = 0; i < 512; i++) {
			int k1 = i - 1, k2 = i + 1;
			if (k1 < 0) k1 = 0;
			if (k2 > 511) k2 = 511;
			for (int j = 0; j < 512; j++) {
				if (oceanMask[i][j] == LAND) {
					boolean isCoast = false;
					int l1 = j - 1, l2 = j + 1;
					if (l1 < 0) l1 = 0;
					if (l2 > 511) l2 = 511;
					for (int k = k1; k <= k2; k++) {
						for (int l = l1; l <= l2; l++) {
							isCoast |= oceanMask[k][l] == OCEAN;
						}
					}
					if (isCoast)
						oceanMask[i][j] = COAST;
				}
			}
		}
	}
	public byte getVal(int i, int j) {
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
