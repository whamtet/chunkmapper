package com.chunkmapper.downloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.FileValidator;

public class SynchronousDownloader {
	private final DefaultHttpClient httpclient = Downloader.getHttpClient();
	
	public void shutdown() {
		httpclient.getConnectionManager().shutdown();
	}
	
	public byte[] downloadToFile(String url, File outputFile) throws IOException, URISyntaxException {
		
		HttpGet httpGet = new HttpGet(new URI(url));
		HttpResponse httpResponse = httpclient.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		
		InputStream in = entity.getContent();
		FileOutputStream out = new FileOutputStream(outputFile);
		ByteArrayOutputStream out2 = new ByteArrayOutputStream();
		
		byte[] buffer = new byte[8192];
		int bytesRead;
		while ((bytesRead = in.read(buffer)) != -1) {
			out.write(buffer, 0, bytesRead);
			out2.write(buffer, 0, bytesRead);
		}
		
		in.close();
		out.close();
		FileValidator.setValid(outputFile);
		EntityUtils.consumeQuietly(entity);
		httpGet.releaseConnection();
		
		return out2.toByteArray();
	}

}
