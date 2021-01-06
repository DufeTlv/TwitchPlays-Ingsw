package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.sun.jdi.event.EventQueue;

import controller.BulletManager;
import controller.EnemyManager;
import controller.GameSettings;
import controller.MapManager;
import model.Bullet;
import model.Player;
import model.Object;

public class GamePanel extends JLayeredPane implements ActionListener, Runnable{
	
	private int camX, camY;
	private Timer timer;
	private boolean isRunning;
	
	private long fpsTimer;
	private final int fpsDELAY = 17;
	private Thread gameThread = null;
	
	Player player;
	Object cursor;
	BulletManager bulletManager;
	MapManager mapManager;
	EnemyManager enemyManager;
	
	public GamePanel() {
		addKeyListener(new KeyInputManager());
		addMouseListener(new MouseInputManager());
		
		setFocusable(true);
		setOpaque(true);
		setBackground(new Color(97, 129, 143));
		
		/*
		 * qui inserisco tutti le variabili/oggetti di gioco
		 */
		
		cursor = new Object(0,0, "gameAssets/sprites/cursor.png");
		
		mapManager = new MapManager();
		player = new Player(40, 40, "gameAssets/sprites/player.png", 12);
		player.setCurrentFloor(mapManager.getCurrentRoomFloor());
		enemyManager = new EnemyManager();
		bulletManager = new BulletManager();
		
		// imposta come centro della camera il punto medio tra giocatore e cursore/mirino
		camX = (player.getCenterX()+cursor.getCenterX())/2 - GameSettings.getInstance().getWRes()/2;
		camY = (player.getCenterY()+cursor.getCenterY())/2 - GameSettings.getInstance().getHRes()/2;
		
		isRunning = true;
		
		timer = new Timer(fpsDELAY, this);
		timer.start();
		
		gameThread = new Thread(this);
		gameThread.start();
		
		fpsTimer = System.currentTimeMillis();
		
		/* testing */
		enemyManager.addEnemies(mapManager.getCurrentRoomFloor(), 5);
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.translate(-camX, -camY);
        
        mapManager.drawMap((Graphics2D)g);
        
        player.draw((Graphics2D)g);
        bulletManager.draw((Graphics2D)g);
		enemyManager.draw((Graphics2D)g);
       
        cursor.draw((Graphics2D)g);
        
        Toolkit.getDefaultToolkit().sync();        
	}

	@Override
	public void run() {
		
		while(isRunning) {
			if(System.currentTimeMillis() > fpsTimer + fpsDELAY) {
				fpsTimer = System.currentTimeMillis();
				
				player.update();
				
				bulletManager.update();
				enemyManager.update();
				//mapManager.update();
				
				/* aggiornamento della posizione del cursore */
				Point p = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(p, this);
				cursor.setPosition( camX+(p.x-(cursor.getBounds().width/2)), camY+(p.y-(cursor.getBounds().height/2)));
				
				/* aggiornamento posizione camera/inquadratura */
				camX = (player.getCenterX()+cursor.getCenterX())/2 - GameSettings.getInstance().getWRes()/2;
				camY = (player.getCenterY()+cursor.getCenterY())/2 - GameSettings.getInstance().getHRes()/2;
				
				repaint();
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	/*			  Class KeyInputManager			*/
	private class KeyInputManager extends KeyAdapter{

		@Override
		public void keyPressed(KeyEvent e) {
			player.processPressEvent(e);
			if(e.getKeyCode() == KeyEvent.VK_V)
				mapManager.changeState(1);
			if(e.getKeyCode() == KeyEvent.VK_B)
				mapManager.changeState(2);
			if(e.getKeyCode() == KeyEvent.VK_N)
				mapManager.changeState(3);
		}

		@Override
		public void keyReleased(KeyEvent e) {
			player.processReleaseEvent(e);
		}
		
	}

	/*			  Class MouseInputManager			*/
	private class MouseInputManager extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)) {
				
				bulletManager.addPlayerBullets(player.getCenterX(), player.getCenterY(), cursor.getCenterX(), cursor.getCenterY());
				
			}
			
		}		
	}

}
