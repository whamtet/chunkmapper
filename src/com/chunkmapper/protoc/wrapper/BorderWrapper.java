package com.chunkmapper.protoc.wrapper;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.chunkmapper.protoc.BorderContainer.BorderSection;
import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.PointContainer;
import com.chunkmapper.protoc.RectangleContainer;

public class BorderWrapper implements SectionWrapper {
	public final BorderSection borderSection;
	public final Rectangle bbox;
	public final ArrayList<Point> points = new ArrayList<Point>();
	
	public BorderWrapper(BorderSection section) {
		this.borderSection = section;
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
		RegionBuilder out = new BorderRegionBuilder();
		DataInputStream in2 = new DataInputStream(in);
		try {
			while (true) {
				int size = in2.readInt();
				byte[] data = new byte[size];
				in2.readFully(data);
				out.addSection(new BorderWrapper(BorderSection.parseFrom(data)));
			}
		} catch (EOFException e) {
		}
		return out;
	}

	@Override
	public byte[] toByteArray() {
		return borderSection.toByteArray();
	}
	
	@Override
	public int hashCode() {
		return bbox.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (!(other instanceof BorderWrapper))
			return false;
		BorderWrapper other2 = (BorderWrapper) other;
		
		if (other2.points.size() != points.size())
			return false;
		for (int i = 0; i < points.size(); i++) {
			if (!other2.points.get(i).equals(points.get(i)))
				return false;
		}
		return true;
	}
}
