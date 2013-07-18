package com.chunkmapper.protoc.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.BorderContainer.BorderSection;
import com.chunkmapper.protoc.LakeContainer.Lake;

public class BorderRegionBuilder implements RegionBuilder {
	public final ArrayList<BorderSection> borderSections = new ArrayList<BorderSection>();
	@Override
	public RegionBuilder newBuilder() {
		return new BorderRegionBuilder();
	}

	@Override
	public List<SectionWrapper> getSections() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (BorderSection border : borderSections) {
			out.add(new BorderWrapper(border));
		}
		return out;
	}

	@Override
	public void addSection(SectionWrapper section) {
		BorderWrapper section2 = (BorderWrapper) section;
		borderSections.add(section2.borderSection);
	}

	@Override
	public int getSectionCount() {
		return borderSections.size();
	}

	@Override
	public RegionBuilder newBuilder(InputStream in) throws IOException {
		DataInputStream in2 = new DataInputStream(in);
		BorderRegionBuilder builder = new BorderRegionBuilder();
		try {
			while(true) {
				int len = in2.readInt();
				byte[] data = new byte[len];
				in2.readFully(data);
				builder.addSection(new BorderWrapper(BorderSection.parseFrom(data)));
			}
		} catch (EOFException e) {}
		return builder;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream out2 = new DataOutputStream(out);
		for (BorderSection border : borderSections) {
			byte[] data = border.toByteArray();
			out2.writeInt(data.length);
			out2.write(data);
		}
		return out.toByteArray();
	}

}
