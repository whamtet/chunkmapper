package com.chunkmapper.protoc.wrapper;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;

public class CoastlineSectionWrapper implements SectionWrapper {
	public final CoastlineSection coastline;
	public final Rectangle bbox;
	public final ArrayList<Point> points = new ArrayList<Point>();
	
	public CoastlineSectionWrapper(CoastlineSection section) {
		coastline = section;
		RectangleContainer.Rectangle r = section.getBbox();
		bbox = new Rectangle(r.getX(), r.getZ(), r.getWidth(), r.getHeight());
		for (PointContainer.Point rawPoint : section.getPointsList()) {
			points.add(new Point(rawPoint.getX(), rawPoint.getZ()));
		}
	}
	@Override
	public Rectangle getBbox() {
		return bbox;
	}

	@Override
	public RegionBuilder getRegion(InputStream in) throws IOException {
		RegionBuilder out = new CoastlineRegionBuilder();
		DataInputStream in2 = new DataInputStream(in);
		try {
			while (true) {
				int size = in2.readInt();
				byte[] data = new byte[size];
				in2.readFully(data);
				out.addSection(new CoastlineSectionWrapper(CoastlineSection.parseFrom(data)));
			}
		} catch (EOFException e) {
		}
		return out;
	}

	@Override
	public byte[] toByteArray() {
		return coastline.toByteArray();
	}
	
	@Override
	public int hashCode() {
		return bbox.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof CoastlineSectionWrapper))
			return false;
		CoastlineSectionWrapper other2 = (CoastlineSectionWrapper) other;
		
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}
//		
	

}
