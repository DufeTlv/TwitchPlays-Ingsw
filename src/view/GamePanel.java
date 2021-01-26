package view;

import java.awt.Color;
import java.awt.Font;
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

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import controller.BulletManager;
import controller.EnemyManager;
import controller.GameSettings;
import controller.MapManager;
import model.HUD;
import model.Mediator;
import model.Player;
import model.RunnableExample;
import model.Object;

public class GamePanel extends JPanel implements ActionListener, Runnable{
	
	private int camX, camY;
	private Timer timer;
	private boolean isRunning;
	private boolean onPause;
	
	private long fpsTimer;
	private final int fpsDELAY = 17;
	private Thread gameThread = null;
	private Thread exampleThread;
	
	private HUD hud;
	private Player player;
	private Object cursor;
	private BulletManager bulletManager;
	private MapManager mapManager;
	private EnemyManager enemyManager;
	
	private Mediator enemyToBulletMediator;
	
	private CardLayoutGameController controller;
	
	public GamePanel(CardLayoutGameController c) {
		Initialize();
		
		controller = c;
	}
	
	/*public GamePanel(String token, String nomeCanale, String) {
		Initialize();
		
		// inizializzazione bot 
		
	}*/

	public void Initialize() {
		addKeyListener(new KeyInputManager());
		addMouseListener(new MouseInputManager());
		
		setFocusable(true);
		setOpaque(true);
		setBackground(new Color(97, 129, 143));
		
		setFont(new Font("TimesRoman", Font.BOLD, 20));
		
		// variabili/oggetti di gioco
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
		onPause = false;
		
		//timer = new Timer(fpsDELAY, this);
		//timer.start();
		
		gameThread = new Thread(this);
		gameThread.start();
		
		//exampleThread = new Thread(new RunnableExample());
		//exampleThread.start();
		
		// aggiungere timer per i Record
		
		
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
        
        if(onPause) {
        	g.setColor(new Color(0,0,0,150));
        	g.fillRect(camX, camY, GameSettings.getInstance().getWRes(), GameSettings.getInstance().getHRes());
        	
        	g.setColor(Color.WHITE);
        	g.drawString("Press P to Resume", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Press P to Resume")/2, camY+GameSettings.getInstance().getHRes()/3);
        	g.drawString("Press ESC to Menu", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Press ESC to Menu")/2, camY+GameSettings.getInstance().getHRes()/2);
        }
        
        
        Toolkit.getDefaultToolkit().sync();
        
	}

	@Override
	public void run() {
		
		while(isRunning) {
			if(System.currentTimeMillis() > fpsTimer + fpsDELAY) {
				fpsTimer = System.currentTimeMillis();
				
				if(!onPause) {
					
					
					mapManager.getCurrentRoom().update();
					player.update();
					bulletManager.update(mapManager.getCurrentRoomFloor(), enemyManager.getEnemies(), player);
					enemyManager.update(mapManager.getCurrentRoomFloor());
					
					
					
					if(enemyManager.roomClear()) {						
						mapManager.setCurrentRoomIndex(player.getFeet());
						player.setCurrentRoom(mapManager.getCurrentRoom());
						
						
						if(!mapManager.getCurrentRoom().isVisited()) {
							if(!mapManager.differentRoom())
								mapManager.getCurrentRoom().setVisited(true);
							else
								enemyManager.addEnemies(mapManager.getCurrentRoomFloor(), 3, enemyToBulletMediator);
						}
						
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
				}else {
					
					
					
				}
				
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
			
			if(e.getKeyCode() == KeyEvent.VK_P)
				onPause = !onPause;
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && onPause)
				controller.showMenu();
				
		}

		@Override
		public void keyReleased(KeyEvent e) {
			player.processReleaseEvent(e);
		}
		
	}

	/*			  Class MouseInputManager			*/
	private class MouseInputManager extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)) {
				
				bulletManager.addPlayerBullets(player.getCentralX(), player.getCentralY(), cursor.getCentralX(), cursor.getCentralY());
				
			}
			
		}
		
	}

}
