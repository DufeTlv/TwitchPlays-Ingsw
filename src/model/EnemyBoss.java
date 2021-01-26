package model;

import java.awt.Rectangle;
import java.util.Random;

public class EnemyBoss extends Enemy{

	public EnemyBoss(int _x, int _y, String filePath, int nFrames, Mediator m) {
		super(_x, _y, filePath, nFrames, m);
		
		health = 400;
		healthBar.width = (int)((health/100f)*((width/17)*9));
		healthBar.x -= (healthBar.width/3);
		
	}

	@Override
	public void idleLogic() {
		changeAnimation(0);
		if(new Random().nextBoolean()) {
			state = 1;
			changeAnimation(1);
		}else
			state = 3;
	}

	@Override
	public void movingLogic(Rectangle floor) {
		state = 0;
	}

	@Override
	public void shootLogic() {
		bulletMediator.shootBoss(getCentralX(), getCentralY());
		state = 0;
	}

}
