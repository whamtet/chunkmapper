package com.chunkmapper.process;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import com.chunkmapper.Point;
import com.chunkmapper.admin.Utila;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.FileContainer.FileList;
import com.chunkmapper.reader.GlobcoverReaderImpl2;



public class ProcessGlobcover {
	private static final int numThreads = 5;
	private final static ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
	private static final LinkedBlockingQueue<Point> queue = new LinkedBlockingQueue<Point>();
	private static final String s = "/Users/matthewmolloy/Downloads/Globcover2009_V2.3_Global_/GLOBCOVER_L4_200901_200912_V2.3.tif";
//	private static final HashSet<Point> set = new HashSet<Point>();
	
	
	public static void main(String[] args) throws Exception {
		copyOver();
	}
	private static void copyOver() throws IOException {
		File parent = new File("/Users/matthewmolloy/workspace/chunkmapper-static/public/mat/data");
		File srcParent = new File("globcover");
		File[] toCopy = srcParent.listFiles(new BinaryFilenameFilter());
		int numInDirectory = 990;
		FileList.Builder fileListBuilder = FileList.newBuilder();
		
		for (int i = 0; i < toCopy.length; i++) {
			File dir = new File(parent, "f_" + i / numInDirectory);
			File src = toCopy[i];
			fileListBuilder.addFiles(FileInfo.newBuilder().setFile(src.getName()).setParent(dir.getName() + "/").build());
			FileUtils.copyFile(src, new File(dir, src.getName()));
		}
		
		FileOutputStream out = new FileOutputStream(new File("/Users/matthewmolloy/workspace/chunkmapper-static/public/mat/master.pbf"));
		out.write(fileListBuilder.build().toByteArray());
		out.close();
	}
	
	//these use specialised java (JAI) files.  Not generally compilable
	
//	private static void splitImage() {
//		
//		PlanarImage im = JAI.create("fileload", s).createInstance();
//		int totalWidth = im.getWidth(), totalHeight = im.getHeight();
//		
//		int midx = 180 * 360, midz = 90 * 360;
//		int windowWidth = 512 * GlobcoverReaderImpl2.REGION_WIDTH / 10;
//		double windowWidthd = windowWidth;
//		
//		int x1 = (int) -Math.ceil(midx / windowWidthd), z1 = (int) -Math.ceil(midz / windowWidthd);
//		int x2 = (totalWidth - midx) / windowWidth, 
//				z2 = (totalHeight - midz) / windowWidth;
//		
//		File parent = new File("globcover");
//		if (parent.exists())
//			throw new RuntimeException("globcover exists!");
//		
//		for (int x = x1; x <= x2; x++) {
//			for (int z = z1; z <= z2; z++) {
//				queue.add(new Point(x, z));
//			}
//		}
//		for (int i = 0; i < numThreads; i++) {
//			executorService.execute(new Task());
//		}
//		executorService.shutdown();
//	}
//	private static class Task implements Runnable {
//		public void run() {
//			
//			
//			PlanarImage im = JAI.create("fileload", s).createInstance();
//			int totalWidth = im.getWidth(), totalHeight = im.getHeight();
//			
//			int midx = 180 * 360, midz = 90 * 360;
//			int windowWidth = 512 * GlobcoverReaderImpl2.REGION_WIDTH / 10;
//			File parent = new File("globcover");
//			parent.mkdir();
//			ColorModel colorModel = im.getColorModel();
//			while (true) {
//				Point p = queue.poll();
//				if (p == null) {
//					return;
//				}
//				int x1 = midx + windowWidth * p.x, z1 = midz + windowWidth * p.z;
//				int x2 = x1 + windowWidth, z2 = z1 + windowWidth;
//				
//				if (x1 < 0)
//					x1 = 0;
//				if (z1 < 0)
//					z1 = 0;
//				if (x2 > totalWidth)
//					x2 = totalWidth;
//				if (z2 > totalHeight)
//					z2 = totalHeight;
//				
//				Rectangle r = new Rectangle(x1, z1, x2 - x1, z2 - z1);
//				BufferedImage image = im.getAsBufferedImage(r, colorModel);
//				File outFile = new File(parent, "f_" + p.x + "_" + p.y + Utila.BINARY_SUFFIX);
//				System.out.println(outFile);
//				try {
//					ImageIO.write(image, "png", outFile);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
	
//		System.out.println(totalWidth * 10 / 50 / 512 * totalHeight * 10 / 50 / 512);
//		int x = totalWidth / 2, y = totalHeight * 2 / 5;
//		
//		BufferedImage image = im.getAsBufferedImage(new Rectangle(x, y, 1000, 1000), im.getColorModel());
//		byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
//		HashSet<Integer> set = new HashSet<Integer>();
//		for (int i : buffer) {
//			if (i < 0)
//				i += 256;
//			set.add(i);
//		}
//		System.out.println(set);

//	}

}
