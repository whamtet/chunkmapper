package com.chunkmapper.manager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
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
import com.chunkmapper.column.NoData;
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
import com.chunkmapper.column.WoolenCol;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.FarmType;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.reader.DensityReader;
import com.chunkmapper.reader.FarmTypeReader;
import com.chunkmapper.reader.FerryReader;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.reader.GlacierReader;
import com.chunkmapper.reader.GlobcoverReader;
import com.chunkmapper.reader.GlobcoverReaderImpl2;
import com.chunkmapper.reader.HasHouseReader;
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
import com.chunkmapper.sections.POI;
import com.chunkmapper.security.MySecurityManager;
import com.chunkmapper.writer.ArtifactWriter;
import com.chunkmapper.writer.GenericWriter;
import com.chunkmapper.writer.SchematicArtifactWriter;

public class GlobcoverManager {
	private final HeightsReader heightsReader;
	private final XapiRailReader railReader;
	private final POIReader poiReader;
	private final HasHouseReader hasHouseReader;
	private final XapiBoundaryReader boundaryReader;
	private final RugbyReader rugbyReader;
	private final XapiHighwayReader highwayReader;
	private final FerryReader ferryReader;
	private final PathReader pathReader;
	private final HutReader hutReader;
	private final DensityReader densityReader;
	public final boolean allWater;
	private final ArtifactWriter artifactWriter = new ArtifactWriter();
	public final int regionx, regionz;
	public final Random RANDOM = new Random();
	private final boolean gaiaMode;

	public final AbstractColumn[][] columns = new AbstractColumn[512][512];

	public static void main(String[] args) throws Exception {
		double[] latlon = Nominatim.getPoint("new plymouth, nz");
		//		double[] latlon = {-43.88, -176.15};
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		GlobcoverManager manager = new GlobcoverManager(regionx, regionz, 1, false);

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

	public GlobcoverManager(int regionx, int regionz, int verticalExaggeration, boolean gaiaMode) throws FileNotYetAvailableException, IOException, IllegalArgumentException, NoSuchElementException, InterruptedException, URISyntaxException, DataFormatException {
		this.regionx = regionx; this.regionz = regionz;
		this.gaiaMode = gaiaMode;
		heightsReader = new HeightsReaderS3(regionx, regionz, verticalExaggeration);

		Thread.sleep(0);
		OverpassObject o = OSMRouter.getObject(regionx, regionz);
		Collection<POI> pois = POIParser.getPois(o, regionx, regionz);

		Thread.sleep(0);
		ferryReader = gaiaMode ? null : new FerryReader(o, regionx, regionz);
		Thread.sleep(0);
		allWater = heightsReader.isAllWater() && ferryReader != null && !ferryReader.hasAFerry;

		if (allWater) {
			railReader = null;
			poiReader = null;
			hasHouseReader = null;
			boundaryReader = null;
			rugbyReader = null;
			highwayReader = null;
			pathReader = null;
			hutReader = null;
			densityReader = null;
			return;
		}

		densityReader = new DensityReader(pois, regionx, regionz);
		OrchardReader orchardReader = new OrchardReader(o, regionx, regionz);
		Thread.sleep(0);
		VineyardReader vineyardReader = new VineyardReader(o, regionx, regionz);
		Thread.sleep(0);
		hutReader = gaiaMode ? null : new HutReader(o, regionx, regionz);
		Thread.sleep(0);
		pathReader = gaiaMode ? null : new PathReader(o, regionx, regionz);
		Thread.sleep(0);
		highwayReader = gaiaMode ? null : new XapiHighwayReader(o, regionx, regionz, heightsReader);
		Thread.sleep(0);
		boundaryReader = gaiaMode ? null : new XapiBoundaryReader(o, regionx, regionz);
		Thread.sleep(0);
		rugbyReader = gaiaMode ? null : new RugbyReader(pois, regionx, regionz);
		Thread.sleep(0);
		hasHouseReader = gaiaMode ? null : new HasHouseReader(pois, regionx, regionz);
		Thread.sleep(0);
		GlobcoverReader coverReader = new GlobcoverReaderImpl2(regionx, regionz);
		Thread.sleep(0);
		GlacierReader glacierReader = new GlacierReader(o, regionx, regionz);
		Thread.sleep(0);

		LakeReader lakeReader = new XapiLakeReader(o, regionx, regionz);
		Thread.sleep(0);
		XapiRiverReader riverReader = new XapiRiverReader(o, regionx, regionz, heightsReader);
		Thread.sleep(0);
		railReader = gaiaMode ? null : new XapiRailReader(densityReader, o, regionx, regionz, heightsReader, verticalExaggeration);
		Thread.sleep(0);

		FarmTypeReader farmTypeReader = gaiaMode ? null : new FarmTypeReader();
		poiReader = gaiaMode ? null : new POIReader(o, regionx, regionz);
		Thread.sleep(0);

		//		XapiCoastlineReader coastlineReader = new XapiCoastlineReader(regionx, regionz, heightsReader);
		XapiCoastlineReader coastlineReader = new XapiCoastlineReader(o, regionx, regionz, coverReader);
		Thread.sleep(0);

		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int absx = j + regionx*512, absz = i + regionz*512;

				//				if (densityReader.isUrbanxz(absx, absz)) {
				//					columns[i][j] = new WoolenCol(absx, absz, heightsReader);
				//					continue;
				//				}

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
					columns[i][j] = gaiaMode ? new Grassland(absx, absz, heightsReader) : new Urban(absx, absz, heightsReader);
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
					columns[i][j] = new NoData(absx, absz, heightsReader);
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
		if (boundaryReader != null)
			boundaryReader.addBoundariesToChunk(chunkx, chunkz, chunk);

		//add some roads
		boolean chunkHasRoad = false;
		if (highwayReader != null && highwayReader.hasHighways)
			chunkHasRoad = highwayReader.addRoad(chunkx, chunkz, chunk);

		if (pathReader != null)
			pathReader.addPath(chunk, chunkx, chunkz);

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
		RugbyField rugbyField = rugbyReader == null ? null : rugbyReader.getRugbyField(chunk);
		if (rugbyField != null && !chunkHasRail && !chunkHasWater && !chunkHasRoad) {
			ArtifactWriter.addRugbyField(chunk, rugbyField);
		} else if ((chunkHasUrban || hasHouseReader != null && hasHouseReader.hasHouse(chunkx, chunkz))
				&& !chunkHasRail && !chunkHasWater && !chunkHasRoad) {
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
				int numFloors = (int) Math.floor(densityReader.getDensityxz(chunk.x0, chunk.z0) * 1000);
				if (numFloors < 1) {
					ArtifactWriter.addHouse(chunk);
				} else {
					SchematicArtifactWriter.addApartment(chunk, numFloors);
				}
				//				ArtifactWriter.addHouse(chunk);
			}

		} else if (chunkAllForest && !chunkHasRail && RANDOM.nextInt(100) == 0 && !gaiaMode) {
			if (RANDOM.nextInt(2) == 0) {
				ArtifactWriter.placeLookout(chunk);
			} else {
				ArtifactWriter.addTunnelIntoTheUnknown(chunk);
			}
		}

		//last but not least, add ferry
		if (!gaiaMode) {
			ferryReader.addLillies(chunk, chunkx, chunkz, columns);
			hutReader.addHut(chunk);
			if (RANDOM.nextInt(100000) == 0) {
				GenericWriter.addHeavenWaterFall(chunk);
			}
		}
		if (!MySecurityManager.offlineValid && abschunkz % 8 == 0)
			GenericWriter.addNorthGlassWall(chunk);

		return chunk;
	}

}