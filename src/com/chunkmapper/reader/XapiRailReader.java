package com.chunkmapper.reader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.downloader.UberDownloader;
import com.chunkmapper.enumeration.CircleRail;
import com.chunkmapper.enumeration.StraightRail;
import com.chunkmapper.parser.RailParser;
import com.chunkmapper.rail.HeightsManager;
import com.chunkmapper.rail.RailSection;
import com.chunkmapper.resourceinfo.XapiRailResourceInfo;

public class XapiRailReader {
	//	private int chunkx, chunkz;
	public short[][] heights = new short[512][512];
	private byte[][] railType = new byte[512][512];
	//	public boolean[][] special = new boolean[512][512];
	public final boolean hasRails;
	private final int x0, z0;
	private final static int MIN_SPACING = 20; //stops players getting dizzy
	private static final int MIN_BUMP_SIZE = 3, MIN_RUT_SIZE = 5;
	/**
	 * @param args
	 */
	private static void smoothHeights(short[] heights) {

		for (int i = 1; i < heights.length; i++) {
			int a = heights[i-1], b = heights[i];
			if (a < b) {
				//look for a small rut
				for (int j = i - 2; j >= 0 && j >= i - MIN_RUT_SIZE; j--) {
					if (heights[j] == b && heights[j+1] == a) {
						//the hollow is too small and we have to remove it
						for (int k = j + 1; k < i; k++) {
							heights[k]++;
						}
						break;
					}
				}
			}
			if (a > b) {
				//look for small mound
				for (int j = i - 2; j >= 0 && j >= i - MIN_BUMP_SIZE; j--) {
					if (heights[j] == b && heights[j+1] == a) {
						//mound is too small
						for (int k = j + 1; k < i; k++) {
							heights[k]--;
						}
						break;
					}
				}

			}
		}
	}
	private boolean inside(int x, int z) {
		x -= x0; z -= z0;
		return x >= 0 && z >= 0 && x < 512 && z < 512;
	}

	private void setBoth(int x, int z, short h, byte railType) {
		x -= x0; z -= z0;
		if (x < 0 || z < 0 || x > 511 || z > 511)
			return;
		heights[z][x] = h;
		this.railType[z][x] = railType;
	}

