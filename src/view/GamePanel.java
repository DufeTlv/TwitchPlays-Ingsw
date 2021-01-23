package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

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
import model.HUD;
import model.Mediator;
import model.Player;
import model.Object;

public class GamePanel extends JLayeredPane implements ActionListener, Runnable{
	
	private int camX, camY;
	private Timer timer;
	private boolean isRunning;
	
	private long fpsTimer;
	private final int fpsDELAY = 17;
	private Thread gameThread = null;
	
	
	private HUD hud;
	private Player player;
	private Object cursor;
	private BulletManager bulletManager;
	private MapManager mapManager;
	private EnemyManager enemyManager;
	
	private Mediator enemyToBulletMediator;
	
	public GamePanel() {
		addKeyListener(new KeyInputManager());
		addMouseListener(new MouseInputManager());
		
		setFocusable(true);
		setOpaque(true);
		setBackground(new Color(97, 129, 143));
		
		//di seguito inserisco tutti le variabili/oggetti di gioco
		cursor = new Object(0,0, "gameAssets/sprites/cursor.png");
		
		mapManager = new MapManager();
		
		player = new Player(100, 100, "gameAssets/sprites/player.png", 12);
		
		player.setCurrentRoom(mapManager.getCurrentRoom());
		enemyManager = new EnemyManager();
		bulletManager = new BulletManager();
		
		enemyToBulletMediator = new Mediator(bulletManager, player);
		
		// imposta come centro della camera il punto medio tra giocatore e cursore/mirino
		camX = (player.getCentralX()+cursor.getCentralX())/2 - GameSettings.getInstance().getWRes()/2;
		camY = (player.getCentralY()+cursor.getCentralY())/2 - GameSettings.getInstance().getHRes()/2;
		
		hud = new HUD(camX, camY);
		
		isRunning = true;
		
		timer = new Timer(fpsDELAY, this);
		timer.start();
		
		gameThread = new Thread(this);
		gameThread.start();
		
		fpsTimer = System.currentTimeMillis();
		
		/* testing */
		enemyManager.addEnemies(mapManager.getCurrentRoomFloor(), 2, enemyToBulletMediator);
		mapManager.changeState(new Random().nextInt(2)+2);
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.translate(-camX, -camY);
        
        mapManager.drawMap((Graphics2D)g);
        
        player.draw((Graphics2D)g);
        enemyManager.draw((Graphics2D)g);
        bulletManager.draw((Graphics2D)g);
        
        cursor.draw((Graphics2D)g);
        
        hud.draw((Graphics2D)g);
        
        Toolkit.getDefaultToolkit().sync();        
	}

	@Override
	public void run() {
		
		while(isRunning) {
			if(System.currentTimeMillis() > fpsTimer + fpsDELAY) {
				fpsTimer = System.currentTimeMillis();
				
				player.update();
				
				mapManager.getCurrentRoom().update();
				bulletManager.update(mapManager.getCurrentRoomFloor(), enemyManager.getEnemies(), player);
				enemyManager.update(mapManager.getCurrentRoomFloor());
				
				//mapManager.getCurrentRoom().setVisited(enemyManager.roomClear());
				if(enemyManager.roomClear()) {
					mapManager.setCurrentRoomIndex(player.getFeet());
					player.setCurrentRoom(mapManager.getCurrentRoom());
				}
				
				/* aggiornamento della posizione del cursore */
				Point p = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(p, this);
				cursor.setLocation( camX+(p.x-(cursor.getBounds().width/2)), camY+(p.y-(cursor.getBounds().height/2)));
				
				/* aggiornamento posizione camera/inquadratura */
				//camX = (player.getCentralX()+cursor.getCentralX())/2 - GameSettings.getInstance().getWRes()/2;
				//camY = (player.getCentralY()+cursor.getCentralY())/2 - GameSettings.getInstance().getHRes()/2;
				camX = (player.getCentralX() - GameSettings.getInstance().getWRes()/2 );
				camY = (player.getCentralY() - GameSettings.getInstance().getHRes()/2);
				
				hud.update(camX, camY, player.getHealth());
				
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
				
				bulletManager.addPlayerBullets(player.getCentralX(), player.getCentralY(), cursor.getCentralX(), cursor.getCentralY());
				
			}
			
		}		
	}

}
