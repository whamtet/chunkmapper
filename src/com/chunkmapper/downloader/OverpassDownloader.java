package com.chunkmapper.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
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
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.Point;
import com.chunkmapper.parser.Nominatim;

public class OverpassDownloader {
	private static final int NUM_DOWNLOADING_THREADS = 6;
	private static final DefaultHttpClient httpclient = Downloader.getHttpClient();
	private static final ConcurrentHashMap<Point, ArrayList<String>> generalCache = new ConcurrentHashMap<Point, ArrayList<String>>();
	private static final String generalQuery, testQuery;
	static {
		String q1 = null;
		String q2 = null;
		try {
			q1 = getQuery("/mainQuery.xml");
			q2 = getQuery("/testQuery.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		generalQuery = q1;
		testQuery = q2;
	}
	public static void main(String[] args) throws Exception {
//		double[] latlon = Nominatim.getPoint("sydney");
//		int regionx = (int) Math.floor(latlon[1] * 3600 / 512);
//		int regionz = (int) Math.floor(-latlon[0] * 3600 / 512);
		int regionx = 1060, regionz = 238;
		System.out.println(getLines(regionx, regionz, false).size());
	}

	public static ArrayList<String> getLines(int regionx, int regionz, boolean test) throws IOException {
		Point p = new Point(regionx, regionz);
		if (generalCache.containsKey(p)) {
			return generalCache.get(p);
		} else {
			String query = test ? testQuery : generalQuery;
			ArrayList<String> lines = doGetLines(query, regionx, regionz);
			generalCache.put(p, lines);
			return lines;
		}
	}
	
	private static ArrayList<String> doGetLines(String query, int regionx, int regionz) throws IOException {
		final double REGION_WIDTH_IN_DEGREES = 512 / 3600.;
		double lon1 = regionx * REGION_WIDTH_IN_DEGREES;
		double lon2 = lon1 + REGION_WIDTH_IN_DEGREES;
		double lat2 = -regionz * REGION_WIDTH_IN_DEGREES;
		double lat1 = lat2 - REGION_WIDTH_IN_DEGREES;
		String header = String.format("<osm-script bbox=\"%s,%s,%s,%s\">", lat1, lon1, lat2, lon2);
		String queryString = header + query + "</osm-script>";

		HttpPost httpPost = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		BufferedReader in = null;
		
		try {
			
			httpPost = new HttpPost("http://www.overpass-api.de/api/interpreter");
			httpPost.setEntity(new StringEntity(queryString));
			response = httpclient.execute(httpPost);
			ArrayList<String> lines = new ArrayList<String>();
			entity = response.getEntity();
			in = new BufferedReader(new InputStreamReader(entity.getContent()));
			String tempLine;
			while ((tempLine = in.readLine()) != null) {
				lines.add(tempLine);
			}
			return lines;
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
			if (httpPost != null)
			httpPost.releaseConnection();
		}
	}
	

	private static String getQuery(String queryFile) throws IOException {
		URL src = OverpassDownloader.class.getResource(queryFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(src.openStream()));
		StringBuilder out = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			out.append(line + "\n");
		}
		return out.toString();
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
		int timeout = 1000000;
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, timeout);
		HttpConnectionParams.setSoTimeout(params, timeout);
		params.setParameter("http.socket.timeout", timeout);

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


}
