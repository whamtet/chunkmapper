package com.chunkmapper;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.imageio.ImageIO;

public class Test {
	public static void main(String[] args) throws Exception {
		BufferedImage im1 = ImageIO.read(new File("/Users/matthewmolloy/Downloads/in.png"));//, im2 = deepCopy(im1);
//		AffineTransform a1 = new AffineTransform(new double[] {.5, Math.sqrt(3)/2, -.5, Math.sqrt(3)/2, 0, 1048});
		AffineTransform a1 = new AffineTransform(new double[] {1, -.5, 1, .5, 0, 1048});
//		AffineTransform a1 = new AffineTransform(new double[] {1, 0, 0, 1});
//		AffineTransform a1 = new AffineTransform(new double[] {Math.sqrt(3) / 2, -.5, Math.sqrt(3)/2, .5});
//		AffineTransform a1 = AffineTransform.getRotateInstance(Math.PI/4);
		a1.invert();
		AffineTransformOp op1 = new AffineTransformOp(a1, AffineTransformOp.TYPE_BILINEAR);
		im1 = op1.filter(im1, null);
		
		ImageIO.write(im1, "png", new File("/Users/matthewmolloy/Downloads/out.png"));
		Runtime.getRuntime().exec("open /Users/matthewmolloy/Downloads/out.png");
		System.out.println("done");
	}
	private static BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}

}