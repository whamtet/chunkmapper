package com.chunkmapper.process;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.HashSet;

import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RenderedOp;



public class ProcessGlobcover {
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Downloads/Globcover2009_V2.3_Global_/GLOBCOVER_L4_200901_200912_V2.3.tif");
		File g = new File("/Users/matthewmolloy/Downloads/ww/transparent.tif");
		String s = "/Users/matthewmolloy/Downloads/Globcover2009_V2.3_Global_/GLOBCOVER_L4_200901_200912_V2.3.tif";
		
		PlanarImage im = JAI.create("fileload", s).createInstance();
		int totalWidth = im.getWidth(), totalHeight = im.getHeight();
		int x = totalWidth / 2, y = totalHeight * 2 / 5;
		
		BufferedImage image = im.getAsBufferedImage(new Rectangle(x, y, 1000, 1000), im.getColorModel());
		byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		HashSet<Integer> set = new HashSet<Integer>();
		for (int i : buffer) {
			if (i < 0)
				i += 256;
			set.add(i);
		}
		System.out.println(set);

	}

}
