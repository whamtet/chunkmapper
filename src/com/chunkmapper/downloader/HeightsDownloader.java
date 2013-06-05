package com.chunkmapper.downloader;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import com.chunkmapper.FileValidator;
import com.chunkmapper.Point;
import com.chunkmapper.resourceinfo.HeightsResourceInfo;

public class HeightsDownloader extends Downloader {

	@Override
	protected void download(Point task) throws Exception {

		HeightsResourceInfo fileToDownload = new HeightsResourceInfo(task.x, task.z);
		if (FileValidator.checkValid(fileToDownload.file)) {
			System.out.println(fileToDownload.file.toString() + " is valid");
			return;
		}

		System.out.println("downloading " + fileToDownload.url);
		HttpGet httpGet = new HttpGet(fileToDownload.url.toURI());
		HttpResponse response = httpclient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		try {
			byte[] buffer = new byte[HeightsResourceInfo.FILE_LENGTH], buffer2 = new byte[HeightsResourceInfo.FILE_LENGTH];
			int i = 0;
			while (true) {
				int amountRead = in.read(buffer);
				if (amountRead < 0)
					break;
				for (int j = 0; j < amountRead; j++) {
					buffer2[i+j] = buffer[j];
				}
				i += amountRead;
			}
			
				BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(fileToDownload.file));
				out.write(buffer2);
				out.close();
				FileValidator.setValid(fileToDownload.file);
		} finally {
			in.close();
			EntityUtils.consumeQuietly(entity);
			httpGet.releaseConnection();
		}

	}

}
