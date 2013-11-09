package com.chunkmapper.mapper;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashSet;

import javax.imageio.ImageIO;

import com.chunkmapper.nbt.CompoundTag;
import com.chunkmapper.nbt.ListTag;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;

public class Mapper {
	private static final int[] brightnessLookup = new int[256];
	private static final int WIDTH = 1024, HEIGHT = 512;
	static {
		int g_MapsizeY = 256;
		for (int y = 0; y < g_MapsizeY; ++y) {
			brightnessLookup[y] = (int) ((100.0f / (1.0f + Math.exp(- (1.3f * (y * 200. / g_MapsizeY) / 16.0f) + 6.0f))) - 91);
		}
	}

	public static class RegionContents {
		public byte[][][] data = new byte[256][512][512];
		public byte[][][] blocks = new byte[256][512][512];
	}
	public static RegionContents readRegion(File f) throws IOException {
		RegionFile regionFile = new RegionFile(f);
		RegionContents out = new RegionContents();
		
		for (int chunkx = 0; chunkx < 32; chunkx++) {
			for (int chunkz = 0; chunkz < 32; chunkz++) {
				if (regionFile.hasChunk(chunkx, chunkz)) {
					DataInputStream in = regionFile.getChunkDataInputStream(chunkx, chunkz);
					CompoundTag root = NbtIo.read(in);
					in.close();
					
					CompoundTag Level = root.getCompound("Level");
					ListTag<CompoundTag> Sections = (ListTag<CompoundTag>) Level.getList("Sections");
					for (int i = 0; i < Sections.size(); i++) {
						CompoundTag section = Sections.get(i);
						int Y = section.getByte("Y");
						byte[] Data = section.getByteArray("Data");
						byte[] Blocks = section.getByteArray("Blocks");
						for (int j = 0; j < 4096; j += 2) {
							int y = Y * 16 + j / 256;
							int z = (j / 16) % 16;
							int x = j % 16;
							
							out.blocks[y][z + chunkz*16][x + chunkx*16] = Blocks[j];
							out.blocks[y][z + chunkz*16][x + chunkx*16 + 1] = Blocks[j+1];
							out.data[y][z + chunkz*16][x + chunkx*16] = (byte) (Data[j/2] & 15);
							out.data[y][z + chunkz*16][x + chunkx*16 + 1] = (byte) (Data[j/2] >> 4);
						}
					}
				}
			}
		}
		regionFile.close();
		return out;
	}
	private static int getColor(int[] color, int del) {
		int r = color[0] + del, g = color[1] + del, b = color[2] + del;
		int a = color[3];
		if (r < 0) r = 0;
		if (g < 0) g = 0;
		if (b < 0) b = 0;
		if (r > 255) r = 255;
		if (g > 255) g = 255;
		if (b > 255) b = 255;
		return a << 24 | r << 16 | g << 8 | b;
	}
	public static void setPixel(BufferedImage im, int x, int y, int block, double brightnessAdjustment) {
		if (block < 0) block += 256;
		if (x < 0 || y < 0) return;
		int sub = (int) (brightnessAdjustment * MapColors.brightness[block] / 323 + .21);
		int[] color = MapColors.colors[block];
		
		int t = getColor(color, sub);
		int l = getColor(color, sub-17);
		int d = getColor(color, sub-27);
		
//		int t = color[1]+sub << 16 | color[2]+sub << 8 | color[3]+sub;
//		int l = color[1]-17+sub << 16 | color[2]-17+sub << 8 | color[3]-17+sub;
//		int d = color[1]-27+sub << 16 | color[2]-27+sub << 8 | color[3]-27+sub;
		
		int width = WIDTH - x, height = HEIGHT - y;
		if (width > 4) width = 4;
		if (height > 4) height = 4;
		
		for (int j = 0; j < width; j++) {
			
			im.setRGB(x + j, y, t);
		}
		for (int i = 1; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int c = j < 2 ? d : l;
				im.setRGB(x+j, y+i, c);
			}
		}
		
	}
	
	private static void getPng() throws IOException {
		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/np/region/r.0.0.mca");
		RegionContents regionContents = readRegion(f);
		
		
		BufferedImage im = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		//let m: (x, z) -> (i, j)
		//m = [256; 0] + [-256/512 256/512; 512/512 512/512] - y
		
		for (int y = 0; y < 20; y++) {
			System.out.println(y);
			for (int z = 0; z < 512; z++) {
				for (int x = 0; x < 512; x++) {
					int i = 256 - x/2 + z/2 - y;
					int j = x + z;
					setPixel(im, j, i, regionContents.blocks[y][z][x], brightnessLookup[y]);
//					im.setRGB(j, i, MapColors.colors[regionContents.blocks[y][z][x]]);
				}
			}
		}
		ImageIO.write(im, "png", new File("test.png"));
		Runtime.getRuntime().exec("open test.png");
	}
	
	public static void main(String[] args) throws MalformedURLException, URISyntaxException, IOException {
		getPng();
	}
	
	
	
}
