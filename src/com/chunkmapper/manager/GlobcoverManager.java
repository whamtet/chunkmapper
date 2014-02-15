package com.chunkmapper.manager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.zip.DataFormatException;

import com.chunkmapper.admin.OSMRouter;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.column.AbstractColumn;
import com.chunkmapper.column.Bare;
import com.chunkmapper.column.BroadleafEvergreen;
import com.chunkmapper.column.ClosedBroadleafDeciduous;
import com.chunkmapper.column.ClosedNeedleleafEvergreen;
import com.chunkmapper.column.Coast;
import com.chunkmapper.column.CroplandWithVegetation;
import com.chunkmapper.column.FloodedGrassland;
import com.chunkmapper.column.Foreshore;
import com.chunkmapper.column.ForestShrublandWithGrass;
import com.chunkmapper.column.FreshFloodedForest;
import com.chunkmapper.column.GrassWithForestShrubland;
import com.chunkmapper.column.Grassland;
import com.chunkmapper.column.IrrigatedCrops;
import com.chunkmapper.column.Lake;
import com.chunkmapper.column.MixedBroadNeedleleaf;
import com.chunkmapper.column.Ocean;
import com.chunkmapper.column.OpenBroadleafDeciduous;
import com.chunkmapper.column.OpenNeedleleaf;
import com.chunkmapper.column.Orchard;
import com.chunkmapper.column.RainfedCrops;
import com.chunkmapper.column.River;
import com.chunkmapper.column.SalineFloodedForest;
import com.chunkmapper.column.Shrubland;
import com.chunkmapper.column.Snow;
import com.chunkmapper.column.SparseVegetation;
import com.chunkmapper.column.Urban;
import com.chunkmapper.column.VegetationWithCropland;
import com.chunkmapper.column.Vineyard;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.FarmType;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.reader.DensityReader;
import com.chunkmapper.reader.FarmTypeReader;
import com.chunkmapper.reader.FerryReader;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.reader.GlacierReader;
import com.chunkmapper.reader.GlobcoverReader;
import com.chunkmapper.reader.GlobcoverReaderImpl2;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.HeightsReaderS3;
import com.chunkmapper.reader.HutReader;
import com.chunkmapper.reader.LakeReader;
import com.chunkmapper.reader.OrchardReader;
import com.chunkmapper.reader.POIReader;
import com.chunkmapper.reader.PathReader;
import com.chunkmapper.reader.RugbyReader;
import com.chunkmapper.reader.RugbyReader.RugbyField;
import com.chunkmapper.reader.VineyardReader;
import com.chunkmapper.reader.XapiBoundaryReader;
import com.chunkmapper.reader.XapiCoastlineReader;
import com.chunkmapper.reader.XapiHighwayReader;
import com.chunkmapper.reader.XapiLakeReader;
import com.chunkmapper.reader.XapiRailReader;
import com.chunkmapper.reader.XapiRiverReader;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;

public class GlobcoverManager {
	private final HeightsReader heightsReader;
	private final XapiRailReader railReader;
	private final POIReader poiReader;
	private final DensityReader densityReader;
	private final XapiBoundaryReader boundaryReader;
	private final RugbyReader rugbyReader;
	private final XapiHighwayReader highwayReader;
	private final FerryReader ferryReader;
	private final PathReader pathReader;
	private final HutReader hutReader;
	public final boolean allWater;
	private final ArtifactWriter artifactWriter = new ArtifactWriter();
	public final int regionx, regionz;
	public final Random RANDOM = new Random();

