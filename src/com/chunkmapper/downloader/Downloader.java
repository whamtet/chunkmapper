package com.chunkmapper.downloader;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import com.chunkmapper.Point;
import com.chunkmapper.Tasker;


public abstract class Downloader extends Tasker {
	private static final int NUM_DOWNLOADING_THREADS = 6;
	protected final DefaultHttpClient httpclient = getHttpClient();

	
	protected abstract void download(Point p) throws Exception;
	protected final void doTask(Point p) throws Exception {
		download(p);
	}



	public Downloader() {
		super(NUM_DOWNLOADING_THREADS, null, null, null);
	}

	private static DefaultHttpClient getHttpClient() {
		PoolingClientConnectionManager cm = new PoolingClientConnectionManager();
		cm.setMaxTotal(NUM_DOWNLOADING_THREADS);
		cm.setDefaultMaxPerRoute(NUM_DOWNLOADING_THREADS);
		//		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager();
		//	    cm.setMaxTotal();
		//	    cm.setDefaultMaxPerRoute(maxConnections);
		DefaultHttpClient httpclient = new DefaultHttpClient(cm);
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
