package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class HUD {
	private int x, y;
	private Rectangle healthBar;
	private Rectangle commandsSection;
	
	public HUD(int x, int y) {
		healthBar = new Rectangle(x+15*3, y+15*3, 60*3, 10*3);
	}
	
	public void update(int x, int y, int h) {
		this.x = x;
		this.y = y;
		healthBar.setLocation(x+15*3, y+15*3);
		healthBar.width = (int)((h/100f)*(60*3));
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawRect(healthBar.x-1, healthBar.y-1, 60*3+1, healthBar.height+1);
		g2d.setColor(new Color(15, 210,100));
		g2d.fillRect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
		
		g2d.drawString("MB: " +((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/ (1024*1024)), x+10 , y+10);
	}

}
