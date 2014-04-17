package com.chunkmapper.reader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.column.AbstractColumn;
import com.chunkmapper.enumeration.CircleRail;
import com.chunkmapper.enumeration.StraightRail;
import com.chunkmapper.parser.OverpassObject;
import com.chunkmapper.parser.RailParser;
import com.chunkmapper.rail.HeightsManager;
import com.chunkmapper.sections.RailSection;

public class XapiRailReader {
	public short[][] heights = new short[512][512];
	private byte[][] railType = new byte[512][512];
	public final boolean hasRails;
	private final int x0, z0;
	private AbstractColumn[][] cols;
	private final static int MIN_SPACING = 20; //stops players getting dizzy
	private static final int MIN_BUMP_SIZE = 3, MIN_RUT_SIZE = 5;
	/**
	 * @param args
	 */
	private static void smoothHeights(int[] heights2) {

		for (int i = 1; i < heights2.length; i++) {
			int a = heights2[i-1], b = heights2[i];
			if (a < b) {
				//look for a small rut
				for (int j = i - 2; j >= 0 && j >= i - MIN_RUT_SIZE; j--) {
					if (heights2[j] == b && heights2[j+1] == a) {
						//the hollow is too small and we have to remove it
						for (int k = j + 1; k < i; k++) {
							heights2[k]++;
						}
						break;
					}
				}
			}
			if (a > b) {
				//look for small mound
				for (int j = i - 2; j >= 0 && j >= i - MIN_BUMP_SIZE; j--) {
					if (heights2[j] == b && heights2[j+1] == a) {
						//mound is too small
						for (int k = j + 1; k < i; k++) {
							heights2[k]--;
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

	private void setBoth(int x, int z, int h, byte railType) {
		x -= x0; z -= z0;
		if (x < 0 || z < 0 || x > 511 || z > 511)
			return;
		heights[z][x] =  (short) h;
		this.railType[z][x] = railType;
	}


	public XapiRailReader(DensityReader densityReader, OverpassObject o, 
			int regionx, int regionz, HeightsReader heightsReader, int verticalExaggeration,
			AbstractColumn[][] cols
			) throws IllegalArgumentException, NoSuchElementException, IOException, InterruptedException, FileNotYetAvailableException, URISyntaxException, DataFormatException {
		x0 = regionx * 512; z0 = regionz * 512;
		this.cols = cols;
		Collection<RailSection> allSections = RailParser.getRailSection(densityReader, o, regionx, regionz); 

		hasRails = allSections.size() > 0;
		if (!hasRails) {
			return;
		}

		HeightsManager heightsManager = new HeightsManager(verticalExaggeration, regionx, regionz, cols);
		//		if (true)
		//			return;

		for (RailSection railSection : allSections) {
			boolean allowAscend = !railSection.hasTunnel && !railSection.hasCutting;
			boolean allowDescend = !railSection.hasBridge && !railSection.hasEmbankment;

			if (railSection.points.size() < 2)
				continue;

			Point previousPoint = railSection.points.get(0);
			Point lastPoint = railSection.points.get(railSection.points.size() - 1);

			if (previousPoint.equals(lastPoint))
				continue;

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
				continue;//because its all outside 512x512

			goodPoints.add(lastPoint);


			Point startPoint = goodPoints.get(0);
			int h = heightsManager.getHeight(startPoint.x, startPoint.y);

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
				byte firstBlock = heightsManager.hasRail(x, z - zStep) ? curveA : StraightRail.East.val;

				if (xStep != 0 && zStep != 0) {

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
				}

				if (x != endPoint.x) {
					//we're going across;
					boolean straightAtStart = zStep == 0;
					firstBlock = straightAtStart ? StraightRail.East.val : curveA;

					//special case for first block
					heightsManager.setBoth(x, z, h, firstBlock);
					this.setBoth(x, z, h, firstBlock);

					//now we try setting heights
					int straightLength = endPoint.x > x ? endPoint.x - x : x - endPoint.x;
					int[] heights = new int[straightLength + 1];

					heights[0] = h;
					for (int j = 1; j < straightLength + 1; j++) {
						int targetHeight = heightsManager.getHeight(x + j * xStep, z);
						//need to check boundary conditions
						boolean notRiseUpOnTurn = j > 1 || straightAtStart;

						if (notRiseUpOnTurn && targetHeight > h && allowAscend)
							h++;

						if (targetHeight < h && allowDescend)
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
					//no need for special case
					int straightLength = endPoint.z > z ? endPoint.z - z : z - endPoint.z;
					int[] heights = new int[straightLength];
					for (int j = 0; j < straightLength; j++) {
						int targetHeight = heightsManager.getHeight(x, z + j * zStep);
						if (j > 0 && targetHeight > h && allowAscend)
							h++;
						if (j > 0 && targetHeight < h && allowDescend)
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
						h = heights[j];
						heightsManager.setBoth(x, z + j * zStep, h, railType);
						this.setBoth(x, z + j*zStep, h, railType);
					}
				}

			}

		}
		//lets fix things up a little
		for (int i = 0; i < 512; i++) {
			for (int j = 0; j < 512; j++) {
				if (heightsManager.hasRailij(i, j)) {
					simpleCleanUp(i, j, heightsManager);
				}
			}
		}
		//save caches!!!
		heightsManager.save();

	}
	private void simpleCleanUp(int i, int j, HeightsManager hm) throws IOException {
		byte railType = this.railType[i][j];
		if (railType == StraightRail.North.val && j > 0 && j < 511
				&& hm.hasRailij(i, j-1) && hm.hasRailij(i, j+1)) {
			this.railType[i][j] = StraightRail.East.val;
			hm.setRailTypeij(i, j, StraightRail.East.val);
		}
		if (railType == StraightRail.East.val && i > 0 && i < 511
				&& hm.hasRail(i-1, j) && hm.hasRail(i+1, j)) {
			this.railType[i][j] = StraightRail.North.val;
			hm.setRailTypeij(i, j, StraightRail.North.val);
		}
	}
	private static enum CP {North, East, South, West}
	private static ArrayList<CP> emptyCP(int i, int j, byte railType, HeightsManager hm) throws IOException {
		ArrayList<CP> endPoints = new ArrayList<CP>(2);
		
		if (railType == StraightRail.North.val) {
			if (i > 0 && !hm.hasRailij(i-1, j))
				endPoints.add(CP.North);
			if (i < 511 && !hm.hasRailij(i+1, j))
				endPoints.add(CP.South);
		}
		if (railType == StraightRail.East.val) {
			if (j > 0 && !hm.hasRailij(i, j-1))
				endPoints.add(CP.West);
			if (j < 511 && !hm.hasRailij(i, j+1))
				endPoints.add(CP.East);
		}
		if (railType == CircleRail.One.val) {
			if (j > 0 && !hm.hasRailij(i, j-1))
				endPoints.add(CP.West);
			if (i < 511 && !hm.hasRailij(i+1, j))
				endPoints.add(CP.South);
		}
		if (railType == CircleRail.Two.val) {
			if (i < 511 && !hm.hasRailij(i+1, j))
				endPoints.add(CP.South);
			if (j < 511 && !hm.hasRailij(i, j+1))
				endPoints.add(CP.East);
		}
		if (railType == CircleRail.Three.val) {
			if (i > 0 && !hm.hasRailij(i-1, j))
				endPoints.add(CP.North);
			if (j < 511 && !hm.hasRailij(i, j+1))
				endPoints.add(CP.East);
		}
		if (railType == CircleRail.Four.val) {
			if (i > 0 && !hm.hasRailij(i-1, j))
				endPoints.add(CP.North);
			if (j > 0 && !hm.hasRailij(i, j-1))
				endPoints.add(CP.West);
		}
		return endPoints;
	}
//	private static ArrayList<Point> emptyEndpoints(int i, int j, byte railType, HeightsManager hm) throws IOException {
//		ArrayList<Point> endPoints = new ArrayList<Point>(2);
//		
//		if (railType == StraightRail.North.val) {
//			if (i > 0 && !hm.hasRailij(i-1, j))
//				endPoints.add(new Point(i-1, j));
//			if (i < 511 && !hm.hasRailij(i+1, j))
//				endPoints.add(new Point(i+1, j));
//		}
//		if (railType == StraightRail.East.val) {
//			if (j > 0 && !hm.hasRailij(i, j-1))
//				endPoints.add(new Point(i, j-1));
//			if (j < 511 && !hm.hasRailij(i, j+1))
//				endPoints.add(new Point(i, j+1));
//		}
//		if (railType == CircleRail.One.val) {
//			if (j > 0 && !hm.hasRailij(i, j-1))
//				endPoints.add(new Point(i, j-1));
//			if (i < 511 && !hm.hasRailij(i+1, j))
//				endPoints.add(new Point(i+1, j));
//		}
//		if (railType == CircleRail.Two.val) {
//			if (j < 511 && !hm.hasRailij(i, j+1))
//				endPoints.add(new Point(i, j+1));
//			if (i < 511 && !hm.hasRailij(i+1, j))
//				endPoints.add(new Point(i+1, j));
//		}
//		if (railType == CircleRail.Three.val) {
//			if (i > 0 && !hm.hasRailij(i-1, j))
//				endPoints.add(new Point(i-1, j));
//			if (j < 511 && !hm.hasRailij(i, j+1))
//				endPoints.add(new Point(i, j+1));
//		}
//		if (railType == CircleRail.Four.val) {
//			if (i > 0 && !hm.hasRailij(i-1, j))
//				endPoints.add(new Point(i-1, j));
//			if (j > 0 && !hm.hasRailij(i, j-1))
//				endPoints.add(new Point(i, j-1));
//		}
//		return endPoints;
//	}
	private static ArrayList<Point> fullEndpoints(int i, int j, byte railType, HeightsManager hm) throws IOException {
		ArrayList<Point> endPoints = new ArrayList<Point>(4);
		if (i > 0 && hm.hasRailij(i-1, j))
			endPoints.add(new Point(i-1, j));
		if (j > 0 && hm.hasRailij(i, j-1))
			endPoints.add(new Point(i, j-1));
		if (i < 511 && hm.hasRailij(i+1, j))
			endPoints.add(new Point(i+1, j));
		if (j < 511 && hm.hasRailij(i, j+1))
			endPoints.add(new Point(i, j+1));
		
		return endPoints;
	}
	private static ArrayList<CP> fullCP(int i, int j, byte railType, HeightsManager hm) throws IOException {
		ArrayList<CP> endPoints = new ArrayList<CP>(4);
		
		if (i > 0 && hm.hasRailij(i-1, j))
			endPoints.add(CP.North);
		if (j > 0 && hm.hasRailij(i, j-1))
			endPoints.add(CP.West);
		if (i < 511 && hm.hasRailij(i+1, j))
			endPoints.add(CP.South);
		if (j < 511 && hm.hasRailij(i, j+1))
			endPoints.add(CP.East);
		
		return endPoints;
	}
	
	//better cleanup
	private byte newRailType(int i, int j, HeightsManager hm) throws IOException {
		byte railType = this.railType[i][j];

		if (railType == StraightRail.EastUp.val || railType == StraightRail.EastDown.val || 
				railType == StraightRail.NorthUp.val || railType == StraightRail.NorthDown.val) return railType;
		
//		ArrayList<Point> emptyEndPoints = emptyEndpoints(i, j, railType, hm),
//				fullEndPoints = fullEndpoints(i, j, railType, hm);
		ArrayList<CP> emptyEndPoints = emptyCP(i, j, railType, hm),
				fullEndPoints = fullCP(i, j, railType, hm);
		
		if (railType == StraightRail.North.val) {
			if (emptyEndPoints.size() == 2)
				return StraightRail.East.val;
			if (emptyEndPoints.size() == 0)
				return railType;		
			switch(emptyEndPoints.get(0)) {
			case West:
				return CircleRail.Four.val;
			case East:
				return CircleRail.Three.val;
			default:
				return railType;
			}
		}
		if (railType == StraightRail.East.val) {
			if (emptyEndPoints.size() == 2)
				return StraightRail.North.val;
			if (emptyEndPoints.size() == 0)
				return railType;
			switch(emptyEndPoints.get(0)) {
			case North:
				return CircleRail.Three.val;
				
			}
		}
	}
		//		private void cleanUp(int i, int j, HeightsManager hm) throws IOException {
		//			byte railType = this.railType[i][j];
		//			
		//			if (railType == StraightRail.EastUp.val || railType == StraightRail.EastDown.val || 
		//					railType == StraightRail.NorthUp.val || railType == StraightRail.NorthDown.val) return;
		//		
		//		boolean northAvailable;
		//		if (i > 0) {
		//			byte northType = this.railType[i-1][j];
		//			northAvailable = hm.hasRailij(i-1, j) && (northType == StraightRail.North.val || northType == CircleRail.One.val 
		//					|| northType == CircleRail.Two.val || northType == StraightRail.NorthUp.val
		//					|| northType == StraightRail.NorthDown.val);
		//		} else {
		//			return;
		//		}
		//		boolean westAvailable;
		//		if (j > 0) {
		//			byte westType = this.railType[i][j-1];
		//			westAvailable = hm.hasRail(i, j-1) && (westType == StraightRail.East.val || westType == CircleRail.Two.val 
		//					|| westType == CircleRail.Three.val || westType == StraightRail.EastUp.val
		//					|| westType == StraightRail.EastDown.val);
		//		} else {
		//			return;
		//		}
		//		boolean eastAvailable = j < 511 && hm.hasRailij(i, j+1);
		//		boolean southAvailable = i < 511 && hm.hasRailij(i+1, j);
		//		
		//		
		//		
		//		if (northAvailable) {
		//			if (westAvailable) {
		//				railType = CircleRail.Four.val;
		//			} else if (southAvailable) {
		//				railType = StraightRail.North.val;
		//			} else if (eastAvailable) {
		//				railType = CircleRail.Three.val;
		//			}
		//		} else if (westAvailable) {
		//			if (eastAvailable) {
		//				railType = StraightRail.East.val;
		//			} else if (southAvailable) {
		//				railType = CircleRail.One.val;
		//			}
		//		} else if (southAvailable) {
		//			if (eastAvailable) {
		//				railType = CircleRail.Two.val;
		//			}
		//		}
		//		
		//		this.railType[i][j] = railType;
		//		hm.setRailTypeij(i, j, railType);
		//	}


		public boolean hasRailij(int i, int j) {
			return heights[i][j] != 0;
		}
		public short getHeightij(int i, int j) {
			return heights[i][j];
		}
		public byte getRailTypeij(int i, int j) {
			return railType[i][j];
		}

	}
