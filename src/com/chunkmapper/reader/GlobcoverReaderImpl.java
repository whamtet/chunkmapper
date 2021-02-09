package com.chunkmapper.reader;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import com.chunkmapper.FileValidator;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Matthewmatics;

public class GlobcoverReaderImpl implements GlobcoverReader {
	public static final int REGION_WIDTH = 50;
	public static final File CACHE_DIR = new File(Utila.CACHE, "mat");
	static {
		CACHE_DIR.mkdirs();
	}
	private final int datax, dataz;
	private Globcover[][] data;
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

	public GlobcoverReaderImpl(int regionx, int regionz) throws FileNotYetAvailableException, IOException, InterruptedException {
		int globx = Matthewmatics.div(regionx, REGION_WIDTH), globz = Matthewmatics.div(regionz, REGION_WIDTH);

		File cacheFile = new File(CACHE_DIR, "f_" + globx + "_" + globz + Utila.BINARY_SUFFIX);
		BufferedImage im;
		
		int i = Matthewmatics.mod(regionz, REGION_WIDTH), j = Matthewmatics.mod(regionx, REGION_WIDTH);
		int totalWidth = 512 * REGION_WIDTH / 10;
		int x1 = j * totalWidth / REGION_WIDTH, x2 = (j + 1) * totalWidth / REGION_WIDTH;
		int z1 = i * totalWidth / REGION_WIDTH, z2 = (i + 1) * totalWidth / REGION_WIDTH;
		datax = x2 - x1;
		dataz = z2 - z1;
		data = new Globcover[dataz][datax];
		
		if (FileValidator.checkValid(cacheFile)) {
			im = ImageIO.read(cacheFile);
		} else {
			String base = "/f_" + globx + "_" + globz + Utila.BINARY_SUFFIX;
			URL url = new URL(BucketInfo.mat() + base);

			try {
				im = ImageIO.read(url);
				//now save it as well.
				ImageIO.write(im, "png", cacheFile);
				FileValidator.setValid(cacheFile);
			} catch (Exception e) {
				MyLogger.LOGGER.warning(String.format("No Globcover data at regionx: %s, regionz: %s", regionx, regionz));
				//binaries need to be sorted.
				for (int z = 0; z < dataz; z++) {
					for (int x = 0; x < datax; x++) {
						data[z][x] = Globcover.NoData;
					}
				}
				return;
			}
		}

		byte[] buffer = ((DataBufferByte) im.getRaster().getDataBuffer()).getData();


		
		int xdrag = regionx < 0 ? totalWidth - im.getWidth() : 0;
		int zdrag = regionz < 0 ? totalWidth - im.getHeight() : 0;
		x1 -= xdrag; x2 -= xdrag;
		z1 -= zdrag; z2 -= zdrag;

		int imWidth = im.getWidth(), imHeight = im.getHeight();
		for (int z = z1; z < z2; z++) {
			for (int x = x1; x < x2; x++) {
				if (z < 0 || z >= imHeight || x < 0 || x >= imWidth) {
					data[z-z1][x-x1] = Globcover.NoData;
				} else {
					data[z-z1][x-x1] = Globcover.getGlobcover(buffer[z*imWidth + x]);
				}
			}
		}
	}
	public Globcover getGlobcover(int i, int j) {
		i = probRound(i * dataz / 512.);
		j = probRound(j * datax / 512.);
		if (i == dataz)
			i--;
		if (j == datax)
			j--;
		return data[i][j];
	}
	public int getValueij(int i, int j) {
		Globcover c = getGlobcover(i, j);
		Globcover[] ds = Globcover.values();
		for (int k = 0; k < ds.length; k++) {
			if (ds[k].equals(c)) {
				return k;
			}
		}
		throw new RuntimeException("impossible");
	}


	public boolean mostlyLand() {
		if (mostlyLand == null) {
			//ook
			int k = 0;
			for (int i = 0; i < dataz; i++) {
				for (int j = 0; j < datax; j++) {
					if (data[i][j] != Globcover.Water)
						k++;
				}
			}
			mostlyLand = k > dataz * datax / 2;
		}
		return mostlyLand;
	}
	



}
