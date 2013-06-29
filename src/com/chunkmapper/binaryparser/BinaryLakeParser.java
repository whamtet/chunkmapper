package com.chunkmapper.binaryparser;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.zip.DataFormatException;

import com.chunkmapper.parser.RiverParser.RiverSection;
import com.chunkmapper.protoc.FileContainer.FileInfo;
import com.chunkmapper.protoc.admin.OfflineFileListManager;
import com.chunkmapper.sections.Lake;

public class BinaryLakeParser {

	public static ArrayList<Lake> getRiverSections(int regionx, int regionz) throws IOException, URISyntaxException, DataFormatException {
		Rectangle myRectangle = new Rectangle(regionx * 512, regionz * 512, 512, 512);
		BinaryLakeCache cache = new BinaryLakeCache(true);

		ArrayList<Lake> out = new ArrayList<Lake>();
		
		for (FileInfo info : OfflineFileListManager.lakeFileList.getFilesList()) {
			String[] split = info.getFile().split("_");

			int x = Integer.parseInt(split[1]);
			int z = Integer.parseInt(split[2]);
			int width = Integer.parseInt(split[3]);
			int height = Integer.parseInt(split[4]);

			Rectangle fileRectangle = new Rectangle(x, z, width, height);
			if (fileRectangle.intersects(myRectangle)) {
				for (Lake section : cache.getSections(info)) {
					if (section.bbox.intersects(myRectangle)) {
						out.add(section);
					}
				}
			}
		}
		cache.shutdown();
		return out;
	}


}
