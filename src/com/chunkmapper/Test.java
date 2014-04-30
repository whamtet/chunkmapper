package com.chunkmapper;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;

public class Test {
	public static void main(String[] args) throws Exception {
		File f = new File("test.txt");
		f.delete();
		
		
		PrintWriter pw = new PrintWriter(new FileWriter(f));
		pw.println("ls");
		pw.close();
		
		Path p = FileSystems.getDefault().getPath(f.getPath());
		HashSet<PosixFilePermission> permissions = new HashSet<PosixFilePermission>();
		permissions.add(PosixFilePermission.OWNER_EXECUTE);
		permissions.add(PosixFilePermission.GROUP_EXECUTE);
		permissions.add(PosixFilePermission.OTHERS_EXECUTE);
		permissions.add(PosixFilePermission.OWNER_READ);
		permissions.add(PosixFilePermission.GROUP_READ);
		permissions.add(PosixFilePermission.OTHERS_READ);
		Files.setPosixFilePermissions(p, permissions);
		
		System.out.println("done");
	}

}