package model;

public class Bridge extends Object{
	
	int roomA, roomB;

	public Bridge(int _x, int _y, int rA, int rB, boolean horizontal) {
		super(_x, _y, "gameAssets/sprites/tileMap.png");
		
		width /= 8;
		roomA = rA;
		roomB = rB;
		
		
	}

}
