package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Room{
	
	private Rectangle floor; 					// contorni della stanza
	private ArrayList<AnimatedObject> tiles;	// pavimentazione
	private int bridges[];						
	
	public Room(int _x, int _y) {
		
		floor = new Rectangle(_x, _y, 256*3, 160*3);
		tiles = new ArrayList<>();
		
		bridges = new int[4];
		bridges[0] = bridges[1] = bridges[2] = bridges[3] = -1;
		
		generateTiles();
		
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

	public void setBridge(int n, int room) {
		bridges[n] = room;
	}
	
	public Rectangle getFloor() {
		return floor;
	}
	
	/* metodo di stampa momentano per vedere i contorni della stanza */
	public void drawRoom(Graphics2D g2d) {
		g2d.drawRect(floor.x, floor.y, floor.width, floor.height);
	}
	
	public void draw(Graphics2D g2d) {
		for(AnimatedObject o: tiles) {
			o.draw(g2d);
		}
	}
	

}
