package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.zip.DataFormatException;

import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.admin.OfflineFileListManager;
import com.chunkmapper.sections.Coastline;

public class BinaryCoastlineParser {

	public static HashSet<Coastline> getCoastlines(int regionx, int regionz) throws IOException, URISyntaxException, DataFormatException {
		Rectangle myRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);
		BinaryCoastlineCache cache = new BinaryCoastlineCache(true);

		HashSet<Coastline> out = new HashSet<Coastline>();
		
		for (FileInfo info : OfflineFileListManager.coastlineFileList.getFilesList()) {
			String[] split = info.getFile().split("_");

			int x = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			int width = Integer.parseInt(split[3]);
			int height = Integer.parseInt(split[4]);

			Rectangle fileRectangle = new Rectangle(x, z, width, height);
			if (fileRectangle.intersects(myRectangle)) {
				for (Coastline section : cache.getSections(info)) {
					if (section.bbox.intersects(myRectangle)) {
						out.add(section);
					}
				}
			}
		}
		return out;
	}

//	public static HashSet<Lake> getOfflineLakes(int regionx, int regionz) throws IOException {
//		Rectangle currentRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);
//
//		HashSet<Lake> out = new HashSet<Lake>();
//		File parent = new File("/Users/matthewmolloy/Downloads/osmosis-master/output/mylakes");
//		for (File f : parent.listFiles()) {
//			if (f.getName().startsWith("f_")) {
//				String[] split = f.getName().split("_");
//				int x = Integer.parseInt(split[1]);
//				int z = Integer.parseInt(split[2]);
//				int width = Integer.parseInt(split[3]);
//				int height = Integer.parseInt(split[4]);
//
//				Rectangle fileRectangle = new Rectangle(x, z, width, height);
//				if (fileRectangle.intersects(currentRectangle)) {
//					DataInputStream in = new DataInputStream(new FileInputStream(f));
//					byte[] data = new byte[(int) f.length()];
//					in.readFully(data);
//					in.close();
//					
//					LakeRegion lakeRegion = LakeRegion.parseFrom(data);
//					for (LakeContainer.Lake rawLake : lakeRegion.getLakesList()) {
//						RectangleContainer.Rectangle sectionBbox = rawLake.getBbox();
//						Rectangle sectionBbox2 = new Rectangle(sectionBbox.getX(), sectionBbox.getZ(), sectionBbox.getWidth(), sectionBbox.getHeight());
//
//						if (sectionBbox2.intersects(currentRectangle)) {
//
//							ArrayList<Point> points = new ArrayList<Point>();
//							Point rootPoint = null;
//							for (PointContainer.Point point : rawLake.getPointsList()) {
//								if (rootPoint == null) {
//									rootPoint = new Point(point.getX(), point.getZ());
//									points.add(rootPoint);
//								} else {
//									points.add(new Point(point.getX() + rootPoint.x, point.getZ() + rootPoint.z));
//								}
//							}
//							//public Lake(ArrayList<Point> points, Rectangle bbox, boolean isInner, boolean isCove, boolean isLagoon) {
//							out.add(new Lake(points, sectionBbox2, rawLake.getIsCove(), rawLake.getIsLagoon()));
//						}
//					}
//				}
//
//			}
//		}
//
//		return out;
//	}

}
