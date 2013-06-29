package com.chunkmapper.writer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.zip.DataFormatException;

import com.chunkmapper.Utila;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.column.AbstractColumn;
import com.chunkmapper.column.Bare;
import com.chunkmapper.column.BroadleafEvergreen;
import com.chunkmapper.column.ClosedBroadleafDeciduous;
import com.chunkmapper.column.ClosedNeedleleafEvergreen;
import com.chunkmapper.column.Coast;
import com.chunkmapper.column.CroplandWithVegetation;
import com.chunkmapper.column.FloodedGrassland;
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
import com.chunkmapper.column.RainfedCrops;
import com.chunkmapper.column.River;
import com.chunkmapper.column.SalineFloodedForest;
import com.chunkmapper.column.Shrubland;
import com.chunkmapper.column.Snow;
import com.chunkmapper.column.SparseVegetation;
import com.chunkmapper.column.Urban;
import com.chunkmapper.column.VegetationWithCropland;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.enumeration.Block;
import com.chunkmapper.enumeration.FarmType;
import com.chunkmapper.enumeration.Globcover;
import com.chunkmapper.math.Matthewmatics;
import com.chunkmapper.math.TriangleZonator;
import com.chunkmapper.reader.FarmTypeReader;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.reader.GlobcoverReader;
import com.chunkmapper.reader.HeightsReader;
import com.chunkmapper.reader.LakeReader;
import com.chunkmapper.reader.NoaaGshhsReader;
import com.chunkmapper.reader.XapiRailReader;
import com.chunkmapper.reader.XapiReader;
import com.chunkmapper.reader.XapiRiverReader;

public class GlobcoverManager {
	private final HeightsReader heightsReader;
	//	private final FlightgearRailReader railReader;
	private final XapiRailReader railReader;
	private final XapiReader xapiReader;
	public final boolean allWater;
	private final ArtifactWriter artifactWriter = new ArtifactWriter();
	public final int regionx, regionz;
	public final Random RANDOM = new Random();

	private final AbstractColumn[][] columns = new AbstractColumn[512][512];

	public GlobcoverManager(int regionx, int regionz, UberDownloader uberDownloader, int verticalExaggeration) throws FileNotYetAvailableException, IOException, IllegalArgumentException, NoSuchElementException, InterruptedException, URISyntaxException, DataFormatException {
		this.regionx = regionx; this.regionz = regionz;

		heightsReader = new HeightsReader(regionx, regionz, uberDownloader, verticalExaggeration);
		allWater = heightsReader.allWater;
		if (allWater) {
			railReader = null;
			xapiReader = null;
			return;
		}
		GlobcoverReader coverReader = new GlobcoverReader(regionx, regionz);

		LakeReader lakeReader = new LakeReader(regionx, regionz, heightsReader);
		XapiRiverReader riverReader = new XapiRiverReader(regionx, regionz);
		railReader = new XapiRailReader(regionx, regionz, heightsReader, uberDownloader, verticalExaggeration);
		boolean includeLivestock = !railReader.hasRails;
		FarmTypeReader farmTypeReader = new FarmTypeReader(includeLivestock);
		xapiReader = new XapiReader(regionx, regionz);

		NoaaGshhsReader noaaGshhsReader = new NoaaGshhsReader(regionx, regionz);

		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				int absx = j + regionx*512, absz = i + regionz*512;

				switch(noaaGshhsReader.getVal(i, j)) {
				case NoaaGshhsReader.OCEAN:
					columns[i][j] = new Ocean(absx, absz);
					continue;
				case NoaaGshhsReader.COAST:
					columns[i][j] = new Coast(absx, absz);
					continue;
				}

				final int h = heightsReader.getHeightij(i, j);
				if (lakeReader.hasWaterij(i, j, h)) {
					columns[i][j] = new Lake(absx, absz, heightsReader);
					continue;
				}
				if (riverReader.hasWaterij(i, j)) {
					columns[i][j] = new River(absx, absz, heightsReader);
					continue;
				}
				double absLat = absz > 0 ? absz / 3600. : -absz / 3600.;
				double snowLine = 4000 * (75 - absLat) / 75.;
				int realHeight = heightsReader.getRealHeightij(i, j);
				if (realHeight >= snowLine) {
					columns[i][j] = new Snow(absx, absz, heightsReader);
					continue;
				}
				//now for the rest
				Globcover coverType = coverReader.getGlobcover(i, j);
				switch (coverType) {
				case IrrigatedCrops:
					byte cropType = Block.Wheat.val;
					columns[i][j] = new IrrigatedCrops(absx, absz, cropType, heightsReader);
					break;
				case RainfedCrops:
					FarmType farmType = farmTypeReader.getFarmType(i, j);
					columns[i][j] = new RainfedCrops(absx, absz, farmType, heightsReader);
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

	public Chunk getChunk(int abschunkx, int abschunkz, int relchunkx, int relchunkz) {
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
				chunkHasUrban |= col.IS_URBAN;
				chunkAllForest &= col.IS_FOREST;
				chunkHasWater |= col.HAS_WATER;
			}
		}
		//now add trees
		int i1 = chunkz*16 - Utila.CHUNK_START, i2 = chunkz*16 + Utila.CHUNK_END;
		int j1 = chunkx*16 - Utila.CHUNK_START, j2 = chunkx*16 + Utila.CHUNK_END;
		if (i1 < 0) i1 = 0;
		if (j1 < 0) j1 = 0;
		if (i2 > 512) i2 = 512;
		if (j2 > 512) j2 = 512;
		for (int i = i1; i < i2; i++) {
			for (int j = j1; j < j2; j++) {
				AbstractColumn col = columns[i][j];

				if (col.treeHeight != 0)
					col.addTree(chunk, heightsReader);
			}
		}

		//add a sign for terrain type
		//		int cornerx = relchunkx*16 + 15, cornerz = relchunkz*16 + 15;
		//		int h = heightsReader.getHeightij(chunkz*16 + 15, chunkx*16 + 15);
		//		if (h > 0) {
		//			String[] ss = columns[chunkz*16 + 15][chunkx*16 + 15].getClass().toString().split("\\.");
		//			ArtifactWriter.addSign(chunk, h, cornerz, cornerx, ss);
		//		}
		//and signs for actual places
		if (xapiReader != null)
			xapiReader.addSigns(chunk);

		//finally add rail
		boolean chunkHasRail = false;
		if (railReader != null && railReader.hasRails) {
			for (int i = 0; i < 16; i++) {
				for (int j = 0; j < 16; j++) {
					int x = j + chunkx*16, z = i + chunkz*16;
					short h1 = railReader.getHeight(x, z);
					if (h1 != 0) {
						AbstractColumn col = columns[i + chunkz*16][j + chunkx*16];
						chunkHasRail = true;
						byte railType = railReader.getRailType(x, z);
						boolean usePlanks = col.HAS_WATER;
						artifactWriter.placeRail(j, i, chunk, h1, railType, usePlanks, false);
					}
				}
			}
		}
		//add a house
		if (chunkHasUrban && !chunkHasRail && !chunkHasWater) {
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
			case 3:
				ArtifactWriter.addGallows(chunk);
				break;
			default:
				ArtifactWriter.addHouse(chunk);
			}

		}
		if (chunkAllForest && !chunkHasRail && RANDOM.nextInt(100) == 0) {
			if (RANDOM.nextInt(2) == 0) {
				ArtifactWriter.placeLookout(chunk);
			} else {
				ArtifactWriter.addTunnelIntoTheUnknown(chunk);
			}
		}
		return chunk;
	}

}
