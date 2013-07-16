package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;

public class GlobcoverReaderImpl implements GlobcoverReader {
	private final short[][] data;
	private final Globcover[] indices;
	private Boolean mostlyLand;
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

	public GlobcoverReaderImpl(int regionx, int regionz) throws FileNotYetAvailableException, IOException {
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
	}
	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.GlobcoverReader#getGlobcover(int, int)
	 */
	@Override
	public Globcover getGlobcover(int i, int j) {
		i = probRound(i * 50 / 512.);
		j = probRound(j * 50 / 512.);
		return indices[data[i][j]];
	}


	public boolean mostlyWater(int z) {
		int numWaters = 0;
		z = z * 50 / 512;
		for (int j = 0; j < 512; j++) {
			int jd = j * 50 / 512;
			int index = data[z][jd];
			if (indices[index] == Globcover.Water) {
				numWaters++;
			}
		}
		return numWaters > 256;
	}

	/* (non-Javadoc)
	 * @see com.chunkmapper.reader.GlobcoverReader#mostlyLand()
	 */
	@Override
	public boolean mostlyLand() {
		if (mostlyLand == null) {
			//ook
			int k = 0;
			for (int i = 0; i < 50; i++) {
				for (int j = 0; j < 50; j++) {
					if (indices[data[i][j]] != Globcover.Water)
						k++;
				}
			}
			mostlyLand = k > 1250;
		}
		return mostlyLand;
	}
	public static void main(String[] args) throws Exception {
		double[] latlon = {-39.75, 174.277};
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		GlobcoverReader reader = new GlobcoverReaderImpl(regionx, regionz);
		System.out.println(reader.mostlyLand());
		
	}



}
