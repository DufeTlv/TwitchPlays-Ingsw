package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class HUD {
	private Rectangle healthBar;
	private Rectangle commandsSection;
	
	public HUD(int x, int y) {
		healthBar = new Rectangle(x+15*3, y+15*3, 60*3, 10*3);
		commandsSection = new Rectangle(x+580, y+45, 200, 100);
	}
	
	public void update(int x, int y, int h) {
		healthBar.setLocation(x+15*3, y+15*3);
		healthBar.width = (int)((h/100f)*(60*3));
		commandsSection.setLocation(x+580, y+45);
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(Color.BLACK);
		g2d.drawRect(healthBar.x-1, healthBar.y-1, 60*3+1, healthBar.height+1);
		g2d.drawRect(commandsSection.x, commandsSection.y, commandsSection.width, commandsSection.height);
		
		g2d.setColor(new Color(15, 210, 30));
		g2d.fillRect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
		
		g2d.drawString("MB: " +((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/ (1024*1024)), healthBar.x , healthBar.y-20);
	}

}
