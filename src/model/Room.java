package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Room{
	
	private Rectangle floor, bridges[]; 		// collisioni e ponti della stanza
	private ArrayList<AnimatedObject> tiles;	// pavimentazione
	private boolean visited;
	
	public Room(int _x, int _y) {
		
		floor = new Rectangle(_x, _y, 256*3, 160*3);
		tiles = new ArrayList<>();
		bridges = new Rectangle[4];
		bridges[0] = bridges[1] = bridges[2] = bridges[3] = null;
		
		visited = false;
		
		generateTiles();
		
	}
	
	public int getUpperSide() {
		return floor.y;
	}
	public int getBottomSide() {
		return floor.y+floor.height;
	}
	public int getLeftSide() {
		return floor.x;
	}
	public int getRightSide() {
		return floor.x+floor.width;
	}
	
	public Rectangle[] getBridges() {
		return bridges;
	}
	
	public void setVisited(boolean v) {
		visited = v;
	}
	
	public void setBridge(int i, Rectangle b) {
		bridges[i] = b;
	}
	
	public Rectangle getBridge(int i) {
		return bridges[i];
	}
	
	public void changeWaterState(int state) {
		for(AnimatedObject o: tiles) {
			if(o.currentAnimation != 0 && o.currentAnimation != state)
				o.changeAnimation(state);
		}
	}
	
	/* genera una griglia[10x16] di Tiles, randomicamente */
	public void generateTiles() {
		
		for(int r = 0; r < 10; ++r) {
			for(int c = 0; c < 16; ++c) {
				
				AnimatedObject newTile = new AnimatedObject(floor.x+((floor.width/16)*c), floor.y+((floor.height/10)*r), "gameAssets/sprites/tileMap.png", 8);
				newTile.addAnimation(0, 0, 0, false);
				newTile.addAnimation(1, 1, 0, false);
				newTile.addAnimation(2, 4, 15, false);
				newTile.addAnimation(5, 7, 15, false);
				newTile.changeAnimation(new Random().nextInt(2));
				tiles.add(newTile);
				
			}
		}
		
	}
	
	public Rectangle getFloor() {
		return floor;
	}
	
	/* metodo di stampa momentano per vedere i contorni della stanza */
	public void drawRoom(Graphics2D g2d) {
		g2d.drawRect(floor.x, floor.y, floor.width, floor.height);
		
		if(bridges[0] != null) g2d.drawRect(bridges[0].x, bridges[0].y, bridges[0].width, bridges[0].height);
		if(bridges[1] != null) g2d.drawRect(bridges[1].x, bridges[1].y, bridges[1].width, bridges[1].height);
		if(bridges[2] != null) g2d.drawRect(bridges[2].x, bridges[2].y, bridges[2].width, bridges[2].height);
		if(bridges[3] != null) g2d.drawRect(bridges[3].x, bridges[3].y, bridges[3].width, bridges[3].height);
		
	}
	
	public void draw(Graphics2D g2d) {
		for(AnimatedObject o: tiles) {
			o.draw(g2d);
		}
		
		drawRoom(g2d);
	}
	

}
