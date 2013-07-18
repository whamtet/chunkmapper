package com.chunkmapper.protoc.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;

public class CoastlineRegionBuilder implements RegionBuilder {
	public final ArrayList<CoastlineSection> coastlines = new ArrayList<CoastlineSection>();
	
	@Override
	public RegionBuilder newBuilder() {
		return new CoastlineRegionBuilder();
	}

	@Override
	public List<SectionWrapper> getSections() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (CoastlineSection coastline : coastlines) {
			out.add(new CoastlineSectionWrapper(coastline));
		}
		return out;
	}

	@Override
	public void addSection(SectionWrapper section) {
		CoastlineSectionWrapper section2 = (CoastlineSectionWrapper) section;
		coastlines.add(section2.coastline);
	}

	@Override
	public int getSectionCount() {
		return coastlines.size();
	}

	@Override
	public RegionBuilder newBuilder(InputStream in) throws IOException {
		DataInputStream in2 = new DataInputStream(in);
		CoastlineRegionBuilder builder = new CoastlineRegionBuilder();
		try {
			while(true) {
				int len = in2.readInt();
				byte[] data = new byte[len];
				in2.readFully(data);
				builder.addSection(new CoastlineSectionWrapper(CoastlineSection.parseFrom(data)));
			}
		} catch (EOFException e) {}
		return builder;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream out2 = new DataOutputStream(out);
		for (CoastlineSection coastline : coastlines) {
			byte[] data = coastline.toByteArray();
			out2.writeInt(data.length);
			out2.write(data);
		}
		return out.toByteArray();
	}

}
