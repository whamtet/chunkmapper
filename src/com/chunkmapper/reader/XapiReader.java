package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;

import com.chunkmapper.FileValidator;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.parser.Parser;
import com.chunkmapper.parser.RailParser;
import com.chunkmapper.resourceinfo.XapiResourceInfo;
import com.chunkmapper.writer.ArtifactWriter;

public class XapiReader extends Parser {
	private ArrayList<Place> data = new ArrayList<Place>();

	public XapiReader(int regionx, int regionz) throws IOException, FileNotYetAvailableException {
		XapiResourceInfo info = new XapiResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file)) {
			throw new FileNotYetAvailableException();
		}
		ArrayList<String> lines = Parser.getLines(info.file);
		String placeName = null, latStr = null, lonStr = null;
		for (String line : lines) {
			String tag = RailParser.getTag(line);
			if (tag == null)
				continue;
			if (tag.equals("node")) {
				latStr = Parser.getValue(line, "lat");
				lonStr = Parser.getValue(line, "lon");
			}
			if (tag.equals("tag") && Parser.getValue(line, "k").equals("name")) {
				placeName = Parser.getValue(line, "v");
			}
			if (tag.equals("/node")) {
				if (latStr != null && lonStr != null && placeName != null) {
					data.add(new Place(latStr, lonStr, placeName));
				}
				latStr = null;
				lonStr = null;
				placeName = null;
			}
			
		}

	}
	public void addSigns(Chunk chunk) {
		//adds all the signs to a chunk
		if (data == null)
			return;
		for (Place place : data) {
			int offsetx = place.absx - chunk.x0, offsetz = place.absz - chunk.z0;
			if (0 <= offsetx && offsetx < 16 && 0 <= offsetz && offsetz < 16) {
				int h = chunk.getHeights(offsetx, offsetz);
				if (h < 0) h = 4;
				ArtifactWriter.addSign(chunk, h, offsetz, offsetx, place.name.split(" "));
			}
		}

	}
	private static class Place {
		public final int absx, absz;
		public final String name;
		public Place(String latStr, String lonStr, String placeName) {
			double lat = Double.parseDouble(latStr);
			double lon = Double.parseDouble(lonStr);
			absx = (int) (3600 * lon);
			absz = (int) (-3600 * lat);
			
			name = placeName;
		}
		public String toString() {
			return name + " at " + absx + ", " + absz;
		}

	}

	/**
	 * @param args
	 */
}
