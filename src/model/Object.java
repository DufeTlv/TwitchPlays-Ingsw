package model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.ImageIcon;

public class Object {
	
	protected int x, y, width, height;
	protected boolean visible;
	protected Image image;
	
	public Object(int _x, int _y, String filePath) {
		x = _x;
		y = _y;
		visible = true;
		loadImage(filePath);
	}
	
	public void loadImage(String path) {
		
		try {
			image = ImageCollection.getInstance().getImage(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		width = image.getWidth(null);
		height = image.getHeight(null);
		
	}
	
	public void draw(Graphics2D g) {
		if(visible && image != null)
			g.drawImage(image, x, y, null);
	}
	
	public void setPosition(int _x, int _y) {
		x = _x;
		y = _y;
	}
	
	public Rectangle getBounds() {
		return new Rectangle(x, y, width, height);
	}
	
	public int getCenterX() {
		return x+(width/2);
	}
	
	public int getCenterY() {
		return y+(height/2);
	}
	
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	
}
