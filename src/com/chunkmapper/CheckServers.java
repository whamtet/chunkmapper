package com.chunkmapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.chunkmapper.resourceinfo.GlobcoverResourceInfo;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;
import com.chunkmapper.resourceinfo.LakeResourceInfo;
import com.chunkmapper.resourceinfo.NoaaGshhsResourceInfo;
import com.chunkmapper.resourceinfo.XapiRailResourceInfo;
import com.chunkmapper.resourceinfo.XapiResourceInfo;
import com.chunkmapper.resourceinfo.XapiRiverResourceInfo;

public class CheckServers {

//	private GlobcoverDownloader globcoverDownloader = new GlobcoverDownloader();
//	private LakeDownloader lakeDownloader = new LakeDownloader();
//	private NoaaGshhsDownloader noaaGshhsDownloader = new NoaaGshhsDownloader();
////	private RiverDownloader riverDownloader = new RiverDownloader();
//	private XapiRiverDownloader riverDownloader = new XapiRiverDownloader();
//	private XapiDownloader xapiDownloader = new XapiDownloader();
//	public HeightsDownloader heightsDownloader = new HeightsDownloader();
//	private XapiRailDownloader xapiRailDownloader = new XapiRailDownloader();
	
	public static void main(String[] args) {
		GlobcoverResourceInfo info = new GlobcoverResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info.url.toString());
			download(info.url);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LakeResourceInfo info2 = new LakeResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info2.url.toString());
			download(info2.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		NoaaGshhsResourceInfo info3 = new NoaaGshhsResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info3.url.toString());
			download(info3.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		XapiRiverResourceInfo info4 = new XapiRiverResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info4.url.toString());
			download(info4.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		XapiResourceInfo info5 = new XapiResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info5.url.toString());
			download(info5.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		HeightsResourceInfo info6 = new HeightsResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info6.url.toString());
			download(info6.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		XapiRailResourceInfo info7 = new XapiRailResourceInfo(0, 0);
		try {
			System.out.println("downloading " + info7.url.toString());
			download(info7.url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void download(URL url) throws IOException {
		InputStream is = url.openConnection().getInputStream();

	    BufferedReader reader = new BufferedReader( new InputStreamReader( is )  );

	    String line = null;
	    while( ( line = reader.readLine() ) != null )  {
	    }
	    reader.close();
	}
}

