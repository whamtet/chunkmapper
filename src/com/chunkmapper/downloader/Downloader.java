package com.chunkmapper.downloader;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.chunkmapper.Point;
import com.chunkmapper.Tasker;


public abstract class Downloader extends Tasker {
	private static final int NUM_DOWNLOADING_THREADS = 6;
	protected final DefaultHttpClient httpclient = getHttpClient();

	
	protected abstract void download(Point p, boolean useBackupServer) throws Exception;
	protected final void doTask(Point p) throws Exception {
		try {
		download(p, false);
		} catch (ConnectTimeoutException e) {
			e.printStackTrace();
			download(p, true);
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			download(p, true);
		}
	}

	public void shutdownNow() {
		super.shutdownNow();
		httpclient.getConnectionManager().shutdown();
	}
	public void blockingShutdownNow() {
		httpclient.getConnectionManager().shutdown();
		super.blockingShutdownNow();
	}



	public Downloader() {
		super(NUM_DOWNLOADING_THREADS);
	}

	private static DefaultHttpClient getHttpClient() {
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

}
