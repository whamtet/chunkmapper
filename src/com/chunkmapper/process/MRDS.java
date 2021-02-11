package com.chunkmapper.process;

import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.chunkmapper.Zip;
import com.chunkmapper.protoc.MineContainer.MineInfo;
import com.chunkmapper.protoc.MineContainer.MineList;

import au.com.bytecode.opencsv.CSVReader;

public class MRDS {

	/**
	 * @param args
	 */
	//dep_id,mrds_id,mas_id,site_name,latitude,longitude,region,country,state,county,com_type,commod1,commod2,commod3,oper_type,dep_type,prod_size,dev_stat,ore,gangue,other_matl,orebody_fm,work_type,model,alteration,conc_proc,prev_name,ore_ctrl,reporter,hrock_unit,hrock_type,arock_unit,arock_type,structure,tectonic,ref,yfp_ba,yr_fst_prd,ylp_ba,yr_lst_prd,dy_ba,disc_yr,prod_yrs,discr
	private static double[] latlon(String[] split) {
		double lat = Double.parseDouble(split[4]);
		double lon = Double.parseDouble(split[5]);
		return new double[] {lat, lon};
	}
	private static final int STEP = 45;
	private static class DataStore {
		public final Point point;
//		public final CSVWriter writer;
		private final File f;
		private final MineList.Builder builder = MineList.newBuilder();

		public DataStore(int x, int y) throws IOException {
			point = new Point(x, y);
			f = new File(String.format("mines/f_%s_%s_.pbf", x, y));
//			writer = new CSVWriter(new FileWriter(f));
		}

		public void write(String[] split) {
//			String[] split2 = {split[4], split[5], split[11]};
//			writer.writeNext(split2);
			double lat = Double.parseDouble(split[4]), lon = Double.parseDouble(split[5]);
			int absx = (int) (lon * 3600), absz = (int) (-lat * 3600);
			MineInfo.Builder builder2 = MineInfo.newBuilder().setAbsx(absx).setAbsz(absz);
			for (String commodity : split[11].split(", ")) {
				builder2.addCommodities(commodity);
			}
			builder.addMines(builder2.build());
		}
		public void close() throws IOException {
			Zip.zipOver(builder.build().toByteArray(), f);
		}
	}
	private static HashMap<Point, DataStore> map = new HashMap<Point, DataStore>();

	private static void initReaders() throws IOException {
		for (int x = -180; x <= 180; x += STEP) {
			for (int y = -90; y <= 90; y += STEP) {
				DataStore ds = new DataStore(x, y);
				map.put(ds.point, ds);
			}
		}
	}
	private static void closeAll() throws IOException {
		for (DataStore ds : map.values()) {
			ds.close();
		}
	}
	private static int floor(double d) {
		return STEP * (int) (Math.floor(d/STEP));
	}
	private static Point point(String[] split) {
		double[] latlon = latlon(split);
		return new Point(floor(latlon[1]), floor(latlon[0]));
	}
	private static int getIndex(String[] split, String k) {
		for (int i = 0; i < split.length; i++) {
			if (split[i].equals(k)) {
				return i;
			}
		}
		return -1;
	}
	private static HashMap<String, Integer> commodCount = new HashMap<String, Integer>();
	public static void main(String[] args) throws Exception {

		CSVReader reader = new CSVReader(new FileReader("/Users/matthewmolloy/Downloads/mrds.csv"));
		initReaders();
//		HashSet<String> s = new HashSet<String>();
//		int i = getIndex(reader.readNext(), "prod_size");
		String [] nextLine;
		while ((nextLine = reader.readNext()) != null) {
			try {
				map.get(point(nextLine)).write(nextLine);
			} catch (NumberFormatException e) {
				
			}
//			s.add(nextLine[i]);
//			String k = nextLine[i];
//			Integer count = commodCount.get(k);
//			if (count == null)
//				count = 1;
//			else
//				count++;
//			commodCount.put(k, count);
		}
//		System.out.println(commodCount);
		closeAll();
		System.out.println("done");

	}

}
