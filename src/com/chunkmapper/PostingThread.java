package com.chunkmapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.chunkmapper.admin.BucketInfo;
import com.chunkmapper.admin.MyLogger;

@Deprecated
public class PostingThread extends Thread {
	
	/*
	 * Thread to post generated minecraft saved files to a remote server.
	 * No longer used, for reference only.
	 */
	
	private HashSet<Point> posted = new HashSet<Point>();
	private final File regionDir, store;
	private final Point rootPoint;
	
	{
		setName("PostingThread");
	}
	public PostingThread(File gameDir, Point rootPoint) throws IOException {
		regionDir = new File(gameDir, "region");
		this.rootPoint = rootPoint;
		store = new File(gameDir, "chunkmapper/posted.txt");
		if (store.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(store));
			String line;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				int regionx = Integer.parseInt(split[0]);
				int regionz = Integer.parseInt(split[1]);
				posted.add(new Point(regionx, regionz));
			}
			br.close();
		}
	}
	private PostingThread(Point rootPoint) {
		this.rootPoint = rootPoint;
		regionDir = null;
		store = null;
	}
	private class FilePoint {
		public final File f;
		public final Point p, relp;
		public FilePoint(File f) {
			this.f = f;
			String[] split = f.getName().split("\\.");
			int relx = Integer.parseInt(split[1]);
			int relz = Integer.parseInt(split[2]);
			p = new Point(rootPoint.x + relx, rootPoint.z + relz);
			relp = new Point(relx, relz);
		}
	}
	private ArrayList<FilePoint> getTodo() {
		ArrayList<FilePoint> out = new ArrayList<FilePoint>();

		for (File f : regionDir.listFiles()) {
			if (f.getName().endsWith(".mca")) {
				FilePoint fp = new FilePoint(f);
				if (!posted.contains(fp.p)) {
					out.add(fp);
				}
			}
		}

		return out;
	}
	@Override
	public void run() {
		while (true) {
			if (Thread.interrupted()) {
				MyLogger.LOGGER.info("Interrupted PostingThread");
				return;
			}
			ArrayList<FilePoint> todo;
			while(true) {
				todo = getTodo();
				if (todo.isEmpty()) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						MyLogger.LOGGER.info(MyLogger.printException(e));
						return;
					}
				} else {
					break;
				}
			}
			for (FilePoint fp : todo) {
				if (Thread.interrupted()) {
					MyLogger.LOGGER.info("Interrupted PostingThread");
					return;
				}
				if (FileValidator.checkSupervalid(fp.f)) {
					try {
						post(fp);
						posted.add(fp.p);
						spit();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						MyLogger.LOGGER.warning(MyLogger.printException(e));
					}
				}
			}
		}
	}
	private void spit() throws IOException {
		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(store)));
		for (Point p : posted) {
			pw.println(p.x + " " + p.z);
		}
		pw.close();
	}
	private void post(FilePoint fp) throws IOException {
		String host = BucketInfo.imageEndpoint();
//		String host = "http://localhost:5000";
		//first we need to see if it's actually available
		URL url = new URL(String.format("%s/available?regionx=%s&regionz=%s", host, fp.p.x, fp.p.z));
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = br.readLine();
		br.close();
		if ("true".equals(line)) {
		
			//we're in business!
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postRequest = new HttpPost(host + "/paint");

			MultipartEntity reqEntity = new MultipartEntity();
			reqEntity.addPart("mca", new ByteArrayBody(Zip.readFully(fp.f), "mca"));
			reqEntity.addPart("regionx", new StringBody(fp.p.x + ""));
			reqEntity.addPart("regionz", new StringBody(fp.p.z + ""));
			reqEntity.addPart("relx", new StringBody(fp.relp.x + ""));
			reqEntity.addPart("relz", new StringBody(fp.relp.z + ""));
			
			postRequest.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(postRequest);
//			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//			while ((line = rd.readLine()) != null) {
//				System.out.println(line);
//			}
		}
	}
	public static void main(String[] args) throws Exception {
		System.out.println("starting");
		File gameDir = new File("/Users/matthewmolloy/Library/Application Support/minecraft/saves/Hong Kong/");
		System.out.println("done");
	}
}

