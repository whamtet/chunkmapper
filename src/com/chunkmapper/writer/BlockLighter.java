package com.chunkmapper.writer;

import com.chunkmapper.enumeration.Blocka;

public class BlockLighter {
	public static byte[][][] getBlockLights(byte[][][] Blocks) {
		byte[][][] out = new byte[256][16][16];
		for (int y = 0; y < 256; y++) {
			for (int z = 0; z < 16; z++) {
				for (int x = 0; x < 16; x++) {
					out[y][z][x] = 0;
				}
			}
		}
		for (int y = 0; y < 256; y++) {
			for (int z = 0; z < 16; z++) {
				for (int x = 0; x < 16; x++) {
					switch(Blocks[y][z][x]) {
					case Blocka.Redstone_Torch_Lit:
					case Blocka.Glow_Stone:
					addLight(out, y, z, x, (byte) 11);
					}
				}
			}
		}

		return out;
	}
	private static void addLight(byte[][][] lights, int y, int z, int x, byte brightness) {
//		int r = brightness - 1;
		int r = brightness*2;

		if (y < r) r = y;
		if (255 - y < r) r = 255 - r;
		if (z < r) r = z;
		if (15 - z < r) r = 15 - z;
		if (x < r) r = x;
		if (15 - x < r) r = 15 - x;

		//
		for (int yd = y - r; yd <= y + r; yd++) {
			for (int zd = z - r; zd <= z + r; zd++) {
				for (int xd = x - r; xd <= x + r; xd++) {
					int xdist = xd - x, ydist = yd - y, zdist = zd - z;
					int dist = (int) Math.sqrt(xdist*xdist + ydist*ydist + zdist*zdist);

					byte newBrightness = (byte) (brightness - dist/2);
					if (lights[y][z][x] < newBrightness) lights[y][z][x] = newBrightness;
				}
			}
		}
	}

}
