package com.chunkmapper.gui.process;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class IconCreator {

	/**
	 * @param args
	 */
	private static void viewImage(BufferedImage image) throws IOException {
		ImageIO.write(image, "png", new File("image.png"));
		Runtime.getRuntime().exec("open image.png");
	}
	private static BufferedImage getIcon() throws IOException {
		File f = new File("/Users/matthewmolloy/Pictures/gps (1).png");
		BufferedImage image = ImageIO.read(f);
		image = image.getSubimage(22, 22, 128, 128);
		return image;
	}
	private static BufferedImage scaleImage(BufferedImage original, int newWidth, int newHeight) {
		BufferedImage resized = new BufferedImage(newWidth, newHeight, original.getType());
	    Graphics2D g = resized.createGraphics();
	    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g.drawImage(original, 0, 0, newWidth, newHeight, 0, 0, original.getWidth(), original.getHeight(), null);
	    g.dispose();
	    return resized;
	}
	public static void main(String[] args) throws Exception {
		BufferedImage icon = getIcon();
		int size = 16;
		ImageIO.write(scaleImage(icon, size, size), "png", new File(String.format("src/images/chunkmapper-icon-%sx%s.png", size, size)));

	}

}