	public XapiRailReader(int regionx, int regionz, HeightsReader heightsReader, UberDownloader uberDownloader) throws IllegalArgumentException, NoSuchElementException, IOException, InterruptedException, FileNotYetAvailableException {
		x0 = regionx * 512; z0 = regionz * 512;
		XapiRailResourceInfo info = new XapiRailResourceInfo(regionx, regionz);
		if (!FileValidator.checkValid(info.file)) {
			throw new FileNotYetAvailableException();
		}
		ArrayList<RailSection> allSections = RailParser.getRailSections(info.file); 

		hasRails = allSections.size() > 0;
		if (!hasRails) {
			return;
		}
		HeightsManager heightsManager = new HeightsManager(uberDownloader);
//		if (true)
//			return;

		for (RailSection railSection : allSections) {
			if (railSection.points.size() < 2)
				continue;

			Point previousPoint = railSection.points.get(0);
			Point lastPoint = railSection.points.get(railSection.points.size() - 1);
			boolean hasInside = inside(previousPoint.x, previousPoint.y) || inside(lastPoint.x, lastPoint.y);
			ArrayList<Point> goodPoints = new ArrayList<Point>(railSection.points.size());
			goodPoints.add(previousPoint);
			for (Point p : railSection.points) {
				if (p.distance(previousPoint) >= MIN_SPACING) {
					hasInside |= inside(p.x, p.y);
					goodPoints.add(p);
					previousPoint = p;
				}
			}
			if (!hasInside)
				continue;//because its all outside

			goodPoints.add(lastPoint);

			Point startPoint = goodPoints.get(0);
			short h = heightsManager.getHeight(startPoint.x, startPoint.y);
			//			this.setSpecial(startPoint.x, startPoint.z);

			for (int i = 0; i < goodPoints.size() - 1; i++) {
				startPoint = goodPoints.get(i);
				//				this.setSpecial(startPoint.x, startPoint.z);
				Point endPoint = goodPoints.get(i+1);
				if (startPoint.equals(endPoint))
					continue;

				final int xStep = startPoint.x < endPoint.x ? 1 :
					startPoint.x == endPoint.x ? 0 : -1;
				final int zStep = startPoint.y < endPoint.y ? 1 :
					startPoint.y == endPoint.y ? 0 : -1;

				byte curveA = 0, curveB = 0;
				if (xStep == 1 && zStep == 1) {
					curveA = CircleRail.Three.val; curveB = CircleRail.One.val;
					//					curveA = CircleRail.One.val; curveB = CircleRail.One.val;
				}
				if (xStep == 1 && zStep == -1) {
					curveA = CircleRail.Two.val; curveB = CircleRail.Four.val;
					//					curveA = CircleRail.Two.val; curveB = CircleRail.Two.val;
				}
				if (xStep == -1 && zStep == -1) {
					curveA = CircleRail.One.val; curveB = CircleRail.Three.val;
					//					curveA = CircleRail.Three.val; curveB = CircleRail.Three.val;
				}
				if (xStep == -1 && zStep == 1) {
					curveA = CircleRail.Four.val; curveB = CircleRail.Two.val;
					//					curveA = CircleRail.Four.val; curveB = CircleRail.Four.val;
				}

				//now we do the diagonal sections
				int x = startPoint.x, z = startPoint.z;

				//we need to check adjacent regions
				//				if (heightsManager.hasRail(x - xStep, z)) {
				//					h = heightsManager.getHeight(x - xStep, z);
				//				}
				//				if (heightsManager.hasRail(x, z - zStep)) {
				//					h = heightsManager.getHeight(x, z - zStep);
				//				}
				//				if (heightsManager.hasRail(x, z)) {
				//					h = heightsManager.getHeight(x, z);
				//				}

				byte firstBlock = heightsManager.hasRail(x, z - zStep) ? curveA : StraightRail.East.val;
				heightsManager.setBoth(x, z, h, firstBlock);
				this.setBoth(x, z, h, firstBlock);
				heightsManager.setBoth(x + xStep, z, h, curveB);
				this.setBoth(x + xStep, z, h, curveB);

				for (x += xStep, z += zStep; x != endPoint.x && z != endPoint.z; x += xStep, z += zStep) {
					heightsManager.setBoth(x, z, h, curveA);
					this.setBoth(x, z, h, curveA);
					heightsManager.setBoth(x + xStep, z, h, curveB);
					this.setBoth(x + xStep, z, h, curveB);
				}

				if (x != endPoint.x){
					//we're going across;
					boolean straightAtStart = zStep == 0;
					firstBlock = straightAtStart ? StraightRail.East.val : curveA;

					heightsManager.setBoth(x, z, h, firstBlock);
					this.setBoth(x, z, h, firstBlock);

					//now we try setting heights
					int straightLength = endPoint.x > x ? endPoint.x - x : x - endPoint.x;
					short[] heights = new short[straightLength + 1];
					heights[0] = h;
					for (int j = 1; j < straightLength + 1; j++) {
						short targetHeight = heightsManager.getHeight(x + j * xStep, z);
						//need to check boundary conditions
						boolean notRiseUpOnTurn = j > 1 || straightAtStart;
						//						boolean noRutAtStart = j + x - startPoint.x >= MIN_RUT_SIZE;
						//						boolean noBumpAtEnd = endPoint.x - x - j >= MIN_BUMP_SIZE;
						//						if (notRiseUpOnTurn && noRutAtStart && noBumpAtEnd && targetHeight > h)
						if (notRiseUpOnTurn && targetHeight > h && railSection.allowAscend)
							h++;
						//						boolean noBumpAtStart = j + x - startPoint.x >= MIN_BUMP_SIZE;
						//						boolean noRutAtEnd = endPoint.x - x - j >= MIN_RUT_SIZE;
						//						if (noBumpAtStart && noRutAtEnd && targetHeight < h)
						if (targetHeight < h && railSection.allowDescend)
							h--;
						heights[j] = h;
					}
					smoothHeights(heights);
					for (int j = 1; j < straightLength + 1; j++) {
						byte railType = 0;
						if (j < straightLength && heights[j+1] > heights[j]) {
							railType = xStep == 1 ? StraightRail.EastUp.val : StraightRail.WestUp.val;
						} else if (heights[j-1] > heights[j]) {
							railType = xStep == 1 ? StraightRail.EastDown.val : StraightRail.WestDown.val;
						} else {
							railType = StraightRail.East.val;
						}
						heightsManager.setBoth(x + j * xStep, z, heights[j], railType);
						this.setBoth(x + j * xStep, z, heights[j], railType);
					}
				} else if (z != endPoint.z) {
					//in this case, things will always be straight at the beginning
					int straightLength = endPoint.z > z ? endPoint.z - z : z - endPoint.z;
					short[] heights = new short[straightLength];
					for (int j = 0; j < straightLength; j++) {
						short targetHeight = heightsManager.getHeight(x, z + j * zStep);
						//						boolean noRutAtStart = j + z - startPoint.z >= MIN_RUT_SIZE;
						//						boolean noBumpAtEnd = endPoint.z - z - j >= MIN_BUMP_SIZE;
						//						if (noRutAtStart && noBumpAtEnd && targetHeight > h)
						if (j > 0 && targetHeight > h && railSection.allowAscend)
							h++;
						//						boolean noBumpAtStart = j + z - startPoint.z >= MIN_BUMP_SIZE;
						//						boolean noRutAtEnd = endPoint.z - z - j >= MIN_RUT_SIZE;
						//						if (noBumpAtStart && noRutAtEnd && targetHeight < h)
						if (j > 0 && targetHeight < h && railSection.allowDescend)
							h--;
						heights[j] = h;
					}
					smoothHeights(heights);
					for (int j = 0; j < straightLength; j++) {
						byte railType = 0;
						if (j < straightLength - 1 && heights[j+1] > heights[j]) {
							railType = zStep == 1 ? StraightRail.SouthUp.val : StraightRail.NorthUp.val;
						} else if (j > 0 && heights[j-1] > heights[j]) {
							railType = zStep == 1 ? StraightRail.SouthDown.val : StraightRail.NorthDown.val;
						} else {
							railType = StraightRail.North.val;
						}
						heightsManager.setBoth(x, z + j * zStep, heights[j], railType);
						this.setBoth(x, z + j*zStep, heights[j], railType);
					}
				} else {

				}

			}

		}

	}

	public short getHeight(int x, int z) {
		//		return heights[z + chunkz*16][x + chunkx*16];
		return heights[com.chunkmapper.math.Matthewmatics.mod(z, 512)][com.chunkmapper.math.Matthewmatics.mod(x, 512)];
	}
	public byte getRailType(int x, int z) {
		//		return railType[z + chunkz*16][x + chunkx*16];
		return railType[com.chunkmapper.math.Matthewmatics.mod(z, 512)][com.chunkmapper.math.Matthewmatics.mod(x, 512)];
	}
	//	public boolean getSpecial(int x, int z) {
	//		return special[com.geominecraft.math.Math.mod(z, 512)][com.geominecraft.math.Math.mod(x, 512)];
	//	}


}
