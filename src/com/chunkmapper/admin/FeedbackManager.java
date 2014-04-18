package com.chunkmapper.admin;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class FeedbackManager {
	private static final ByteArrayOutputStream loggingStream = new ByteArrayOutputStream();
	public static final StreamHandler streamHandler = new StreamHandler(loggingStream, new SimpleFormatter());
	
	public static String getLogs() {
		streamHandler.flush();
		return loggingStream.toString();
	}
	public static void main(String[] args) throws Exception {
		System.out.println(getProperties());
	}
	public static String getProperties() {
		StringBuilder sb = new StringBuilder();
		Properties p = System.getProperties();
		for (Object k : p.keySet()) {
			sb.append(k + ": " + p.get(k) + "\n");
		}
		return sb.toString();
	}
	public static void submitFeedback(List<NameValuePair> nameValuePairs) {
		if (nameValuePairs == null) {
			nameValuePairs = new ArrayList<NameValuePair>();
		}
		HttpClient httpClient = new DefaultHttpClient();
//		String addr = Utila.isMatt() ? "http://localhost:5000/feedback" : "https://secure.chunkmapper.com/feedback";
		String addr = "https://secure.chunkmapper.com/feedback";
		HttpPost postRequest = new HttpPost(
				//"http://localhost:5000/feedback"
				//"https://secure.chunkmapper.com/feedback"
				addr
				);
		
		nameValuePairs.add(new BasicNameValuePair("version", "2"));
		nameValuePairs.add(new BasicNameValuePair("logs", getLogs()));
		//system properties
		nameValuePairs.add(new BasicNameValuePair("system_properties", getProperties()));
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(postRequest);
		} catch (Exception e) {
			MyLogger.LOGGER.warning(MyLogger.printException(e));
		}
		
	}
	
}
