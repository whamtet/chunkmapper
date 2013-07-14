package com.chunkmapper;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.imageio.ImageIO;

public class CheckGlobcover {
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Downloads/RenderData.png");
		BufferedImage image = ImageIO.read(f);
		byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		
		for (int i = 0; i < buffer.length; i++) {
			int b = buffer[i];
			if (b != 2) {
				buffer[i] = 0;
			}
		}
		File g = new File("/Users/matthewmolloy/Downloads/checkGlobcover.png");
		ImageIO.write(image, "png", g);
		System.out.println("done");

	}

}
