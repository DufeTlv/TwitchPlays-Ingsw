package controller;

import java.awt.Graphics2D;
import java.util.ArrayList;
import model.Bullet;

public class BulletManager {
	private ArrayList<Bullet> eBullets;
	private ArrayList<Bullet> pBullets;
	
	public BulletManager() {
		eBullets = new ArrayList<>();
		pBullets = new ArrayList<>();
	}
	
	public void addEnemyBullets() {
		
	}
	
	public void addPlayerBullets(int x, int y, int x1, int y1) {
		pBullets.add(new Bullet(x, y, x1, y1, 5, "gameAssets/sprites/snowBall.png"));
	}
	
	public void draw(Graphics2D g2d) {
		for(Bullet b: pBullets) {
			b.draw(g2d);
		}
		for(Bullet b: eBullets) {
			b.draw(g2d);
		}
	}
	
	public void update() {
		for(Bullet b: pBullets) {
			b.update();
			
			/* eliminare/rimuovere il proiettile se avviene un collisione 
			 * con un nemico o con i muri, nel primo caso decrementare anche 
			 * la vita del nemico*/
			
		}
		for(Bullet b: eBullets) {
			b.update();
		}
		
	}
	

}
