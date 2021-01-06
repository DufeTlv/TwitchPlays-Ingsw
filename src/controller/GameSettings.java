package controller;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GameSettings {

	private static GameSettings instance = null;
	
	private Dimension screenSize;
	private int resolution[];
	
	private GameSettings() {
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		resolution = new int[2];
		resolution[0] = 800;
		resolution[1] = 600;
 		
	}
	
	public void setResolution(int w, int h) {
		resolution[0] = w;
		resolution[1] = h;
	}
	
	public int getWRes() {
		return resolution[0];
	}
	
	public int getHRes() {
		return resolution[1];
	}
	
	public static GameSettings getInstance() {
		if(instance == null)
			instance = new GameSettings();
		
		return instance;
	}
}
