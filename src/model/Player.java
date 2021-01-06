package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends AnimatedObject{
	
	private int speedX, speedY;
	private int pixelW, pixelH;
	private boolean right, left, up, down;
	private Rectangle feet, currentFloor;
	
	private int health, ammo;

	public Player(int _x, int _y, String filePath, int nFrames) {
		super(_x, _y, filePath, nFrames);
		
		addAnimation(0, 3, 200, true);  // idle
		addAnimation(4, 11, 100, true); // walk
		
		pixelW = (width/17);
		pixelH = (height/20);
		
		feet = new Rectangle(_x+((width/17)*4), _y+((height/20)*18), ((width/17)*9), ((height/20)*3));
		
		right = left = up = down = false;
		
		speedX = 2*2;
		speedY = 1*2;
		
	}
	
	public void setCurrentFloor(Rectangle floor) {
		currentFloor = floor;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawRect(feet.x, feet.y, feet.width, feet.height);
		
		super.draw(g2d);		
		
		g2d.drawRect(x, y, width, height);
	}
	
	public void update() {
		
		animate();
		
		if(right && !rightCollision()) {x += speedX; changeAnimation(1);}
		if(left  && !leftCollition())  {x -= speedX; changeAnimation(1);}
		if(up	 && !upCollition())	   {y -= speedY; changeAnimation(1);}
		if(down	 && !downCollition())  {y += speedY; changeAnimation(1);}
		
		if(!right && !left && !up && !down) changeAnimation(0);
		
		feet.x = x+(pixelW*4);
		feet.y = y+(pixelH*18);
	}
	
	public boolean rightCollision() {
		if( (feet.x+feet.width)+speedX > currentFloor.x+currentFloor.width)
			return true;
		return false;
	}
	
	public boolean leftCollition() {
		if(feet.x - speedX < currentFloor.x)
			return true;
		return false;
	}
	
	public boolean upCollition() {
		if( feet.y - speedY < currentFloor.y)
			return true;
		return false;
	}
	
	public boolean downCollition() {
		if( feet.y+feet.height + speedY > currentFloor.y + currentFloor.height)
			return true;
		return false;
	}
	
	public void processPressEvent(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			mirrored = false;
			right = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			mirrored = true;
			left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			up = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			down = true;
		}
		
	}
	
	public void processReleaseEvent(KeyEvent e) {
		
		if(e.getKeyCode() == KeyEvent.VK_D) {
			right = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_W) {
			up = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			down = false;
		}
		
	}
	

	
}
