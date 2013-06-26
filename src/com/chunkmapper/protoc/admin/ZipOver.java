package com.chunkmapper.protoc.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;

public class ZipOver {

	public static void zipOver(File src, File dest) throws IOException, DataFormatException {
		byte[] data = new byte[(int) src.length()], data2 = new byte[(int) src.length()];
		FileInputStream in = new FileInputStream(src);
		in.read(data);
		in.close();

		Deflater compresser = new Deflater(Deflater.BEST_COMPRESSION);
		compresser.setInput(data);
		compresser.finish();

		int cl = compresser.deflate(data2);
		
//		System.out.println(src.getName() + ": " + data.length + ", " + cl);

		FileOutputStream out = new FileOutputStream(dest);
		out.write(data2, 0, cl);
		out.close();

	}

}
