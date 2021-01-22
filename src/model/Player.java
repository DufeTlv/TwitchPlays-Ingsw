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
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bR = currentRoom.getBridge(1); //right
		Rectangle bD = currentRoom.getBridge(2); //down
		
		int collisionSide; 
		
		if		( bU != null && (feet.y > (bU.y-feet.height) && (feet.y+feet.height) < (bU.y+bU.height+feet.height) ) ) collisionSide = (bU.x+bU.width);
		else if ( bR != null && (feet.y > (bR.y) 			 && (feet.y+feet.height) < (bR.y+bR.height) ) ) 			return false;
		else if ( bD != null && (feet.y > (bD.y-feet.height) && (feet.y+feet.height) < (bD.y+bD.height+feet.height) ) ) collisionSide = (bD.x+bD.width);
		else collisionSide = currentRoom.getRightSide();
		
		return ( (feet.x+feet.width)+speedX > collisionSide );
		
	}
	
	public boolean leftCollition() {
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bD = currentRoom.getBridge(2); //down
		
		int collisionSide; 
		
		if		( bU != null && (feet.y > (bU.y-feet.height) && (feet.y+feet.height) < (bU.y+bU.height+feet.height) ) ) collisionSide = (bU.x);
		else if ( bL != null && (feet.y > (bL.y) 			 && (feet.y+feet.height) < (bL.y+bL.height) ) ) 			return false;
		else if ( bD != null && (feet.y > (bD.y-feet.height) && (feet.y+feet.height) < (bD.y+bD.height+feet.height) ) ) collisionSide = (bD.x);
		else collisionSide = currentRoom.getLeftSide();
		
		return ( feet.x-speedX < collisionSide );
		
	}
	
	public boolean upCollition() {
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bR = currentRoom.getBridge(1); //right
		
		int collisionSide; 
		
		if		( bL != null && (feet.x > (bL.x-feet.width) && (feet.x+feet.width) < (bL.x+bL.width+feet.width) ) ) collisionSide = (bL.y);
		else if ( bU != null && (feet.x > (bU.x) 			&& (feet.x+feet.width) < (bU.x+bU.width) ) ) 			return false;
		else if ( bR != null && (feet.x > (bR.x-feet.width) && (feet.x+feet.width) < (bR.x+bR.width+feet.width) ) ) collisionSide = (bR.y);
		else collisionSide = currentRoom.getUpperSide();
		
		return ( (feet.y)-speedY < collisionSide );
		
	}
	
	public boolean downCollition() {
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bD = currentRoom.getBridge(2); //down
		Rectangle bR = currentRoom.getBridge(1); //right
		
		int collisionSide; 
		
		if		( bL != null && (feet.x > (bL.x-feet.width) && (feet.x+feet.width) < (bL.x+bL.width+feet.width) ) ) collisionSide = (bL.y+bL.height);
		else if ( bD != null && (feet.x > (bD.x) 			&& (feet.x+feet.width) < (bD.x+bD.width) ) ) 			return false;
		else if ( bR != null && (feet.x > (bR.x-feet.width) && (feet.x+feet.width) < (bR.x+bR.width+feet.width) ) ) collisionSide = (bR.y+bR.height);
		else collisionSide = currentRoom.getBottomSide();
		
		return ( (feet.y+feet.height)+speedY > collisionSide );
		
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
