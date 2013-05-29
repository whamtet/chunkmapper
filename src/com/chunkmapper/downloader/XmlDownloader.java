package com.chunkmapper.downloader;

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.FileValidator;
import com.chunkmapper.resourceinfo.ResourceInfo;

public abstract class XmlDownloader extends Downloader {

	protected void downloadXml(ResourceInfo fileToDownload) throws Exception {
		if (FileValidator.checkValid(fileToDownload.file))
			return;
		System.out.println("downloading " + fileToDownload.url);
		HttpGet httpGet = new HttpGet(fileToDownload.url.toURI());
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
			FileOutputStream out = new FileOutputStream(fileToDownload.file);

			byte[] buffer = new byte[8192];
			try {
				int i;
				while ((i = in.read(buffer)) != -1) {
					out.write(buffer, 0, i);
				}

			} finally {
				out.close();
				in.close();
				EntityUtils.consumeQuietly(entity);
				httpGet.releaseConnection();
			}
			FileValidator.setValid(fileToDownload.file);

	}

}
