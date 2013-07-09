package com.chunkmapper.protoc.wrapper;

import java.util.HashSet;

import com.chunkmapper.protoc.CoastlineContainer.CoastlineSection;
import com.chunkmapper.protoc.LakeContainer.Lake;
import com.chunkmapper.protoc.RectangleContainer;
import com.chunkmapper.protoc.RailSectionContainer.RailSection;
import com.chunkmapper.protoc.RiverContainer.RiverSection;

public class Test {
	public static void main(String[] args) {
		RectangleContainer.Rectangle rec1 = RectangleContainer.Rectangle.newBuilder().setX(0).setZ(0).setHeight(10).setWidth(10).build();
		RectangleContainer.Rectangle rec2 = RectangleContainer.Rectangle.newBuilder().setX(0).setZ(0).setHeight(10).setWidth(10).build();
		
		HashSet<LakeWrapper> s1 = new HashSet<LakeWrapper>();
		Lake l1 = Lake.newBuilder().setBbox(rec1).build(), l2 = Lake.newBuilder().setBbox(rec2).build();
		LakeWrapper w1 = new LakeWrapper(l1), w2 = new LakeWrapper(l2);
		s1.add(w1);
		s1.add(w2);
		System.out.println(s1.size());
		
		RiverSection r1 = RiverSection.newBuilder().setBbox(rec1).build(), r2 = RiverSection.newBuilder().setBbox(rec2).build();
		RiverSectionWrapper rs1 = new RiverSectionWrapper(r1);
		RiverSectionWrapper rs2 = new RiverSectionWrapper(r2);
		HashSet<RiverSectionWrapper> s2 = new HashSet<RiverSectionWrapper>();
		s2.add(rs1);
		s2.add(rs2);
		System.out.println(s2.size());
		
		RailSection rail1 = RailSection.newBuilder().setBbox(rec1).build(), rail2 = RailSection.newBuilder().setBbox(rec2).build();
		RailSectionWrapper rails1 = new RailSectionWrapper(rail1);
		RailSectionWrapper rails2 = new RailSectionWrapper(rail2);
		HashSet<RailSectionWrapper> s3 = new HashSet<RailSectionWrapper>();
		s3.add(rails1);
		s3.add(rails2);
		System.out.println(s3.size());
		
		CoastlineSection section1 = CoastlineSection.newBuilder().setBbox(rec1).build(), section2 = CoastlineSection.newBuilder().setBbox(rec2).build();
		CoastlineSectionWrapper coastWrapper1 = new CoastlineSectionWrapper(section1);
		CoastlineSectionWrapper coastWrapper2 = new CoastlineSectionWrapper(section2);
		HashSet<CoastlineSectionWrapper> s4 = new HashSet<CoastlineSectionWrapper>();
		s4.add(coastWrapper1);
		s4.add(coastWrapper2);
		System.out.println(s4.size());
	}

}
