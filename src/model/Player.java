package model;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

public class Player extends AnimatedObject{
	
	private double speed[];
	private double position[];
	private boolean right, left, up, down;
	private Rectangle feet;
	private Room currentRoom;
	private AnimatedObject currentTile;
	
	private int health, ammo;

	public Player(int _x, int _y, String filePath, int nFrames) {
		super(_x, _y, filePath, nFrames);
		
		addAnimation(0, 3, 200, true);  // idle
		addAnimation(4, 11, 100, true); // walk
		
		feet = new Rectangle(_x+((width/17)*4), _y+((height/20)*18), ((width/17)*9), ((height/20)*3));
		
		right = left = up = down = false;
		
		position = new double[2];
		position[0] = _x;
		position[1] = _y;
		
		speed = new double[2];
		speed[0] = 2*2;
		speed[1] = 2*2;
		
		health = 100;
		
		/*testing bruttissimo*/
		currentTile = null;
		
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
	
	public Rectangle getFeet() {
		return feet;
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		g2d.drawRect(feet.x, feet.y, feet.width, feet.height);
		
		super.draw(g2d);		
		
		g2d.drawRect(x, y, width, height);
	}
	
	/*public boolean isOutsideRoom() {
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bR = currentRoom.getBridge(1); //right
		Rectangle bD = currentRoom.getBridge(2); //down
		Rectangle bL = currentRoom.getBridge(3); //left
		
		return (!currentRoom.getFloor().contains(feet)
					&& ( bU == null || (bU != null && !bU.contains(feet)))
					&& ( bR == null || (bR != null && !bR.contains(feet)))
					&& ( bD == null || (bD != null && !bD.contains(feet)))
					&& ( bL == null || (bL != null && !bL.contains(feet)))
				);
	}*/
	
	public void update() {
		
		animate();
		
		if(right && !rightCollision()) {position[0] += speed[0];	x = (int)position[0]; changeAnimation(1);}
		if(left  && !leftCollision())  {position[0] -= speed[0];	x = (int)position[0]; changeAnimation(1);}
		if(up	 && !upCollision())	   {position[1] -= speed[1];	y = (int)position[1]; changeAnimation(1);}
		if(down	 && !downCollision())  {position[1] += speed[1];	y = (int)position[1]; changeAnimation(1);}
		
		if(!right && !left && !up && !down) changeAnimation(0);
		
		feet.x = x+((width/17)*4);
		feet.y = y+((height/20)*18);
		
		if(currentTile == null || !currentTile.contains((int)feet.getCenterX(), (int)feet.getCenterY())) {
			currentTile = currentRoom.getCurrentTile((int)feet.getCenterX(), (int)feet.getCenterY());
			
			if(currentTile.currentAnimation < 2)
				speed[0] = speed[1] = 2*2;
			else if(currentTile.currentAnimation == 2 )
				speed[0] = speed[1] = 0.5*2;
			else if(currentTile.currentAnimation == 3 )
				speed[0] = speed[1] = 4*2;
		}
	}
	
	public boolean rightCollision() {
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bR = currentRoom.getBridge(1); //right
		Rectangle bD = currentRoom.getBridge(2); //down
		
		int collisionSide; 
		
		if(currentRoom.isVisited()) {
			if		( bU != null && (feet.y >= (bU.y-feet.height) && (feet.y+feet.height) <= (bU.y+bU.height+feet.height) ) ) collisionSide = (bU.x+bU.width);
			else if ( bR != null && (feet.y >= (bR.y) 			  && (feet.y+feet.height) <= (bR.y+bR.height) ) ) 			return false;
			else if ( bD != null && (feet.y >= (bD.y-feet.height) && (feet.y+feet.height) <= (bD.y+bD.height+feet.height) ) ) collisionSide = (bD.x+bD.width);
			else collisionSide = currentRoom.getRightSide();
		}else collisionSide = currentRoom.getRightSide();
		
		return ( (feet.x+feet.width)+speed[0] >= collisionSide );
		
	}
	
	public boolean leftCollision() {
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bD = currentRoom.getBridge(2); //down
		
		int collisionSide; 
		
		if(currentRoom.isVisited()) {
			if		( bU != null && (feet.y >= (bU.y-feet.height) && (feet.y+feet.height) <= (bU.y+bU.height+feet.height) ) ) collisionSide = (bU.x);
			else if ( bL != null && (feet.y >= (bL.y) 			  && (feet.y+feet.height) <= (bL.y+bL.height) ) ) 			return false;
			else if ( bD != null && (feet.y >= (bD.y-feet.height) && (feet.y+feet.height) <= (bD.y+bD.height+feet.height) ) ) collisionSide = (bD.x);
			else collisionSide = currentRoom.getLeftSide();
		}else collisionSide = currentRoom.getLeftSide();
		
		return ( feet.x-speed[0] <= collisionSide );
		
	}
	
	public boolean upCollision() {
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bU = currentRoom.getBridge(0); //up
		Rectangle bR = currentRoom.getBridge(1); //right
		
		int collisionSide; 
		
		if(currentRoom.isVisited()) {
			if		( bL != null && (feet.x >= (bL.x-feet.width) && (feet.x+feet.width) <= (bL.x+bL.width+feet.width) ) ) collisionSide = (bL.y);
			else if ( bU != null && (feet.x >= (bU.x) 			 && (feet.x+feet.width) <= (bU.x+bU.width) ) ) 			return false;
			else if ( bR != null && (feet.x >= (bR.x-feet.width) && (feet.x+feet.width) <= (bR.x+bR.width+feet.width) ) ) collisionSide = (bR.y);
			else collisionSide = currentRoom.getUpperSide();
		}else collisionSide = currentRoom.getUpperSide();
		
		return ( (feet.y)-speed[1] <= collisionSide );
		
	}
	
	public boolean downCollision() {
		Rectangle bL = currentRoom.getBridge(3); //left
		Rectangle bD = currentRoom.getBridge(2); //down
		Rectangle bR = currentRoom.getBridge(1); //right
		
		int collisionSide; 
		
		if(currentRoom.isVisited()) {
			if		( bL != null && (feet.x >= (bL.x-feet.width) && (feet.x+feet.width) <= (bL.x+bL.width+feet.width) ) ) collisionSide = (bL.y+bL.height);
			else if ( bD != null && (feet.x >= (bD.x) 			 && (feet.x+feet.width) <= (bD.x+bD.width) ) ) 			return false;
			else if ( bR != null && (feet.x >= (bR.x-feet.width) && (feet.x+feet.width) <= (bR.x+bR.width+feet.width) ) ) collisionSide = (bR.y+bR.height);
			else collisionSide = currentRoom.getBottomSide();
		}else collisionSide = currentRoom.getBottomSide();
		
		return ( (feet.y+feet.height)+speed[1] >= collisionSide );
		
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
