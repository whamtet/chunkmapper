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

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.chunkmapper.admin.Utila;

public class MySecurityManager {
	
	private static final File keyFile = new File(Utila.CACHE, "key");
	
	public static enum Status {
		OK, HACKED, UNPAID, INVALID_PW;
	}

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
		sb.append(new String(mac) + "\n");
		sb.append(System.getProperty("os.arch") + "\n");
		sb.append(System.getProperty("user.home") + "\n");
		sb.append(System.getProperty("user.name") + "\n");
		return sb.toString();
	}
	private static String md5(String input) {

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
	private static String readEntireFile() throws IOException {
		FileReader in = new FileReader(keyFile);
		StringBuilder contents = new StringBuilder();
		char[] buffer = new char[4096];
		int read = 0;
		do {
			contents.append(buffer, 0, read);
			read = in.read(buffer);
		} while (read >= 0);
		return contents.toString();
	}
	private static void spit(String s) {
		try {
			FileWriter out = new FileWriter(keyFile);
			out.write(s);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Status getOnlineStatus(String email, String password) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(
				"https://secure.chunkmapper.com/authenticate");
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("email", email));
		nameValuePairs.add(new BasicNameValuePair("password", password));
		
		try {
			postRequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String responseLine = rd.readLine();
			rd.close();
			if ("ok".equals(responseLine)) return Status.OK;
			if ("hacked".equals(responseLine)) return Status.HACKED;
			if ("unpaid".equals(responseLine)) return Status.UNPAID;
			if ("invalid password".equals(responseLine)) return Status.INVALID_PW;
			return null;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static boolean isOfflineValid() {
		try {
			String key = readEntireFile().trim();
			return key.equals(getKey());
		} catch (IOException e) {}
		return false;
	}
	public static Status getStatus(String username, String password) {
		if (username == null || password == null) return Status.INVALID_PW;
		if ("".equals(username.trim()) || "".equals(password.trim())) return Status.INVALID_PW;
		Status s = getOnlineStatus(username, password);
		if (Status.OK.equals(s)) {
			spit(getKey());
		}
		return s;
	}
	public static void main(String[] args) throws Exception {
		JOptionPane.showMessageDialog(null, getRawKey());
		
	}

}
