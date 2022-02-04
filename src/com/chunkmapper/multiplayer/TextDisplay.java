package com.chunkmapper.multiplayer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import com.chunkmapper.Point;
import com.chunkmapper.interfaces.MappedSquareManager;

public class TextDisplay implements MappedSquareManager {
	
	public TextDisplay(File chunkmapperDir, Point rootPoint) {
	}
	@Override
	public synchronized void addFinishedPoint(Point p) {
		System.out.printf("Added finished point at %s\n", p);
		
	}
	public void updatePlayer(String playerName, int regionx0, int regionz0) {
		System.out.printf("UpdatePlayer %s: %s, %s\n", playerName, regionx0, regionz0);
	}
	@Override
	public void addUnfinishedPoint(Point p) {
		// TODO Auto-generated method stub
		
	}
	


}
