package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Enemy extends AnimatedObject{
	
	private boolean right, left, up, down;
	private Rectangle feet;

	public Enemy(int _x, int _y, String filePath, int nFrames) {
		super(_x, _y, filePath, nFrames);
		
		addAnimation(0, 3, 200, true);  	// idle
		addAnimation(4, 11, 100, true); 	// walk
		//addAnimation(12, 15, 100, false);	// attack
		
		feet = new Rectangle(_x+((width/17)*4), _y+((height/20)*18), ((width/17)*9), ((height/20)*3));
		
		right = left = up = down = false;
		
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawRect(feet.x, feet.y, feet.width, feet.height);
		
		super.draw(g2d);		
		
		//g2d.drawRect(x, y, width, height);
	}
	
	public void update() {
		animate();
		
		
		/* anziche creare tre tipologie diverse di nemico posso creare 3 logiche diverse
		 * e usare una variabile per decidere quale logica/comportamento usare */
	}

}
