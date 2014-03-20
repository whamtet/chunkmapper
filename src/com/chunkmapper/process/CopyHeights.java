package com.chunkmapper.process;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.chunkmapper.Zip;

public class CopyHeights {
	public static class StreamObject {
		private byte[] data; 
		private String name;
		
		public byte[] getData() {
			return data;
		}
		public String getName() {
			return name.toUpperCase();
		}
		public StreamObject(String name, byte[] data) {
			this.name = name;
			this.data = data;
		}
	}
	
	public static void main(String[] args) throws Exception {
		File f = new File("/Users/matthewmolloy/Downloads/Folx/46-60A.zip");
	}

}
