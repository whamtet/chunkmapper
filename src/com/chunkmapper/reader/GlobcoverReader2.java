package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.chunkmapper.Utila;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Matthewmatics;

public class GlobcoverReader2 {
	public static final int REGION_WIDTH = 50;
	private final boolean isPolar;
	private short[][] data;
	private Globcover[] indices;
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

	public GlobcoverReader2(int regionx, int regionz) throws FileNotYetAvailableException, IOException {
		int globx = Matthewmatics.mod(regionx, REGION_WIDTH), globz = Matthewmatics.mod(regionz, REGION_WIDTH);
		File f = new File("globcover/f_" + globx + "_" + globz + Utila.BINARY_SUFFIX);
		System.out.println(f);
		System.out.println(f.exists());
		isPolar = true;
		//		indices = Globcover.makeArray(info.file);
//		BufferedImage image = ImageIO.read(info.file);
//		byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		data = new short[GlobcoverResourceInfo.PICTURE_SIZE][GlobcoverResourceInfo.PICTURE_SIZE];
//		for (int i = 0; i < GlobcoverResourceInfo.PICTURE_SIZE; i++) {
//			for (int j = 0; j < GlobcoverResourceInfo.PICTURE_SIZE; j++) {
//				short k = buffer[i*GlobcoverResourceInfo.PICTURE_SIZE + j];
//				if (k < 0) k += 256;
//				data[i][j] = k;
//			}
//		}
	}
	public Globcover getGlobcover(int i, int j) {
		i = probRound(i * 50 / 512.);
		j = probRound(j * 50 / 512.);
		return indices[data[i][j]];
	}


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
		double[] latlon = geocode.core.placeToCoords("new plymouth, nz");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		new GlobcoverReader2(regionx, regionz);
		
	}



}
