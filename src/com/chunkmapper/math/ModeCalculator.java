package com.chunkmapper.math;

import java.util.HashMap;

public class ModeCalculator {
	private HashMap<Byte, Integer> data = new HashMap<Byte, Integer>();
	public void addValue(byte b) {
		if (data.containsKey(b)) {
			data.put(b, data.get(b) + 1);
		} else {
			data.put(b, 1);
		}
	}
	public byte getMode() {
		int maxCount = 0;
		byte mode = 0;
		for (Byte key : data.keySet()) {
			int count = data.get(key);
			if (count > maxCount) {
				mode = key;
				maxCount = count;
			}
		}
		return mode;
	}

}
