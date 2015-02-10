package com.chunkmapper.geology;
import java.util.HashMap;

import com.chunkmapper.enumeration.Blocka;
public class OreMap {
	public static final HashMap<String, byte[]> map = new HashMap<String, byte[]>();
	static {
		//Stone, Bedrock, Sand, Gravel, Gold Ore, Iron Ore, Coal Ore
		//Lapis Lazuli Ore, Sandstone, Diamond Ore, Redstone Ore
		//Clay Block, Emerald Ore, Stained Clay, Hardened Clay
		//Cobblestone, Obsidian, Gravel, Nether Quartz Ore
		//Glowstone, Netherrack, End Stone, Block of Coal
		map.put("Gold", new byte[] {Blocka.Gold_Ore}); // 70069
		map.put("Construction", new byte[] {Blocka.Gravel}); // 45454
		map.put("Sand and Gravel", new byte[] {Blocka.Sand, Blocka.Gravel}); // 45454
		map.put("Copper", new byte[] {Blocka.Iron_Ore}); // 25557
		map.put("Silver", new byte[] {Blocka.Iron_Block}); // 24405
		map.put("Stone", new byte[] {Blocka.Stone}); // 23132
		map.put("", new byte[] {Blocka.Stone}); // 21314
		map.put("Lead", new byte[] {Blocka.Iron_Ore}); // 21309
		map.put("Crushed/Broken", new byte[] {Blocka.Cobblestone}); // 17115
		map.put("Iron", new byte[] {Blocka.Iron_Ore}); // 14937
		map.put("Zinc", new byte[] {Blocka.Iron_Ore}); // 13054
		map.put("Uranium", new byte[] {Blocka.Iron_Ore}); // 10991
		map.put("Clay", new byte[] {Blocka.Clay}); // 6088
		map.put("Tungsten", new byte[] {Blocka.Iron_Block}); // 5519
		map.put("Manganese", new byte[] {Blocka.Iron_Block}); // 5245
		map.put("Chromium", new byte[] {Blocka.Iron_Block}); // 4518
		map.put("Limestone", new byte[] {Blocka.Block_Of_Quartz}); // 3997
		map.put("Mica", new byte[] {Blocka.Block_Of_Quartz}); // 3713
		map.put("General", new byte[] {Blocka.Iron_Block}); // 3278
		map.put("Barium-Barite", new byte[] {Blocka.Iron_Block}); // 3134
		map.put("Mercury", new byte[] {Blocka.Nether_Quartz_Ore}); // 3046
		map.put("Dimension", new byte[] {Blocka.Iron_Block}); // 2994
		map.put("Aluminum", new byte[] {Blocka.Iron_Block}); // 2606
		map.put("Phosphorus-Phosphates", new byte[] {Blocka.Netherrack}); // 2463
		map.put("Fluorine-Fluorite", new byte[] {Blocka.Emerald}); // 2441
		map.put("Fire Clay (Refractory)", new byte[] {Blocka.Clay}); // 2374
		map.put("Feldspar", new byte[] {Blocka.Block_Of_Quartz}); // 2167
		map.put("Molybdenum", new byte[] {Blocka.Iron_Block}); // 2031
		map.put("Geothermal", new byte[] {Blocka.Lava, Blocka.Water}); // 1730
		map.put("Nickel", new byte[] {Blocka.Iron_Ore}); // 1717
		map.put("Vanadium", new byte[] {Blocka.Iron_Block}); // 1672
		map.put("Gypsum-Anhydrite", new byte[] {Blocka.Block_Of_Quartz}); // 1663
		map.put("Tin", new byte[] {Blocka.Iron_Ore}); // 1592
		map.put("Antimony", new byte[] {Blocka.Iron_Ore}); // 1519
		map.put("Pumice", new byte[] {Blocka.Gravel}); // 1427
		map.put("Silica", new byte[] {Blocka.Block_Of_Quartz}); // 1418
		map.put("Talc-Soapstone", new byte[] {Blocka.Block_Of_Quartz, Blocka.Gravel}); // 1187
		map.put("Granite", new byte[] {Blocka.Stone}); // 1145
		map.put("Titanium", new byte[] {Blocka.Iron_Block}); // 1143
		map.put("Gemstone", new byte[] {Blocka.Emerald}); // 1128
		map.put("Platinum", new byte[] {Blocka.Iron_Block}); // 1002
		map.put("Beryllium", new byte[] {Blocka.Iron_Block}); // 970
		map.put("Bentonite", new byte[] {Blocka.Iron_Ore}); // 967
		map.put("Asbestos", new byte[] {Blocka.Nether_Quartz_Ore}); // 867
		map.put("Sulfur", new byte[] {Blocka.Sponge}); // 857
		map.put("Kaolin", new byte[] {Blocka.Block_Of_Quartz}); // 847
		map.put("Thorium", new byte[] {Blocka.Iron_Block}); // 745
		map.put("Diatomite", new byte[] {Blocka.Block_Of_Quartz}); // 685
		map.put("Graphite", new byte[] {Blocka.Coal_Block}); // 681
		map.put("Quartz", new byte[] {Blocka.Block_Of_Quartz}); // 631
		map.put("Magnesite", new byte[] {Blocka.Glass}); // 619
		map.put("REE", new byte[] {Blocka.Stone}); // 596
		map.put("Marble", new byte[] {Blocka.Block_Of_Quartz}); // 587
		map.put("Cobalt", new byte[] {Blocka.Iron_Ore}); // 571
		map.put("Calcium", new byte[] {Blocka.Block_Of_Quartz}); // 527
		map.put("Boron-Borates", new byte[] {Blocka.Iron_Block}); // 520
		map.put("Perlite", new byte[] {Blocka.Glass}); // 502
		map.put("Flagstone", new byte[] {Blocka.Cobblestone}); // 470
		map.put("Diamond", new byte[] {Blocka.Diamond_Ore}); // 458
		map.put("Volcanic Materials", new byte[] {Blocka.Obsidian}); // 451
		map.put("Metal", new byte[] {Blocka.Iron_Ore}); // 442
		map.put("Sand", new byte[] {Blocka.Sand}); // 414
		map.put("Salt", new byte[] {Blocka.Sand}); // 395
		map.put("Dolomite", new byte[] {Blocka.Glass}); // 367
		map.put("Niobium (Columbium)", new byte[] {Blocka.Iron_Block}); // 363
		map.put("Zirconium", new byte[] {Blocka.Iron_Block}); // 363
		map.put("Halite", new byte[] {Blocka.Glass}); // 341
		map.put("Lithium", new byte[] {Blocka.Iron_Block}); // 337
		map.put("Potassium", new byte[] {Blocka.Iron_Block}); // 323
		map.put("Kyanite", new byte[] {Blocka.Lapis_Lazuli_Ore}); // 314
		map.put("Vermiculite", new byte[] {Blocka.Gravel}); // 287
		map.put("Slate", new byte[] {Blocka.Cobblestone}); // 285
		map.put("Semiprecious Gemstone", new byte[] {Blocka.Emerald}); // 275
		map.put("Iridium", new byte[] {Blocka.Iron_Block}); // 268
		map.put("Tantalum", new byte[] {Blocka.Iron_Block}); // 266
		map.put("Osmium", new byte[] {Blocka.Iron_Block}); // 265
		map.put("Arsenic", new byte[] {Blocka.Iron_Block}); // 257
		map.put("PGE", new byte[] {Blocka.Iron_Ore}); // 236
		map.put("Bromine", new byte[] {Blocka.Soul_Sand}); // 229
		map.put("Zeolites", new byte[] {Blocka.Block_Of_Quartz}); // 229
		map.put("Strontium", new byte[] {Blocka.Iron_Block}); // 225
		map.put("Sulfur-Pyrite", new byte[] {Blocka.Sponge}); // 223
		map.put("Garnet", new byte[] {Blocka.Glass}); // 213
		map.put("Bismuth", new byte[] {Blocka.Iron_Block}); // 209
		map.put("Sodium", new byte[] {Blocka.Sponge}); // 187
		map.put("Palladium", new byte[] {Blocka.Iron_Block}); // 180
		map.put("Ball Clay", new byte[] {Blocka.Clay}); // 158
		map.put("Contained or Metal", new byte[] {Blocka.Iron_Block}); // 146
		map.put("Brick Clay", new byte[] {Blocka.Hardened_Clay}); // 145
		map.put("Nitrogen-Nitrates", new byte[] {Blocka.Sand}); // 107
		map.put("Peat", new byte[] {Blocka.Coal_Ore}); // 88
		map.put("Corundum", new byte[] {Blocka.Stained_Glass}); // 88
		map.put("Travertine", new byte[] {Blocka.Block_Of_Quartz}); // 85
		map.put("Tellurium", new byte[] {Blocka.Iron_Block}); // 83
		map.put("Selenium", new byte[] {Blocka.Netherrack}); // 74
		map.put("Coal", new byte[] {Blocka.Coal_Ore}); // 68
		map.put("Wollastonite", new byte[] {Blocka.Block_Of_Quartz}); // 67
		map.put("Ferrochrome", new byte[] {Blocka.Iron_Ore}); // 67
		map.put("Pigment", new byte[] {Blocka.Stained_Clay}); // 65
		map.put("Refiner", new byte[] {Blocka.Coal_Ore}); // 64
		map.put("Mineral Pigments", new byte[] {Blocka.Stained_Clay}); // 62
		map.put("High Calcium", new byte[] {Blocka.Block_Of_Quartz}); // 60
		map.put("Fullers Earth", new byte[] {Blocka.Soul_Sand}); // 58
		map.put("Emery", new byte[] {Blocka.Obsidian}); // 45
		map.put("Rhodium", new byte[] {Blocka.Iron_Block}); // 42
		map.put("Light Weight", new byte[] {Blocka.Iron_Block}); // 36
		map.put("Aggregate", new byte[] {Blocka.Gravel}); // 36
		map.put("Bloating Material", new byte[] {Blocka.Gravel}); // 33
		map.put("Helium", new byte[] {Blocka.Coal_Ore}); // 31
		map.put("Iodine", new byte[] {Blocka.Block_Of_Quartz}); // 30
		map.put("Sodium Sulfate", new byte[] {Blocka.Gravel}); // 29
		map.put("Cement Rock", new byte[] {Blocka.Gravel}); // 29
		map.put("Sodium Carbonate", new byte[] {}); // 29
		map.put("Sulfuric Acid", new byte[] {Blocka.Glow_Stone}); // 29
		map.put("Bituminous", new byte[] {Blocka.Coal_Ore}); // 27
		map.put("Abrasive", new byte[] {Blocka.Sand}); // 25
		map.put("Cadmium", new byte[] {Blocka.Iron_Block}); // 25
		map.put("Titanium-Heavy Minerals", new byte[] {Blocka.Iron_Block}); // 24
		map.put("Ruthenium", new byte[] {Blocka.Iron_Block}); // 24
		map.put("Smelter", new byte[] {Blocka.Iron_Ore}); // 23
		map.put("Ultra Pure", new byte[] {Blocka.Iron_Ore}); // 22
		map.put("Montmorillonite", new byte[] {Blocka.Block_Of_Quartz}); // 22
		map.put("Radium", new byte[] {Blocka.Glow_Stone}); // 21
		map.put("Rhenium", new byte[] {Blocka.Iron_Block}); // 19
		map.put("Sapphire", new byte[] {Blocka.Emerald}); // 19
		map.put("Iron-Pyrite", new byte[] {Blocka.Iron_Block}); // 19
		map.put("Olivine", new byte[] {Blocka.Stained_Glass}); // 18
		map.put("Pig Iron", new byte[] {Blocka.Iron_Ore}); // 17
		map.put("Natural Gas", new byte[] {Blocka.Coal_Ore}); // 16
		map.put("Lignite", new byte[] {Blocka.Coal_Ore}); // 15
		map.put("Cesium", new byte[] {Blocka.Iron_Block}); // 15
		map.put("Ruby", new byte[] {Blocka.Diamond_Ore}); // 14
		map.put("Oil Sands", new byte[] {Blocka.Coal_Ore}); // 13
		map.put("Gallium", new byte[] {Blocka.Iron_Block}); // 13
		map.put("Rock Asphalt", new byte[] {Blocka.Gravel}); // 13
		map.put("Sulfides", new byte[] {Blocka.Sponge}); // 12
		map.put("Ferromanganese", new byte[] {Blocka.Iron_Ore}); // 12
		map.put("Petroleum (Oil)", new byte[] {Blocka.Iron_Ore}); // 11
		map.put("Germanium", new byte[] {Blocka.Iron_Ore}); // 9
		map.put("Staurolite", new byte[] {Blocka.Stone}); // 8
		map.put("Water", new byte[] {Blocka.Water}); // 8
		map.put("Free", new byte[] {Blocka.Stone}); // 8
		map.put("Titanium-Ilmenite", new byte[] {Blocka.Iron_Ore}); // 7
		map.put("Ferrosilicon", new byte[] {Blocka.Iron_Ore}); // 7
		map.put("Copper Sulfide", new byte[] {Blocka.Sponge}); // 7
		map.put("Rubidium", new byte[] {Blocka.Iron_Block}); // 6
		map.put("Scandium", new byte[] {Blocka.Iron_Block}); // 6
		map.put("Magnesium Compounds", new byte[] {Blocka.Iron_Block}); // 5
		map.put("Oil Shale", new byte[] {Blocka.Coal_Ore}); // 5
		map.put("Pyrophyllite", new byte[] {Blocka.Sponge}); // 5
		map.put("Sandstone", new byte[] {Blocka.Sandstone}); // 4
		map.put("Chlorine", new byte[] {Blocka.Block_Of_Quartz}); // 4
		map.put("Refinery", new byte[] {Blocka.Coal_Ore}); // 4
		map.put("Carbon Dioxide", new byte[] {Blocka.Coal_Ore}); // 4
		map.put("Thallium", new byte[] {Blocka.Iron_Block}); // 4
		map.put("Emerald", new byte[] {Blocka.Emerald}); // 4
		map.put("Jade", new byte[] {Blocka.Emerald}); // 3
		map.put("Chlorite", new byte[] {Blocka.Sandstone}); // 3
		map.put("Mill Concentrate", new byte[] {Blocka.Sand}); // 3
		map.put("Pyrite", new byte[] {Blocka.Iron_Block}); // 3
		map.put("High Alumina Clay", new byte[] {Blocka.Hardened_Clay}); // 3
		map.put("Hafnium", new byte[] {Blocka.Iron_Block}); // 3
		map.put("Iron Oxide Pigments", new byte[] {Blocka.Iron_Ore}); // 2
		map.put("Soda Ash", new byte[] {Blocka.Gravel}); // 2
		map.put("Hectorite", new byte[] {Blocka.Sand}); // 2
		map.put("Ash", new byte[] {Blocka.Gravel}); // 2
		map.put("Titanium-Rutile", new byte[] {Blocka.Iron_Block}); // 2
		map.put("Nickel Laterite", new byte[] {Blocka.Iron_Block}); // 1
		map.put("Alabaster", new byte[] {Blocka.Block_Of_Quartz}); // 1
		map.put("Gilsonite", new byte[] {Blocka.Gravel}); // 1
		map.put("Flint", new byte[] {Blocka.Gravel}); // 1
		map.put("Anthracite", new byte[] {Blocka.Iron_Block}); // 1
		map.put("Tantalum from Tin Slag", new byte[] {Blocka.Iron_Block}); // 1
		map.put("Subbituminous", new byte[] {Blocka.Coal_Ore}); // 1
		map.put("Indium", new byte[] {Blocka.Iron_Ore}); // 1
		map.put("Tripoli", new byte[] {Blocka.Gravel}); // 1
		map.put("commod1", new byte[] {Blocka.Iron_Ore}); // 1
		map.put("Tailings", new byte[] {Blocka.Gravel}); // 1
		map.put("Cerium", new byte[] {Blocka.Iron_Ore}); // 1

	}
}