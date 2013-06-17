package com.chunkmapper.process;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ProcessGlobcover {
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Downloads/Globcover2009_V2.3_Global_/GLOBCOVER_L4_200901_200912_V2.3.tif");
		File g = new File("/Users/matthewmolloy/Downloads/ww/transparent.tif");
		
		BufferedImage image = ImageIO.read(f);
		
		System.out.println(image.getWidth());
		
	}

}
