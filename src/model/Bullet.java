package model;

import java.awt.geom.Point2D;

public class Bullet extends Object{
	
	private double directions[];
	private double position[];
	private int velocity;
	private int damage;

	public Bullet(int _x, int _y, int x1, int y2, int v, int d, String filePath) {
		super(_x, _y, filePath);
		
		position = new double[2];
		position[0] = _x;
		position[1] = _y;
		
		directions = new double[2];
		directions[0] = x1 -_x;
		directions[1] = y2 -_y;
		
		double length = Math.sqrt((directions[0]*directions[0]) + (directions[1]*directions[1]));
		directions[0] /= length;
		directions[1] /= length;
		
		velocity = v;
		damage = d;
		
	}
	
	public int getDamage() {
		return damage;
	}
	
	public void update() {
		
		position[0] += directions[0]*velocity;
		position[1] += directions[1]*velocity;
		
		setLocation((int)position[0]-(width/2), (int)position[1]-(height/2));
	}

}
