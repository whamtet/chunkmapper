package com.chunkmapper.quickgen;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

import com.chunkmapper.Point;
import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.GlobalSettings;
import com.chunkmapper.admin.MyLogger;
import com.chunkmapper.chunk.Chunk;
import com.chunkmapper.manager.GlobcoverManager;
import com.chunkmapper.nbt.NbtIo;
import com.chunkmapper.nbt.RegionFile;
import com.chunkmapper.parser.Nominatim;
import com.chunkmapper.reader.FileNotYetAvailableException;
import com.chunkmapper.security.MySecurityManager;

public class QuickGen {

	public static void main(String[] args) throws MalformedURLException, URISyntaxException, IOException, IllegalArgumentException, NoSuchElementException, FileNotYetAvailableException, InterruptedException, DataFormatException {
		double[] latlon = Nominatim.getPoint("Hollywood");
		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
//		gen(regionx, regionz);
		System.out.println((new Point(regionx, regionz)).toString());
		System.out.println("done");
	}


	public static File gen(int regionx, int regionz) throws IOException, IllegalArgumentException, NoSuchElementException, FileNotYetAvailableException, InterruptedException, URISyntaxException, DataFormatException {
		BucketInfo.initMap();
		//MyLogger.LOGGER.info(String.format("Quick generating at %s, %s", regionx, regionz));
		MySecurityManager.offlineValid = true;
		
		File t = new File("temp");
		t.mkdir();
		File tempDir = File.createTempFile("temp", "", t);
		tempDir.delete();
		tempDir.mkdirs();
		
		(new File(tempDir, "level.dat")).createNewFile();
		File regionDir = new File(tempDir, "region");
		regionDir.mkdir();
		
		File mca = new File(regionDir, "r.0.0.mca");
		RegionFile regionFile = new RegionFile(mca);

		int a = 0, b = 0;
		Point p = new Point(regionx, regionz);
		//MyLogger.LOGGER.info("Writing point at " + p.toString());
		
		GlobcoverManager coverManager = new GlobcoverManager(regionx, regionz);

		for (int chunkx = 0; chunkx < 32; chunkx++) {
			for (int chunkz = 0; chunkz < 32; chunkz++) {
				int abschunkx = chunkx + regionx * 32, abschunkz = chunkz + regionz * 32;
				int relchunkx = chunkx + a*32, relchunkz = chunkz + b * 32;
				Chunk chunk = coverManager.allWater ? new Chunk(abschunkx, abschunkz, relchunkx, relchunkz) : coverManager.getChunk(abschunkx, abschunkz, chunkx + a*32, chunkz + b*32);
				DataOutputStream stream = regionFile.getChunkDataOutputStream(chunkx, chunkz);
				NbtIo.write(chunk.getTag(), stream);
				stream.close();
			}
		}
		regionFile.close();
		
		return tempDir;
                
	}
	


}
