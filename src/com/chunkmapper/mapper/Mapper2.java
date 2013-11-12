package com.chunkmapper.mapper;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chunkmapper.Point;
import com.chunkmapper.mapper.Mapper.RegionContents;
import com.chunkmapper.parser.Nominatim;

public class Mapper2 {
	private static final int[] brightnessLookup = new int[256];
	private static final int SCALE = 3;
	private static final int WIDTH = (int) (512 * Math.sqrt(3)) * SCALE;
	private static final int HEIGHT = 256 * 3 * SCALE;
	static {
		int g_MapsizeY = 256;
		for (int y = 0; y < g_MapsizeY; ++y) {
			brightnessLookup[y] = (int) ((100.0f / (1.0f + Math.exp(- (1.3f * (y * 200. / g_MapsizeY) / 16.0f) + 6.0f))) - 91);
		}
	}

	private static class Point3d {
		public final int x, y, z;
		public final int color;
		//		public Point3d(int x, int y, int z) {
		//			this.x = x;
		//			this.y = y;
		//			this.z = z;
		//		}
		public Point3d(double x, double y, double z, int color) {
			this.x = (int) Math.floor(x);
			this.y = (int) Math.floor(y);
			this.z = (int) Math.floor(z);
			this.color = color;
		}
		public Point3d(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
			color = 0;
		}
	}
	private static Point3d getPoint(int i, int j) {
		i /= SCALE;
		j /= SCALE;
		double root3 = Math.sqrt(3);

		int i1 = 256;
		int i2 = 512;
		int i3 = 256 * 3;
		double j3 = 256 * root3;

		int white = 255 << 24 | 255 << 18 | 255 << 9 | 255;
		int blue = 255 << 24 | 255;
		int red =  255 << 24 | 244 << 18;

		double x, y, z;

		x = - (i - i1) + j / root3;
		z = (i - i1) + j / root3;
		if (0 <= x && x < 512 &&
				0 <= z && z < 512)
			return new Point3d(x, 255, z, white);


		y = - (i - i2) + j / root3;
		z = 2 * j / root3;
		if (0 <= y && y < 256 &&
				0 <= z && z < 512)
			return new Point3d(0, y, z, red);

		x = 2 * (j - j3) / root3;
		y = - (i - i3) - (j - j3) / root3;

		if (0 <= x && x < 512 &&
				0 <= y && y < 256)
			return new Point3d(x, y, 511, blue);

		return null;
	}
	private static Point3d getPoint(RegionContents regionContents, Point3d p) {
		if (p == null) return null;
		for (int x = p.x, y = p.y, z = p.z; x < 512 && 0 <= y && 0 <= z; x++, y--, z--) {
			byte b = regionContents.blocks[y][z][x];
			if (b != 0) {
				return new Point3d(x, y, z);
			}
		}
		return null;
	}

