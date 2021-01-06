package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import model.Enemy;

public class EnemyManager {
	ArrayList<Enemy> enemies;
	
	public EnemyManager() {
		enemies = new ArrayList<Enemy>();
	}
	
	public void addEnemies(Rectangle room, int quantity) {
		
		Random random = new Random();
		for(int i = 0; i < quantity; ++i)
			enemies.add(new Enemy(random.nextInt(room.width-room.width/4)+room.x, random.nextInt(room.height-(room.height/4))+room.y, "gameAssets/sprites/enemy.png", 12));
		
	}
	
	public void draw(Graphics2D g2d) {
		for(Enemy e: enemies) {
			e.draw(g2d);
		}
	}
	
	public void update() {
		for(Enemy e: enemies) {
			e.update();
		}
	}

}
