package com.chunkmapper.downloader;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.FileValidator;
import com.chunkmapper.resourceinfo.ResourceInfo;

public abstract class PngDownloader extends Downloader {

	protected void downloadPng(ResourceInfo fileToDownload) throws URISyntaxException, IOException {
		if (FileValidator.checkValid(fileToDownload.file))
			return;
		System.out.println("downloading " + fileToDownload.url);
		HttpGet httpGet = new HttpGet(fileToDownload.url.toURI());
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		try {
			BufferedImage im = ImageIO.read(in);
			ImageIO.write(im, "png", fileToDownload.file);
			FileValidator.setValid(fileToDownload.file);
		} finally {
			in.close();
			EntityUtils.consumeQuietly(entity);
			httpGet.releaseConnection();
		}
	}

}
