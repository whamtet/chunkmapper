package com.chunkmapper.downloader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import com.chunkmapper.admin.MyLogger;

public class OverpassDownloader {
	private static final int NUM_DOWNLOADING_THREADS = 6;
	private static DefaultHttpClient httpclient = Downloader.getHttpClient();
	private static final String generalQuery;

	static {
		String q1 = null;
		try {
			q1 = getQuery("/mainQuery.xml");
		} catch (IOException e) {
			MyLogger.LOGGER.severe(MyLogger.printException(e));
		}
		generalQuery = q1;
	}

	public static ArrayList<String> getLines(int regionx, int regionz) throws IOException {
		return doGetLines(generalQuery, regionx, regionz);
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
		HttpResponse response;
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
				MyLogger.LOGGER.severe(MyLogger.printException(e1));
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


}
