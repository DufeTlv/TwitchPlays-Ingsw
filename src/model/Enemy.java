package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Random;

public abstract class Enemy extends AnimatedObject{
	
	protected Rectangle healthBar;
	protected int state;
	protected int health;
	
	protected double directions[];
	protected double position[];
	protected double length;
	protected double velocity;
	
	protected Mediator bulletMediator; 

	public Enemy(int _x, int _y, String filePath, int nFrames, Mediator m) {
		super(_x, _y, filePath, nFrames);
		
		addAnimation(0, 3, 200, true);  	// idle
		addAnimation(4, 11, 100, true); 	// walk
		addAnimation(12, 15, 100, false);	// death
		
		health = 100;
		healthBar = new Rectangle(_x+((width/17)*4), _y-((height/20)*4), ((width/17)*9), ((height/20)*3));
		
		state = 0;
		
		position = new double[2];
		position[0] = getCentralX();
		position[1] = getCentralY();
		
		directions = new double[2];		
		directions[0] = -1;
		directions[1] = -1; 
		
		velocity = 1.50;
		
		// farà da mediatore tra l'enemy e il bulletManager
		// permettendo all'enemy di comunicargli di rilasciare un proiettile 
		bulletMediator = m;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.setColor(new Color(15, 190,100));
		g2d.fillRect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
		
		super.draw(g2d);		
	}
	
	public void damage(int d) {
		health -= d;
		healthBar.width = (int)((health/100f)*((width/17)*9));
		
		//if( health > 0)
			//state = 2; // hurt
		//else
		if( health < 1)
			state = 4; // death
	}
	
	public boolean isDead() {
		return state == 4 && isEnded();
	}
	
	// il metodo update diventa un metodo Template
	public void update(Rectangle floor) {
		animate();
		
		/* suddivisione della logica del nemico a stati */
		
		switch(state) {
			case 0: // idle				
				idleLogic();
				break;
				
			case 1: // moving
				movingLogic(floor);
				break;
				
			case 2: // hurt
				// rimuovere
				break;
				
			case 3: // shoot
				shootLogic();
				break;
				
			case 4: // death
				changeAnimation(2);
				break;
				
		}
		
		
	}
	
	// metodi primitivi del metodo Template
	public abstract void idleLogic();
	public abstract void movingLogic(Rectangle floor);
	public abstract void shootLogic();

}
