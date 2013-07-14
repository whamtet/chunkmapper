package com.chunkmapper.enumeration;

import java.io.File;
import java.util.Random;

public enum LenteTree {
	
	Alaska_Cedar, Alder, American_Basswood, American_Beech, Aspen, Astro_Fraxinifolium, Astro_Juari, Astro_Leicontei, 
	Atlantic_White_Cedar, Awarra, Babassu_Palm, Baldcypress, Balsam_Fir, Blackthorn, Brazil_Nut, Californian_Red_Fir, 
	Cashapona, Cinchona_Ledgeriana, Common_Ash, Common_Hawthorn, Douglas_Fir, European_Beech, European_Black_Pine, European_Holly, 
	European_Hornbeam, European_Larch, Fiberpalm, Flowering_Dogwood, Fraser_Fir, Grand_Fir, 
	Green_Ash, Hancornia_Speciosa, Hazel, Leopo_Piassaba, Linden, Loblolly_Pine, Lodgepole_Pine, Maripa_Palm, 
	Maritime_Pine, Myrciaria, Norway_Spruce, Paper_Birch, Parica, Peach_Palm, Platypodium_Elegans, Quaking_Aspen, 
	Red_Adler, Red_Maple, Rubbertree, Scots_Pine, Sesille_Oak, Silver_Birch, Smooth_Leaved_Elm, Soncoya, 
	Sugar_Maple, Sweetgum, Theobroma_Cacao, White_Oak, White_Willow, Yagrumo_Macho;
	
	private static final Random RANDOM = new Random();
	
	public final int rootDepth;
	private LenteTree(int rootDepth) {
		this.rootDepth = rootDepth;
	}
	private LenteTree() {
		rootDepth = 0;
	}
	
	private static void createEnum() {
		File f = new File("/Users/matthewmolloy/workspace/chunkmapper2/resources/trees");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (File child : f.listFiles()) {
			if (child.getName().endsWith(".schematic")) {
				String name = child.getName().split(".schematic")[0].replace(" ", "_");
				name = name.replace("-", "_");
				name += ", ";
				i++;
				if (i % 8 == 0) {
					name += "\n";
				}
				sb.append(name);
			}
		}
		System.out.println(sb.toString());
	}
	public static LenteTree randomTree(LenteTree[] list) {
		return list[RANDOM.nextInt(list.length)];
	}
	
	public static final LenteTree[] BoadleafEvergreen = {Astro_Fraxinifolium, Astro_Juari, Astro_Leicontei, Awarra, Babassu_Palm, 
		Brazil_Nut, Cashapona, Cinchona_Ledgeriana, Hancornia_Speciosa,
		Leopo_Piassaba, Maripa_Palm, Myrciaria, Parica, Peach_Palm, Platypodium_Elegans, 
		Rubbertree, Soncoya, Theobroma_Cacao, Yagrumo_Macho},
		//limit against shrubs for now
		ClosedBroadleafDeciduous = {Alder, American_Basswood, American_Beech, Aspen, Blackthorn,
		Common_Ash, Common_Hawthorn, European_Beech, European_Holly, European_Hornbeam, Flowering_Dogwood,
		Green_Ash, Hazel, Linden, Paper_Birch, Quaking_Aspen, Red_Adler, Red_Maple, Rubbertree, Sesille_Oak,
		Silver_Birch, Smooth_Leaved_Elm, Sugar_Maple, Sweetgum, White_Oak, White_Willow},
		
		ClosedNeedleleafEvergreen = {Alaska_Cedar, Atlantic_White_Cedar, Balsam_Fir, Californian_Red_Fir,
		Douglas_Fir, European_Black_Pine, Fraser_Fir, Grand_Fir, Loblolly_Pine, Lodgepole_Pine, Maritime_Pine,
		Norway_Spruce, Scots_Pine},
		CroplandWithVegetation = {Blackthorn, Common_Hawthorn, European_Holly, Rubbertree},
		//make it half trees, half shrubs
		ForestShrublandWithGrass = {Blackthorn, Common_Hawthorn, European_Holly, Rubbertree,
		European_Hornbeam, Linden, Fraser_Fir, Grand_Fir},
		FreshFloodedForest = {Baldcypress},
		GrassWithForestShrubland = ForestShrublandWithGrass,
		MixedBroadNeedleleaf = {Alder, American_Basswood, American_Beech, Aspen, Blackthorn,
		Common_Ash, Common_Hawthorn, European_Beech, European_Holly, European_Hornbeam, Flowering_Dogwood,
		Green_Ash, Hazel, Linden, Paper_Birch, Quaking_Aspen, Red_Adler, Red_Maple, Rubbertree, Sesille_Oak,
		Silver_Birch, Smooth_Leaved_Elm, Sugar_Maple, Sweetgum, White_Oak, White_Willow,
		Alaska_Cedar, Atlantic_White_Cedar, Balsam_Fir, Californian_Red_Fir,
		Douglas_Fir, European_Black_Pine, Fraser_Fir, Grand_Fir, Loblolly_Pine, Lodgepole_Pine, Maritime_Pine,
		Norway_Spruce, Scots_Pine},
		OpenBroadleafDeciduous = ClosedBroadleafDeciduous,
		OpenNeedleleaf = {Alaska_Cedar, Atlantic_White_Cedar, Balsam_Fir, Californian_Red_Fir,
				Douglas_Fir, European_Black_Pine, Fraser_Fir, Grand_Fir, Loblolly_Pine, Lodgepole_Pine, Maritime_Pine,
				Norway_Spruce, Scots_Pine, Baldcypress, European_Larch},
//		Shrubland = {Blackthorn, Common_Hawthorn, European_Holly},
		Shrubland = {Fraser_Fir, European_Holly, Blackthorn, Leopo_Piassaba, Hazel, Theobroma_Cacao,
		Soncoya},
		VegetationWithCropLand = CroplandWithVegetation;
}
