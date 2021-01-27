package model;

import java.awt.Rectangle;
import java.util.Random;

public class EnemyBoss extends Enemy{

	protected int direction, length;
	
	public EnemyBoss(int _x, int _y, String filePath, int nFrames, Mediator m) {
		super(_x, _y, filePath, nFrames, m);
		
		health = 400;
		healthBar.width = (int)((health/100f)*((width/17)*9));
		healthBar.x = x - (healthBar.width/2) + (width/2);
		
		direction = 0;
		
		length = 10*3;
		
	}
	
	private void setDirection() {
		
		direction = (new Random().nextInt(2)-1)*2;

		mirrored = direction < 0;
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
		setDirection();
		
		if(length > 0) {
			if((getCenterX() + direction) > floor.x+30*3 && (getCenterX() + direction) < (floor.x+floor.width-30*3)) {
				x += direction;
				healthBar.x = x - (healthBar.width/2) + (width/2);
			}
		}
		
		length -= 2;
		
		if(length <= 0) {
			length = 10*3;
			state = 0;
			System.out.println(length);
		}
		
		
	}

	@Override
	public void shootLogic() {
		bulletMediator.shootBoss(getCentralX(), getCentralY());
		state = 0;
	}

}
