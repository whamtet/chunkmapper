package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParsingException;
import nu.xom.ValidityException;

import com.chunkmapper.FileValidator;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.resourceinfo.XapiResourceInfo;
import com.chunkmapper.writer.ArtifactWriter;

public class XapiReader {
	private ArrayList<Place> data;

	public XapiReader(int regionx, int regionz) throws ValidityException, ParsingException, IOException, FileNotYetAvailableException {
		XapiResourceInfo info = new XapiResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file)) {
			throw new FileNotYetAvailableException();
			//			System.out.println("xapi file not found");
			//			return;
		}
			Builder parser = new Builder();
			Document doc = parser.build(info.file);
			Node osm = doc.getChild(0);
			int numChildren = osm.getChildCount();
			data = new ArrayList<Place>(numChildren);

			for (int i = 0; i < numChildren; i++) {
				Node child = osm.getChild(i);
				if (child instanceof Element) {
					Element el = (Element) child;
					if (el.getLocalName().equals("node")) {
						data.add(new Place(el));
					}
				}
			}
		//		for (Place place : data) {
		//			System.out.println(place);
		//		}


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
				ArtifactWriter.addSign(chunk, h, chunk.zr + offsetz, chunk.xr + offsetx, new String[] {place.name});
			}
		}

	}
	private static class Place {
		public final int absx, absz;
		public final String name;
		public Place(Element el) {
			double lat = Double.parseDouble(el.getAttributeValue("lat"));
			double lon = Double.parseDouble(el.getAttributeValue("lon"));
			absx = (int) (3600 * lon);
			absz = (int) (-3600 * lat);

			String name2 = "";
			for (int i = 0; i < el.getChildCount(); i++) {
				Node child = el.getChild(i);
				if (child instanceof Element) {
					Element el2 = (Element) child;
					if (el2.getAttributeValue("k").equals("name")) {
						name2 = el2.getAttributeValue("v");
						break;
					}
				}
			}
			name = name2;
		}
		public String toString() {
			return name + " at " + absx + ", " + absz;
		}

	}

	/**
	 * @param args
	 */
	//	public static void main(String[] args) throws Exception {
	//		double[] latlon = geocode.core.placeToCoords("takanini, auckland");
	//		double lat = latlon[0], lon = latlon[1];
	//		int regionx = (int) (lon * 3600 / 512);
	//		int regionz = -(int) (lat * 3600 / 512);
	//		long l1 = System.currentTimeMillis();
	//		XapiReader xapiReader = new XapiReader(regionx, regionz);
	//		long l2 = System.currentTimeMillis();
	//		System.out.println(l2 - l1);
	//
	//	}

}
