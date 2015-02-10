package com.chunkmapper.writer;

import com.chunkmapper.enumeration.Stairs;
import com.chunkmapper.enumeration.StraightRail;

public class MineWriter {
	private byte[][][] blocks = new byte[256][512][512], data = new byte[256][512][512];
	private static enum Orientation {
		NORTH, EAST, SOUTH, WEST;
	}
	
	public MineWriter(int relx, int relz, int h) {
		boolean goEast = relx < 256;
		if (relz == 511) relz--;
		byte stepData = goEast ? Stairs.Ascending_West.val : Stairs.Ascending_East.val;
		byte railData = goEast ? StraightRail.EastDown.val : StraightRail.WestDown.val;
	}
}
