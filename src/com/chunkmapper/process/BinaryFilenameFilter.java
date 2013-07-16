package com.chunkmapper.process;

import java.io.File;
import java.io.FilenameFilter;

public class BinaryFilenameFilter implements FilenameFilter {

	@Override
	public boolean accept(File arg0, String arg1) {
		return arg1.startsWith("f_");
	}

}
