package com.chunkmapper.downloader;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


public class Downloader {

	// now only used for getHttpClient();

	private static final int NUM_DOWNLOADING_THREADS = 6;
	protected final DefaultHttpClient httpclient = getHttpClient();

	public static DefaultHttpClient getHttpClient() {
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
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

		httpclient.addRequestInterceptor((request, context) -> {
			if (!request.containsHeader("Accept-Encoding")) {
				request.addHeader("Accept-Encoding", "gzip");
			}
		});

		httpclient.addResponseInterceptor((response, context) -> {
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
		});
		return httpclient;
	}

}
