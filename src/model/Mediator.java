package model;

import controller.BulletManager;

public class Mediator {
	
	private BulletManager bM;
	private Player p;
	
	public Mediator(BulletManager _b, Player _p) {
		bM = _b;
		p = _p;
	}
	
	public void shoot(int x, int y) {
		if( Math.sqrt( Math.pow(p.getCentralX() - x, 2) + Math.pow(p.getCentralY() - y, 2)) < 200*3 )
			bM.addEnemyBullets(x, y, p.getCentralX(), p.getCentralY());
	}

}
