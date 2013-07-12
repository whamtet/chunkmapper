package com.chunkmapper.enumeration;

import java.io.File;

public enum LenteTrees {
	Alaska_Cedar, Alder, American_Basswood, American_Beech, Aspen, Astro_Fraxinifolium, Astro_Juari, Astro_Leicontei, 
	Atlantic_White_Cedar, Awarra, Babassu_Palm, Baldcypress, Balsam_Fir, Blackthorn, Brazil_Nut, Californian_Red_Fir, 
	Cashapona, Cinchona_Ledgeriana, Common_Ash, Common_Hawthorn, Douglas_Fir, European_Beech, European_Black_Pine, European_Holly, 
	European_Hornbeam, European_Larch, f_73, f_74, Fiberpalm, Flowering_Dogwood, Fraser_Fir, Grand_Fir, 
	Green_Ash, Hancornia_Speciosa, Hazel, Leopo_Piassaba, Linden, Loblolly_Pine, Lodgepole_Pine, Maripa_palm, 
	Maritime_Pine, Myrciaria, Norway_Spruce, Paper_Birch, Parica, Peach_palm, Platypodium_Elegans, Quaking_Aspen, 
	Red_Adler, Red_Maple, Rubbertree, Scots_Pine, Sesille_Oak, Silver_Birch, Smooth_Leaved_Elm, Soncoya, 
	Sugar_Maple, Sweetgum, Theobroma_Cacao, White_Oak, White_Willow, Yagrumo_Macho;
	public static void main(String[] args) throws Exception {
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
}
