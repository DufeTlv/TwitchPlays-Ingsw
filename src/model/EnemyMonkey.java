package model;

import java.awt.Rectangle;
import java.util.Random;

public class EnemyMonkey extends Enemy{
	
	public EnemyMonkey(int _x, int _y, String filePath, int nFrames, Mediator m) {
		super(_x, _y, filePath, nFrames, m);
		
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
		setDirection(floor);
		move();
		
	}

	@Override
	public void shootLogic() {
		bulletMediator.shoot(getCentralX(), getCentralY());
		state = 0;
		
	}
	
	private void setDirection(Rectangle floor) {
		if(directions[0] != -1)
			return;
		
		Random r = new Random();
		
		int xRange = ( r.nextBoolean() && (getCentralX()-(20*3) > floor.x+10*3)? -20: (getCentralX()+(20*3) < floor.x+floor.width-20*3 )? 10 : 0)*3;
		int yRange = ( r.nextBoolean() && (getCentralY()-(20*3) > floor.y+10*3)? -20: (getCentralY()+(20*3) < floor.y+floor.height-20*3)? 10 : 0)*3;
		
		// genero una posizione verso cui dirigere l'enemy
		directions[0] = ( (r.nextInt(10)*3) + (getCentralX() + xRange*3) ) - getCentralX();
		directions[1] = ( (r.nextInt(10)*3) + (getCentralY() + yRange*3) ) - getCentralY();
		
		length = Math.sqrt((directions[0]*directions[0]) + (directions[1]*directions[1]));
		
		directions[0] /= length;
		directions[1] /= length;
		
		// la sprite viene specchiata in base alla direzione
		// del movimento sull'asse delle ascisse (y=0)
		mirrored = directions[0] < 0;
		
	}
	
	private void move() {
		// se la distanza(lunghezza del vettore) raggiunge lo zero
		// lo stato torna 0 e il vettore della direzione viene "resettato" a -1
		if(length <= 0) {
			state = 3;
			directions[0] = directions[1] = -1;
			
			return;
		}
		
		// viene aggiornata la posizione in double
		// che fa riferimento al centro dell'enemy
		position[0] += directions[0]*velocity;
		position[1] += directions[1]*velocity;
		
		x = (int)position[0] - (width/2);
		y = (int)position[1] - (height/2);
		
		healthBar.setLocation(x+((width/17)*4), y-((height/20)*4));
		
		// la lunghezza viene decrementata in riferimento
		// alla velocità di spostamento
		length -= velocity;
		
	}

}
