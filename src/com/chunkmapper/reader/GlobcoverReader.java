package com.chunkmapper.reader;

import com.chunkmapper.enumeration.Globcover;

public interface GlobcoverReader {

	public Globcover getGlobcover(int i, int j);

	public boolean mostlyLand();

}