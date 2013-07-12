package com.chunkmapper.worldpainter;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.vecmath.Point3i;

import org.apache.commons.io.FileUtils;
import org.pepsoft.minecraft.Material;
import org.pepsoft.worldpainter.layers.bo2.Schematic;

import com.chunkmapper.enumeration.Blocka;
import com.chunkmapper.enumeration.DataSource;

public class CopyTrees {

	@SuppressWarnings("restriction")
	private static boolean isNeedleLeaf(File f) throws IOException {
		String name = f.getName();
		if (name.contains("Fir") || name.contains("Pine") || name.contains("Larch")) {
			return true;
		}
		Schematic s = Schematic.load(f);
		Point3i d = s.getDimensions();
		for (int x = 0; x < d.x; x++) {
			for (int y = 0; y < d.y; y++) {
				for (int z = 0; z < d.z; z++) {
					if (s.getMask(x, y, z)) {
						Material m = s.getMaterial(x, y, z);
						if (m.getBlockType() == Blocka.Leaves && (m.getData() & 3) == 1) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static void main(String[] args) throws IOException {
		File[] directories = {
				new File("/Users/matthewmolloy/Downloads/Custom Tree Repository by Lentebriesje/Tree Schematics/Native Trees of Europe"),
				new File("/Users/matthewmolloy/Downloads/Custom Tree Repository by Lentebriesje/Tree Schematics/Native Trees of North America"),
				new File("/Users/matthewmolloy/Downloads/Custom Tree Repository by Lentebriesje/Tree Schematics/Trees of the Amazon")
		};
		FileUtils.deleteDirectory(new File("resources/trees"));
		int i = 0;
		for (File directory : directories) {
			for (File f : directory.listFiles()) {
				if ((f.getName().contains("Small") || f.getName().startsWith("73") || f.getName().startsWith("74"))
						&& !f.getName().contains("Sandbox")) {
					String[] split = f.getName().split(" \\[");
					File destDir = new File("resources/trees");
//					if (directory.getName().startsWith("Native")) {
//						destDir = isNeedleLeaf(f) ? new File("resources/trees/needleleaf") : new File("resources/trees/broadleaf"); 
//					} else {
//						destDir = new File("resources/trees/tropical");
//					}
					String name = split[0];
					if (name.startsWith("73") || name.startsWith("74")) {
						name = "f_" + name;
					}
					File dest = new File(destDir, name + ".schematic");
					FileUtils.copyFile(f, dest);
				}
			}
		}
						

	}

}
