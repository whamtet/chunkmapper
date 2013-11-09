package com.chunkmapper.security;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class SecurityManager {
	public static final int INVALID = 0, REQUIRES_LOGIN = 1, VALID = 2;
	private static final File keyFile = new File(FileUtils.getUserDirectory(), ".chunkmapper/key");

	private static String getRawKey() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		NetworkInterface network = null;
		try {
			network = NetworkInterface.getByInetAddress(ip);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		byte[] mac = null;
		try {
			mac = network.getHardwareAddress();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(new String(mac));
		sb.append(System.getProperty("java.home"));
		sb.append(System.getProperty("java.vendor"));
		sb.append(System.getProperty("java.version"));
		sb.append(System.getProperty("os.arch"));
		sb.append(System.getProperty("user.dir"));
		sb.append(System.getProperty("user.home"));
		sb.append(System.getProperty("user.name"));
		return sb.toString();
	}
	public static String md5(String input) {

		String md5 = null;

		if(null == input) return null;

		try {

			//Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("SHA-512");

			//Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			//Converts message digest value in base 16 (hex) 
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}
	private static String getKey() {
		return md5(getRawKey());
	}
	private static String readEntireFile(File f) throws IOException {
		FileReader in = new FileReader(f);
		StringBuilder contents = new StringBuilder();
		char[] buffer = new char[4096];
		int read = 0;
		do {
			contents.append(buffer, 0, read);
			read = in.read(buffer);
		} while (read >= 0);
		return contents.toString();
	}
	private static void spit(File f, String s) {
		try {
			FileWriter out = new FileWriter(keyFile);
			out.write(s);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static int onlineValidity(String email, String password) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(
				"http://backend.chunkmapper.com/check");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpResponse response = null;
		try {
			response = httpClient.execute(postRequest);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader rd = null;
		try {
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String responseLine = null;
		try {
			responseLine = rd.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			int numLogins = Integer.parseInt(responseLine);
			if (numLogins < 200) {
				spit(keyFile, getKey());
				return VALID;
			} else {
				return REQUIRES_LOGIN;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return INVALID;

	}
	public static boolean isOfflineValid() {

		try {
			String key = readEntireFile(keyFile).trim();
			return key.equals(getKey());
		} catch (IOException e) {}
		return false;
	}
	public static void main(String[] args) throws Exception {
		System.out.println("hi");
	}

}
