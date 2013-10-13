package com.chunkmapper;

import java.util.HashMap;

import javax.swing.JOptionPane;

public class Test {
	private static HashMap<Point, Point> map = new HashMap<Point, Point>();
	public static void main(String[] args) throws Exception {
		JOptionPane.showConfirmDialog(null,
                "Are you sure?",
                "Delete Cache", 
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
	}
}
