package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends AnimatedObject{
	
	private int speedX, speedY;
	private boolean right, left, up, down;
	private Rectangle feet;
	private Room currentRoom;
	
	private int health, ammo;

	public Player(int _x, int _y, String filePath, int nFrames) {
		super(_x, _y, filePath, nFrames);
		
		addAnimation(0, 3, 200, true);  // idle
		addAnimation(4, 11, 100, true); // walk
		
		
		feet = new Rectangle(_x+((width/17)*4), _y+((height/20)*18), ((width/17)*9), ((height/20)*3));
		
		right = left = up = down = false;
		
		speedX = 2*2;
		speedY = 1*2;
		
		health = 100;
		
	}
	
	public int getHealth() {
		return health;
	}
	
	public void damage(int d) {
		health -= d;
	}
	
	public void setCurrentRoom(Room r) {
		currentRoom = r;
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
		
		feet.x = x+((width/17)*4);
		feet.y = y+((height/20)*18);
	}
	
	public boolean rightCollision() {
		//Rectangle bU = currentRoom.getBridge(0);
		//Rectangle bR = currentRoom.getBridge(1);
		//Rectangle bD = currentRoom.getBridge(2);
		
		//int collisionSide = ()? : ; 
		
		return ( (feet.x+feet.width)+speedX > currentRoom.getRightSide() );
		
	}
	
	public boolean leftCollition() {
		Rectangle b = currentRoom.getBridge(3);
		
		if(feet.x - speedX < currentRoom.getLeftSide()&& 
				(b == null || !(b != null && (feet.y >= b.y) && (feet.y+feet.height <= b.y+b.height))) )
			return true;
		return false;
	}
	
	public boolean upCollition() {
		if( feet.y - speedY < currentRoom.getUpperSide())
			return true;
		return false;
	}
	
	public boolean downCollition() {
		if( feet.y+feet.height + speedY > currentRoom.getBottomSide() )
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
