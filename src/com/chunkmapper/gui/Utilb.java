package com.chunkmapper.gui;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
//Can't remember what this is for
public class Utilb {
	private static Cursor cursor;
//	public static void main(String[] args) throws Exception {
//		printDimensions("src/images/START.png");
//		printDimensions("src/images/CANCEL.png");
//		printDimensions("src/images/CHOOSE_START_POINT.png");
//	}
	public static Cursor getCustomCursor() {
		if (cursor == null) {
			Toolkit toolkit = Toolkit.getDefaultToolkit();
		    Image cursorImage = toolkit.getImage("src/images/color_icons_green_home.png");
		    Point cursorHotSpot = new Point(15, 49);
		    cursor = toolkit.createCustomCursor(cursorImage, cursorHotSpot, "Cursor");
		}
		return cursor;
	}
	
	
}
