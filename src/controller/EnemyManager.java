package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import model.Enemy;
import model.EnemyMonkey;
import model.Mediator;

public class EnemyManager {
	ArrayList<Enemy> enemies;
	
	public EnemyManager() {
		enemies = new ArrayList<Enemy>();
	}
	
	public void addEnemies(Rectangle room, int quantity, Mediator m) {
		
		Random random = new Random();
		for(int i = 0; i < quantity; ++i)
			enemies.add(new EnemyMonkey(random.nextInt(room.width-room.width/4)+room.x, random.nextInt(room.height-(room.height/4))+room.y, "gameAssets/sprites/enemy.png", 16, m));
		
	}
	
	public boolean roomClear() {
		return enemies.isEmpty();
	}
	
	public ArrayList<Enemy> getEnemies(){
		return enemies;
	}
	
	public void draw(Graphics2D g2d) {
		for(Enemy e: enemies) {
			e.draw(g2d);
		}
	}
	
	public void update(Rectangle room) {
		for(int i = 0; i < enemies.size(); ++i) {
			Enemy e = enemies.get(i);
			e.update(room);
			
			if(e.isDead())
				enemies.remove(i);
			
		}
			
	}

}
