package com.chunkmapper.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.InfoManager;
import com.chunkmapper.Point;
import com.chunkmapper.enumeration.OSMSource;
import com.chunkmapper.parser.BoundaryParser;
import com.chunkmapper.parser.CoastlineParser;
import com.chunkmapper.parser.LakeParser;
import com.chunkmapper.parser.POIParser;
import com.chunkmapper.parser.RailParser;
import com.chunkmapper.parser.RiverParser;
import com.chunkmapper.protoc.ServerInfoContainer.ServerInfo;
import com.chunkmapper.protoc.admin.ServerInfoManager;
import com.chunkmapper.sections.Section;

public class OSMDownloader {
	private static final int NUM_DOWNLOADING_THREADS = 6;
	private static final DefaultHttpClient httpclient = Downloader.getHttpClient();
	private static final ConcurrentHashMap<PointSource, ArrayList<String>> cache = new ConcurrentHashMap<PointSource, ArrayList<String>>();



	public static Collection<? extends Section> getSections(OSMSource source, int regionx, int regionz) throws URISyntaxException, IOException {
		ServerInfo serverInfo = ServerInfoManager.getServerInfo();
		if (serverInfo.getGetXapi()) {
			//first and foremost we always try to get the latest xml from xapi
			PointSource ps = new PointSource(regionx, regionz, source);
			ArrayList<String> lines;
			HttpGet httpGet = null;
			HttpResponse response = null;
			HttpEntity entity = null;
			BufferedReader in = null;
			try {
				if (cache.containsKey(ps)) {
					lines = cache.get(ps);
				} else {
					URL url = null;
					switch(source) {
					case lakes:
						url = InfoManager.lakesServer(regionx, regionz);
						break;
					case poi:
						url = InfoManager.poiServer(regionx, regionz);
						break;
					case rivers:
						url = InfoManager.riversServer(regionx, regionz);
						break;
					case boundaries:
						url = InfoManager.boundariesServer(regionx, regionz);
						break;
					case coastlines:
						url = InfoManager.coastlinesServer(regionx, regionz);
						break;
					case rails:
						url = InfoManager.railsServer(regionx, regionz);
						break;
					}

					httpGet = new HttpGet(url.toURI());
					response = httpclient.execute(httpGet);
					entity = response.getEntity();
					in = new BufferedReader(new InputStreamReader(entity.getContent()));
					lines = new ArrayList<String>();
					String tempLine;
					while ((tempLine = in.readLine()) != null) {
						lines.add(tempLine);
					}
					if (lines.size() == 0) {
						throw new RuntimeException("xml empty");
					}
					String finalLine = lines.get(lines.size() - 1);
					if (!finalLine.trim().equals("</osm>")) {
						throw new RuntimeException("invalid osm");
					}
					//print
					cache.put(ps, lines);
				}
				switch(source) {
				case lakes:
					return LakeParser.getLakes(lines);
				case poi:
					return POIParser.getPois(lines);
				case rivers:
					return RiverParser.getRiverSections(lines);
				case boundaries:
					return BoundaryParser.getBoundaries(lines);
				case coastlines:
					return CoastlineParser.getCoastlines(lines);
				case rails:
					return RailParser.getRailSections(lines);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (entity != null)
				EntityUtils.consumeQuietly(entity);
				if (httpGet != null)
				httpGet.releaseConnection();
			}
		}
		throw new RuntimeException("unimplemented");
		//		switch(source) {
		//		case lakes:
		//		return BinaryLakeParser.getLakes(regionx, regionz);
		//		break;
		//		case poi:
		//		return 
		//		break;
		//		case rivers:
		//		f = InfoManager.riversFile(regionx, regionz);
		//		break;
		//		case boundaries:
		//		f = InfoManager.boundariesFile(regionx, regionz);
		//		break;
		//		case coastlines:
		//		f = InfoManager.coastlinesFile(regionx, regionz);
		//		break;
		//		case rails:
		//		f = InfoManager.railsFile(regionx, regionz);
		//		break;
		//		}
	}
	public static DefaultHttpClient getHttpClient() {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		//		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		//		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");

		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(NUM_DOWNLOADING_THREADS);
		cm.setDefaultMaxPerRoute(NUM_DOWNLOADING_THREADS);

		DefaultHttpClient httpclient = new DefaultHttpClient(cm);

		//timeout
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		HttpConnectionParams.setSoTimeout(params, 10000);

		httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(
					final HttpRequest request,
					final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip");
				}
			}

		});

		httpclient.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(
					final HttpResponse response,
					final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						HeaderElement[] codecs = ceheader.getElements();
						for (int i = 0; i < codecs.length; i++) {
							if (codecs[i].getName().equalsIgnoreCase("gzip")) {
								response.setEntity(
										new GzipDecompressingEntity(response.getEntity()));
								return;
							}
						}
					}
				}
			}

		});
		return httpclient;
	}
	private static class PointSource {
		public final Point p;
		public final OSMSource source;
		public PointSource(int regionx, int regionz, OSMSource source) {
			this.source = source;
			p = new Point(regionx, regionz);
		}
		public int hashCode() {
			return p.hashCode();
		}
		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (!(other instanceof PointSource))
				return false;
			PointSource other2 = (PointSource) other;
			return other2.p.equals(p) && other2.source.equals(source);
		}
	}

}
