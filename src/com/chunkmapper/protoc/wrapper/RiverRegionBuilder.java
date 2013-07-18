package com.chunkmapper.protoc.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.RiverContainer.RiverSection;

public class RiverRegionBuilder implements RegionBuilder {
	public final ArrayList<RiverSection> rivers = new ArrayList<RiverSection>();
	@Override
	public RegionBuilder newBuilder() {
		return new RiverRegionBuilder();
	}

	@Override
	public List<SectionWrapper> getSections() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (RiverSection section : rivers) {
			out.add(new RiverSectionWrapper(section));
		}
		return out;
	}

	@Override
	public void addSection(SectionWrapper section) {
		RiverSectionWrapper section2 = (RiverSectionWrapper) section;
		rivers.add(section2.riverSection);
	}

	@Override
	public int getSectionCount() {
		return rivers.size();
	}

	@Override
	public RegionBuilder newBuilder(InputStream in) throws IOException {
		DataInputStream in2 = new DataInputStream(in);
		RiverRegionBuilder builder = new RiverRegionBuilder();
		try {
			while(true) {
				int len = in2.readInt();
				byte[] data = new byte[len];
				in2.readFully(data);
				builder.addSection(new RiverSectionWrapper(RiverSection.parseFrom(data)));
			}
		} catch (EOFException e) {}
		return builder;
	}


	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream out2 = new DataOutputStream(out);
		for (RiverSection river : rivers) {
			byte[] data = river.toByteArray();
			out2.writeInt(data.length);
			out2.write(data);
		}
		return out.toByteArray();
	}

}