	private static BufferedImage getPng(File regionDir, int regionx, int regionz) throws IOException {

		BufferedImage im = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		File f = new File(regionDir, String.format("r.%s.%s.mca", regionx, regionz));
		//		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/np/region/r.-1.2.mca");
		RegionContents regionContents = Mapper.readRegion(f);

		for (int i = 0; i < HEIGHT; i += SCALE) {
			for (int j = 0; j < WIDTH; j += SCALE) {
				Point3d p = getPoint(regionContents, getPoint(i, j));
				if (p != null) {
					byte b = regionContents.blocks[p.y][p.z][p.x];
					setPixel(im, j, i, b, brightnessLookup[p.y]);
					//					if (b < 0) b += 128;
					//					if (b != 0) {
					//						int[] color = MapColors.colors[b];
					//						im.setRGB(j, i, 255 << 24 | color[0] << 16 | color[1] << 8 | color[2]);
					//					}
					//					im.setRGB(j, i, p.color);
				}
			}
		}

		return im;

		//		ImageIO.write(im, "png", new File("test.png"));
		//		Runtime.getRuntime().exec("open test.png");
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

	private static void postImage(BufferedImage im, int regionx, int regionz, Point rootPoint) throws ClientProtocolException, IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(im, "png", out);
		out.close();

		//		String url = "https://selfsolve.apple.com/wcResults.do";
		//		URL obj = new URL(url);
		//		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		// 
		//		//add reuqest header
		//		con.setRequestMethod("POST");
		// 
		//		URLEncoder.
		//		// Send post request
		//		con.setDoOutput(true);
		//		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		//		wr.writeBytes(urlParameters);
		//		wr.flush();
		//		wr.close();



		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://gae-mcmap.appspot.com/upload");

		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("image", new ByteArrayBody(out.toByteArray(), "image/png", "image"));
		reqEntity.addPart("regionx", new StringBody(regionx + rootPoint.x + ""));
		reqEntity.addPart("regionz", new StringBody(regionz + rootPoint.z + ""));
		post.setEntity(reqEntity);

		HttpResponse response = client.execute(post);

		//		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		//		String line = "";
		//		while ((line = rd.readLine()) != null) {
		//			System.out.println(line);
		//		}
	}

	private static void setPixel(BufferedImage im, int x, int y, int block, double brightnessAdjustment) {
		if (block < 0) block += 256;
		if (x < 0 || y < 0) return;
		int sub = 0;
		//		int sub = (int) (brightnessAdjustment * MapColors.brightness[block] / 323 + .21);
		int[] color = MapColors.colors[block];

		int t = getColor(color, sub);
		int l = getColor(color, sub-17);
		int d = getColor(color, sub-27);

		//		int t = color[1]+sub << 16 | color[2]+sub << 8 | color[3]+sub;
		//		int l = color[1]-17+sub << 16 | color[2]-17+sub << 8 | color[3]-17+sub;
		//		int d = color[1]-27+sub << 16 | color[2]-27+sub << 8 | color[3]-27+sub;

		int width = WIDTH - x, height = HEIGHT - y;
		if (width > 4) width = 4;
		if (height > 3) height = 3;

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
	public static void postRegion(File regionDir, int regionx, int regionz, Point rootPoint) throws IOException {
		if (upgradeNeeded(regionx, regionz, rootPoint)) {
			BufferedImage im = getPng(regionDir, regionx, regionz);
			postImage(im, regionx, regionz, rootPoint);
			ImageIO.write(im, "png", new File(String.format("pics/f_%s_%s.png", regionx, regionz)));
		}
			postImage(getPng(regionDir, regionx, regionz), regionx, regionz, rootPoint);
//		BufferedImage im = getPng(regionDir, regionx, regionz);
//		ImageIO.write(im, "png", new File(String.format("/Users/matthewmolloy/Downloads/f_%s_%s.png", regionx, regionz)));
	}
	
	private static boolean upgradeNeeded(int regionx, int regionz, Point rootPoint) throws IOException {
		URL url = new URL(String.format("http://gae-mcmap.appspot.com/needsupgrade?regionx=%s&regionz=%s",
				regionx + rootPoint.x, regionz + rootPoint.z));
		BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
		boolean b = Boolean.parseBoolean(r.readLine());
		r.close();
		return b;
	}

	public static void main(String[] args) throws IOException, URISyntaxException {
		File f = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/np3/region");
		double[] latlon = Nominatim.getPoint("new plymouth, nz");
//		System.out.println(latlon[0]);
//		System.out.println(latlon[1]);
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		System.out.println(regionx);
		System.out.println(regionz);
//		Point rootPoint = new Point(regionx, regionz);
//		System.out.println(upgradeNeeded(0, 110, rootPoint));
//		long l1 = System.nanoTime();
//		postRegion(f, 0, 0, rootPoint);
//		long l2 = System.nanoTime();
//		
//		System.out.println((l2 - l1) / 1000000);
	}

}
