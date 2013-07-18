package com.chunkmapper.protoc.wrapper;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.chunkmapper.protoc.LakeContainer.Lake;

public class LakeRegionBuilder implements RegionBuilder {
	public final ArrayList<Lake> lakes = new ArrayList<Lake>();
	@Override
	public RegionBuilder newBuilder() {
		return new LakeRegionBuilder();
	}

	@Override
	public List<SectionWrapper> getSections() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (Lake lake : lakes) {
			out.add(new LakeWrapper(lake));
		}
		return out;
	}

	@Override
	public void addSection(SectionWrapper section) {
		LakeWrapper section2 = (LakeWrapper) section;
		lakes.add(section2.lake);
	}

	@Override
	public int getSectionCount() {
		return lakes.size();
	}

	@Override
	public RegionBuilder newBuilder(InputStream in) throws IOException {
		DataInputStream in2 = new DataInputStream(in);
		LakeRegionBuilder builder = new LakeRegionBuilder();
		try {
			while(true) {
				int len = in2.readInt();
				byte[] data = new byte[len];
				in2.readFully(data);
				builder.addSection(new LakeWrapper(Lake.parseFrom(data)));
			}
		} catch (EOFException e) {}
		return builder;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream out2 = new DataOutputStream(out);
		for (Lake lake : lakes) {
			byte[] data = lake.toByteArray();
			out2.writeInt(data.length);
			out2.write(data);
		}
		return out.toByteArray();
	}

}