	public final AbstractColumn[][] columns = new AbstractColumn[512][512];

	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("new plymouth, nz");
		//		double[] latlon = {-43.88, -176.15};
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		GlobcoverManager manager = new GlobcoverManager(regionx, regionz, 1);

		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				if (manager.columns[i][j] instanceof RainfedCrops) {
					System.out.println("crops");
					System.exit(0);
				}
			}
		}
		System.out.println("no crops");
	}

	public GlobcoverManager(int regionx, int regionz, int verticalExaggeration) throws FileNotYetAvailableException, IOException, IllegalArgumentException, NoSuchElementException, InterruptedException, URISyntaxException, DataFormatException {
		this.regionx = regionx; this.regionz = regionz;

		heightsReader = new HeightsReaderS3(regionx, regionz, verticalExaggeration);
		ferryReader = new FerryReader(regionx, regionz);
		allWater = heightsReader.isAllWater() && !ferryReader.hasAFerry;

		if (allWater) {
			railReader = null;
			poiReader = null;
			densityReader = null;
			boundaryReader = null;
			rugbyReader = null;
			highwayReader = null;
			pathReader = null;
			hutReader = null;
			return;
		}
		OverpassObject o = OSMRouter.getObject(regionx, regionz);
		OrchardReader orchardReader = new OrchardReader(o, regionx, regionz);
		VineyardReader vineyardReader = new VineyardReader(o, regionx, regionz);
		hutReader = new HutReader(o, regionx, regionz);
		pathReader = new PathReader(o, regionx, regionz);
		highwayReader = new XapiHighwayReader(o, regionx, regionz, heightsReader);
		boundaryReader = new XapiBoundaryReader(o, regionx, regionz);
		rugbyReader = new RugbyReader(o, regionx, regionz);
		densityReader = new DensityReader(o, regionx, regionz);
		GlobcoverReader coverReader = new GlobcoverReaderImpl2(regionx, regionz);
		GlacierReader glacierReader = new GlacierReader(o, regionx, regionz);

		LakeReader lakeReader = new XapiLakeReader(o, regionx, regionz);
		XapiRiverReader riverReader = new XapiRiverReader(o, regionx, regionz, heightsReader);
		railReader = new XapiRailReader(o, regionx, regionz, heightsReader, verticalExaggeration);

		FarmTypeReader farmTypeReader = null;
		farmTypeReader = new FarmTypeReader();
		poiReader = new POIReader(o, regionx, regionz);

		//		XapiCoastlineReader coastlineReader = new XapiCoastlineReader(regionx, regionz, heightsReader);
		XapiCoastlineReader coastlineReader = new XapiCoastlineReader(o, regionx, regionz, coverReader);

		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int absx = j + regionx*512, absz = i + regionz*512;

				if (coastlineReader.hasWaterij(i, j)) {
					columns[i][j] = new Ocean(absx, absz);
					continue;
				}
				if (coastlineReader.isCoastij(i, j)) {
					columns[i][j] = new Coast(absx, absz);
					continue;
				}
				if (coastlineReader.isForeshoreij(i, j)) {
					columns[i][j] = new Foreshore(absx, absz);
					continue;
				}

				if (lakeReader.hasWaterij(i, j)) {
					columns[i][j] = new Lake(absx, absz, heightsReader);
					continue;
				}
				if (riverReader.hasWaterij(i, j)) {
					columns[i][j] = new River(absx, absz, heightsReader);
					continue;
				}
				if (vineyardReader.hasVineyard[i][j]) {
					columns[i][j] = new Vineyard(absx, absz, heightsReader);
					continue;
				}
				if (orchardReader.hasOrchard[i][j]) {
					columns[i][j] = new Orchard(absx, absz, heightsReader);
					continue;
				}
				if (glacierReader.hasGlacierij(i, j)) {
					columns[i][j] = new Snow(absx, absz, heightsReader);
					continue;
				}
				//				double absLat = absz > 0 ? absz / 3600. : -absz / 3600.;
				//				double snowLine = 4000 * (75 - absLat) / 75.;
				//				int realHeight = heightsReader.getRealHeightij(i, j);
				//				if (realHeight >= snowLine) {
				//					columns[i][j] = new Snow(absx, absz, heightsReader);
				//					continue;
				//				}
				//now for the rest
				Globcover coverType = coverReader.getGlobcover(i, j);
				switch (coverType) {
				case IrrigatedCrops:
					byte cropType = Block.Wheat.val;
					columns[i][j] = new IrrigatedCrops(absx, absz, cropType, heightsReader);
					break;
				case RainfedCrops:
					if (farmTypeReader == null) {
						columns[i][j] = new Grassland(absx, absz, heightsReader); 
					} else {
						FarmType farmType = farmTypeReader.getFarmType(i, j);
						columns[i][j] = new RainfedCrops(absx, absz, farmType, heightsReader);
					}
					break;
				case CroplandWithVegetation:
					cropType = Block.Wheat.val;
					columns[i][j] = new CroplandWithVegetation(absx, absz, cropType, heightsReader);
					break;
				case VegetationWithCropland:
					cropType = Block.Wheat.val;
					columns[i][j] = new VegetationWithCropland(absx, absz, cropType, heightsReader);
					break;
				case BroadleafEvergreen:
					columns[i][j] = new BroadleafEvergreen(absx, absz, heightsReader);
					break;
				case ClosedBroadleafDeciduous:
					columns[i][j] = new ClosedBroadleafDeciduous(absx, absz, heightsReader);
					break;
				case OpenBroadleafDeciduous:
					columns[i][j] = new OpenBroadleafDeciduous(absx, absz, heightsReader);
					break;
				case ClosedNeedleleafEvergreen:
					columns[i][j] = new ClosedNeedleleafEvergreen(absx, absz, heightsReader);
					break;
				case OpenNeedleleaf:
					columns[i][j] = new OpenNeedleleaf(absx, absz, heightsReader);
					break;
				case MixedBroadNeedleleaf:
					columns[i][j] = new MixedBroadNeedleleaf(absx, absz, heightsReader);
					break;
				case ForestShrublandWithGrass:
					columns[i][j] = new ForestShrublandWithGrass(absx, absz, heightsReader);
					break;
				case GrassWithForestShrubland:
					columns[i][j] = new GrassWithForestShrubland(absx, absz, heightsReader);
					break;
				case Shrubland:
					columns[i][j] = new Shrubland(absx, absz, heightsReader);
					break;
				case Grassland:
					columns[i][j] = new Grassland(absx, absz, heightsReader);
					break;
				case SparseVegetation:
					columns[i][j] = new SparseVegetation(absx, absz, heightsReader);
					break;
				case FreshFloodedForest:
					columns[i][j] = new FreshFloodedForest(absx, absz, heightsReader);
					break;
				case SalineFloodedForest:
					columns[i][j] = new SalineFloodedForest(absx, absz, heightsReader);
					break;
				case FloodedGrassland:
					columns[i][j] = new FloodedGrassland(absx, absz, heightsReader);
					break;
				case Urban:
					columns[i][j] = new Urban(absx, absz, heightsReader);
					break;
				case Bare:
					columns[i][j] = new Bare(absx, absz, heightsReader);
					break;
				case Water:
					columns[i][j] = new Grassland(absx, absz, heightsReader);
					break;
				case Snow:
					columns[i][j] = new Snow(absx, absz, heightsReader);
					break;
				case NoData:
					columns[i][j] = new Grassland(absx, absz, heightsReader);
					break;
				}
			}
		}

	}

	public Chunk getChunk(int abschunkx, int abschunkz, int relchunkx, int relchunkz) throws IOException {
		int chunkx = Matthewmatics.mod(abschunkx, 32);
		int chunkz = Matthewmatics.mod(abschunkz, 32);
		Chunk chunk = new Chunk(abschunkx, abschunkz, heightsReader.getHeights(chunkx, chunkz), relchunkx, relchunkz);

		//add bedrock
		GenericWriter.addBedrock(chunk);
		//draw basic columns
		boolean chunkHasUrban = false, chunkAllForest = true;
		boolean chunkHasWater = false;
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				AbstractColumn col = columns[i + chunkz*16][j + chunkx*16];
				col.addColumn(chunk);
				chunk.setBiome(j, i, col.biome);
				chunkHasUrban |= col.IS_URBAN;
				chunkAllForest &= col.IS_FOREST;
				chunkHasWater |= col.HAS_WATER;
			}
		}
		//now add trees
		int i1 = chunkz*16 - 20, i2 = chunkz*16 + 36;
		int j1 = chunkx*16 - 20, j2 = chunkx*16 + 36;
		if (i1 < 0) i1 = 0;
		if (j1 < 0) j1 = 0;
		if (i2 > 512) i2 = 512;
		if (j2 > 512) j2 = 512;
		for (int i = i1; i < i2; i++) {
			for (int j = j1; j < j2; j++) {
				AbstractColumn col = columns[i][j];
				col.addTree(chunk, heightsReader);
			}
		}

		//and signs for actual places
		if (poiReader != null)
			poiReader.addSigns(chunk);

		//a special sign
		POIReader.addSpecialLandmarks(chunk);

		//add country boundaries
		boundaryReader.addBoundariesToChunk(chunkx, chunkz, chunk);

		//add some roads
		boolean chunkHasRoad = false;
		if (highwayReader.hasHighways)
			chunkHasRoad = highwayReader.addRoad(chunkx, chunkz, chunk);

		//finally add rail
		boolean chunkHasRail = false;
		if (railReader != null && railReader.hasRails) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					int x = j + chunkx*16, z = i + chunkz*16;
					if (railReader.hasRailij(z, x)) {
						int h1 = railReader.getHeightij(z, x);
						AbstractColumn col = columns[i + chunkz*16][j + chunkx*16];
						chunkHasRail = true;
						byte railType = railReader.getRailTypeij(z, x);
						boolean usePlanks = col.HAS_WATER;
						artifactWriter.placeRail(j, i, chunk, h1, railType, usePlanks, false);
					}
				}
			}
		}



		//add a house
		RugbyField rugbyField = rugbyReader.getRugbyField(chunk);
		if (rugbyField != null && !chunkHasRail && !chunkHasWater && !chunkHasRoad) {
			ArtifactWriter.addRugbyField(chunk, rugbyField);
		} else if ((chunkHasUrban || densityReader.hasHouse(chunkx, chunkz)) && !chunkHasRail && !chunkHasWater
				&& !chunkHasRoad) {
			int i = RANDOM.nextInt(100);
			switch(i) {
			case 0:
				ArtifactWriter.placeLibrary(chunk);
				break;
			case 1:
				ArtifactWriter.placeMarket(chunk);
				break;
			case 2:
				ArtifactWriter.placePrison(chunk);
				break;
			default:
				ArtifactWriter.addHouse(chunk);
			}

		} else if (chunkAllForest && !chunkHasRail && RANDOM.nextInt(100) == 0) {
			if (RANDOM.nextInt(2) == 0) {
				ArtifactWriter.placeLookout(chunk);
			} else {
				ArtifactWriter.addTunnelIntoTheUnknown(chunk);
			}
		}
		pathReader.addPath(chunk, chunkx, chunkz);
		//last but not least, add ferry
		ferryReader.addLillies(chunk, chunkx, chunkz);
		hutReader.addHut(chunk);
		return chunk;
	}

}
