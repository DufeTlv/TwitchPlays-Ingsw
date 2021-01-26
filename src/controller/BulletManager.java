package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import model.Bullet;
import model.Enemy;
import model.Player;

public class BulletManager {
	private ArrayList<Bullet> eBullets;
	private ArrayList<Bullet> pBullets;
	
	public BulletManager() {
		eBullets = new ArrayList<>();
		pBullets = new ArrayList<>();
	}
	
	public void addEnemyBullets(int x, int y, int x1, int y1) {
		int offset = (new Random().nextInt(20)-10) *3;
		eBullets.add(new Bullet(x, y, x1, y1+offset, 5, 15, "gameAssets/sprites/snowBall_Enemy.png"));
	}
	
	public void addBossBullets(int x, int y, int x1, int y1) {
		eBullets.add(new Bullet(x, y, x1, y1, 3, 25, "gameAssets/sprites/snowBall_Boss.png"));
	}
	
	public void addPlayerBullets(int x, int y, int x1, int y1) {
		pBullets.add(new Bullet(x, y, x1, y1, 5, 15, "gameAssets/sprites/snowBall.png"));
	}
	
	public void draw(Graphics2D g2d) {
		for(Bullet b: pBullets) {
			b.draw(g2d);
		}
		for(Bullet b: eBullets) {
			b.draw(g2d);
		}
	}
	
	public void update(Rectangle floor, ArrayList<Enemy> enemies, Player player) {
		for(int i = 0; i < pBullets.size(); ++i) {			
			Bullet b = pBullets.get(i);
			
			b.update();
			
			
			/* elimina/rimuove il proiettile se avviene un collisione 
			 * con le mura o con un nemico, nel secondo caso decrementa anche 
			 * la vita del nemico*/
			if(!floor.contains(b))
				pBullets.remove(i);
			
			/* scorro i nemici*/
			for(Enemy e: enemies) {
				if( e.contains(b) ){
					e.damage(b.getDamage());
					pBullets.remove(i);
				}
			}			
			
		}
		
		for(int i = 0; i < eBullets.size(); ++i) {
			Bullet b = eBullets.get(i);
			b.update();
			
			if(!floor.contains(b))
				eBullets.remove(i);
			
			if(player.contains(b)) {
				//player.damage(b.getDamage());
				eBullets.remove(i);
			}
		}
		
	}
	

}
