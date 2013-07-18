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
import com.chunkmapper.protoc.POIContainer.POI;

public class POIRegionBuilder implements RegionBuilder {
	public final ArrayList<POI> pois = new ArrayList<POI>();
	@Override
	public RegionBuilder newBuilder() {
		return new POIRegionBuilder();
	}

	@Override
	public List<SectionWrapper> getSections() {
		ArrayList<SectionWrapper> out = new ArrayList<SectionWrapper>();
		for (POI poi : pois) {
			out.add(new POIWrapper(poi));
		}
		return out;
	}

	@Override
	public void addSection(SectionWrapper section) {
		POIWrapper section2 = (POIWrapper) section;
		pois.add(section2.poi);
	}

	@Override
	public int getSectionCount() {
		return pois.size();
	}

	@Override
	public RegionBuilder newBuilder(InputStream in) throws IOException {
		DataInputStream in2 = new DataInputStream(in);
		POIRegionBuilder builder = new POIRegionBuilder();
		try {
			while(true) {
				int len = in2.readInt();
				byte[] data = new byte[len];
				in2.readFully(data);
				builder.addSection(new POIWrapper(POI.parseFrom(data)));
			}
		} catch (EOFException e) {}
		return builder;
	}

	@Override
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream out2 = new DataOutputStream(out);
		for (POI poi : pois) {
			byte[] data = poi.toByteArray();
			out2.writeInt(data.length);
			out2.write(data);
		}
		return out.toByteArray();
	}

}
