package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class HUD {
	private Object healthBarDecoration;
	private Rectangle healthBar;
	
	private Rectangle commandsSection;
	private Color healthBarColor, sectionColor;
	
	public HUD(int x, int y) {
		healthBarDecoration = new Object(x+15*3, y+15*3, "gameAssets/sprites/healthBar.png");
		healthBar = new Rectangle(x+17*3, y+17*3, 60*3, 10*3);
		commandsSection = new Rectangle(x+580, y+45, 200, 100);
		healthBarColor = new Color(78, 12, 19).brighter().brighter().brighter();
		sectionColor   = new Color(0, 0, 0, 190);
	}
	
	public void update(int x, int y, int h) {
		healthBarDecoration.setLocation(x+15*3, y+15*3);
		healthBar.setLocation(x+17*3, y+17*3);
		healthBar.width = (int)((h/100f)*(60*3));
		
		commandsSection.setLocation(x+580, y+45);
	}
	
	public void draw(Graphics2D g2d) {
		g2d.setColor(sectionColor);
		g2d.fillRoundRect(commandsSection.x, commandsSection.y, commandsSection.width, commandsSection.height, 10, 10);
		
		g2d.setColor(Color.BLACK);
		healthBarDecoration.draw(g2d);
		g2d.setColor(healthBarColor);
		g2d.fillRect(healthBar.x, healthBar.y, healthBar.width, healthBar.height);
		
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 15));
		g2d.setColor(Color.WHITE);
		g2d.drawString("Comandi della Chat:", commandsSection.x + 10, commandsSection.y + 20);
		g2d.drawString("#addenemies"		, commandsSection.x + 10, commandsSection.y + 50);
		g2d.drawString("#water" 			, commandsSection.x + 10, commandsSection.y + 70);
		g2d.drawString("#ice"	    		, commandsSection.x + 10, commandsSection.y + 90);
		
		g2d.drawString("MB: " +((int)(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())/ (1024*1024)), healthBar.x , healthBar.y-20);
	}

}
