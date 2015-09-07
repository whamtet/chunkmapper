package com.chunkmapper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.chunkmapper.admin.MyLogger;

public class FileValidator {
	
	/*The FileValidator class is a utility class that enables us to mark input files as 'Valid', whatever that means.
	 * In practice it usually means that the file has finished downloading.
	 * Because some of the source data is quite big, we mark a file as 'Valid' once it has finished downloading.
	 * setValid checks on file length only
	 * setSuperValid checks on md5.
	 */
	
	public static void setValid(File f) throws IOException {
		if (f.getName().endsWith("~"))
			return;

		File metaFile = new File(f.getAbsolutePath() + "~");
		FileWriter writer = new FileWriter(metaFile);
		writer.write(f.length() + "");
		writer.close();
	}
	public static boolean checkValid(File f) throws IOException {

		File metaFile = new File(f.getAbsolutePath() + "~");
		if (!f.exists() || !metaFile.exists())
			return false;

		BufferedReader reader = new BufferedReader(new FileReader(metaFile));
		boolean isValid = Long.parseLong(reader.readLine()) == f.length();
		reader.close();
		return isValid;
	}
	public static void setSupervalid(File f) throws IOException {
		//first read in bytes
		byte[] data = new byte[(int) f.length()];
		DataInputStream in = new DataInputStream(new FileInputStream(f));
		in.readFully(data);
		in.close();

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			MyLogger.LOGGER.warning(MyLogger.printException(e));
			return;
		}
		byte[] digest = md.digest(data);
		File metaFile = new File(f.getAbsolutePath() + "~~");
		FileOutputStream out = new FileOutputStream(metaFile);
		out.write(digest);
		out.close();
	}
	public static boolean checkSupervalid(File f) {
		try {
			File metaFile = new File(f.getAbsolutePath() + "~~");
			if (!metaFile.exists())
				return false;
			MessageDigest md = null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				MyLogger.LOGGER.warning(MyLogger.printException(e));
				return false;
			}
			byte[] data = new byte[(int) f.length()];
			DataInputStream in = new DataInputStream(new FileInputStream(f));
			in.readFully(data);
			in.close();
			byte[] digest = md.digest(data);

			data = new byte[(int) metaFile.length()];
			in = new DataInputStream(new FileInputStream(metaFile));
			in.readFully(data);
			in.close();

			if (data.length != digest.length)
				return false;
			for (int i = 0; i < data.length; i++) {
				if (data[i] != digest[i])
					return false;
			}
			return true;
		} catch (IOException e) {
			MyLogger.LOGGER.info(MyLogger.printException(e));
			return false;
		}
	}
	//	public static void main(String[] args) throws Exception {
	//		File f = new File("test.txt");
	//		FileWriter writer = new FileWriter(f);
	//		writer.write("hi old man");
	//		writer.close();
	//		
	//		setSupervalid(f);
	//		System.out.println(checkSupervalid(f));
	//	}

}
