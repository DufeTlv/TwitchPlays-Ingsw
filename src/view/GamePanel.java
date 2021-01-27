package view;

import java.awt.Color;
import java.awt.Font;
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
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import controller.BulletManager;
import controller.EnemyManager;
import controller.GameSettings;
import controller.GameSound;
import controller.MapManager;
import javaTwirk.TwitchIRC;
import model.Chronometer;
import model.HUD;
import model.Mediator;
import model.Player;
import model.Object;

public class GamePanel extends JPanel implements ActionListener, Runnable{
	
	private int 	camX, camY;
	private boolean isRunning;
	private int 	gameState;
	
	private long 		fpsTimer;
	private final int 	fpsDELAY = 17;
	private Thread 		gameThread = null;
	private Thread 		twitchThread = null;
	
	private TwitchIRC 	  bot = null;
	
	private HUD 		  hud;
	private Player 		  player;
	private Object 		  cursor;
	private BulletManager bulletManager;
	private MapManager 	  mapManager;
	private EnemyManager  enemyManager;
	
	private Mediator enemyToBulletMediator;
	
	private CardLayoutGameController controller;
	private GameSound soundtrack;
	
	private Chronometer chronometer;
	
	/* Costruttore Partita Classica */
	public GamePanel(CardLayoutGameController c) {
		Initialize();
		
		controller = c;
		
	}
	
	/* Costruttore Partita Online */
	public GamePanel(CardLayoutGameController c, String name, String authToken, String channel) {
		Initialize();
		
		controller = c;
		
		// inizializzazione e avvio del bot 
		//System.out.println( name + " " + authToken + " #" + channel);
		
		bot = new TwitchIRC(authToken, channel, name);
		twitchThread = new Thread(bot);
		twitchThread.start();
		
		
	}

	public void Initialize() {
		addKeyListener	(new KeyInputManager());
		addMouseListener(new MouseInputManager());
		setFocusable	(true);
		setOpaque		(true);
		setBackground	(new Color(97, 120, 143).brighter());
		setFont			(new Font("TimesRoman", Font.BOLD, 20));
		
		/* variabili/oggetti di gioco */
		gameState = 0;
		
		cursor = new Object(0,0, "gameAssets/sprites/cursor.png");
		
		mapManager 		= new MapManager();
		player 			= new Player(
								(int)mapManager.getCurrentRoomFloor().getCenterX(), 
								(int)mapManager.getCurrentRoomFloor().getCenterY(), 
								"gameAssets/sprites/player.png", 
								12
							);
		player.setCurrentRoom(mapManager.getCurrentRoom());
		enemyManager 	= new EnemyManager();
		bulletManager 	= new BulletManager();
		
		enemyToBulletMediator = new Mediator(bulletManager, player);
		
		/* imposta come centro della camera il punto medio tra giocatore e cursore/mirino */
		camX = (player.getCentralX()+cursor.getCentralX())/2 - GameSettings.getInstance().getWRes()/2;
		camY = (player.getCentralY()+cursor.getCentralY())/2 - GameSettings.getInstance().getHRes()/2;
		
		hud = new HUD(camX, camY);
		
		isRunning = true;
		
		gameThread = new Thread(this);
		gameThread.start();
		
		fpsTimer = System.currentTimeMillis();
		
		// aggiungere timer per i Record
		chronometer = new Chronometer();		
		
		/* configurazione della prima stanza */
		enemyManager.addEnemies(mapManager.getCurrentRoomFloor(), 2, enemyToBulletMediator);
		mapManager.changeState(new Random().nextInt(2)+2);
		
		
		/* avvio della soundtrack */
		try {
			soundtrack = new GameSound("gameAssets/sounds/undertale_ost.wav");
			soundtrack.startSound();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		/* timer del paint component (non serve più) */
		//timer = new Timer(fpsDELAY, this);
		//timer.start();
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.translate(-camX, -camY);
        
        mapManager.drawMap((Graphics2D)g); 	/* disegna la mappa */
        
        player.draw((Graphics2D)g);			/* disegna il giocatore */
        enemyManager.draw((Graphics2D)g);	/* disegna i nemici */
        bulletManager.draw((Graphics2D)g);	/* disegna i proiettili */
        
        cursor.draw((Graphics2D)g);			/* disegna il cursore/mirino */
        
        hud.draw((Graphics2D)g);			/* disegna l' HUD */
        
        
        if(gameState != 0) {
        	g.setColor(new Color(0,0,0,150));
        	g.fillRect(camX, camY, GameSettings.getInstance().getWRes(), GameSettings.getInstance().getHRes());
        	
        	g.setColor(Color.WHITE);
        	
        	if(gameState == 1) { 	   /*	  PAUSA		*/
        		g.drawString("Press P to Resume", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Press P to Resume")/2, camY+GameSettings.getInstance().getHRes()/3);
        	}else if(gameState == 2) { /*  SCONFITTA  	*/
        		g.drawString("Sei Stato Sconfitto", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Sei Stato Sconfitto")/2, camY+GameSettings.getInstance().getHRes()/3);
        		g.drawString("Press R to Retry", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Press R to Retry")/2, camY+GameSettings.getInstance().getHRes()/2+50);
        	}else {					   /*   VITTORIA 	*/
        		
        		g.drawString("Hai Vinto", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Sei Stato Sconfitto")/2, camY+GameSettings.getInstance().getHRes()/3);
        		String s = " minuti: " + chronometer.getTime().get("minutes").toString() + " secondi: " + chronometer.getTime().get("seconds").toString();
        		g.drawString(s, camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth(s)/2, camY+GameSettings.getInstance().getHRes()/3+200);
        		
        	}
        	
        	g.drawString("Press ESC to Menu", camX+GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Press ESC to Menu")/2, camY+GameSettings.getInstance().getHRes()/2);
        	
        }
        
        
        Toolkit.getDefaultToolkit().sync();
        
	}
	
	public void controlliStanza() {
		
		/* controlla che i nemici siano finiti */
		if(enemyManager.roomClear()) {
			
			/* controlla in che stanza si trova il player */
			mapManager.setCurrentRoomIndex(player.getFeet());
			player.setCurrentRoom(mapManager.getCurrentRoom());
			
			
			if(!mapManager.getCurrentRoom().isVisited()) {
				
				/* se la stanza è quella attuale la imposta Visitata */
				if(!mapManager.differentRoom()) 
				{
					mapManager.getCurrentRoom().setVisited(true);
					/* se la stanza terminata è l'ultima */
					if(mapManager.lastRoom()) { gameState = 3; chronometer.updatePause();}
				}
				/* altrimenti, se è una stanza NON Visitata e non è l'ultima stanza, genera nuovi nemici, e cambia lo stato del terreno */
				else if(!mapManager.lastRoom()) {
					
					int n = (bot != null)? bot.getEnemiesNumber() : 0;
					if(n == 0) n = 3;
					enemyManager.addEnemies(mapManager.getCurrentRoomFloor(), n, enemyToBulletMediator);
					
					int waterState = (bot != null)? bot.getState()+2 : 0;
					if(waterState == 0) waterState = new Random().nextInt(2)+2;
					mapManager.changeState( waterState );
					
				}
				/* altrimenti, genera il Boss, e cambia lo stato del terreno */
				else {
					
					enemyManager.addBoss(mapManager.getCurrentRoomFloor(), 1, enemyToBulletMediator);
					
					int waterState = (bot != null)? bot.getState()+2 : 0;
					if(waterState == 0) waterState = new Random().nextInt(2)+2;
					mapManager.changeState( waterState );
					
				}
				
			}
			
		}
		
	}

	@Override
	public void run() {
		
		while(isRunning) {
			if(System.currentTimeMillis() > fpsTimer + fpsDELAY) {
				fpsTimer = System.currentTimeMillis();
				
				if(gameState == 0) { /* partita in corso (quindi ne pausa, ne vittoria, ne sconfitta)*/					
					
					mapManager.getCurrentRoom().update();
					player.update();
					bulletManager.update(mapManager.getCurrentRoomFloor(), enemyManager.getEnemies(), player);
					enemyManager.update(mapManager.getCurrentRoomFloor());
					
					controlliStanza();
					
					/* se la vita del player viene azzerata lo stato diventa Partita Persa/Sconfitta */
					if(player.getHealth() <= 0) gameState = 2;
					
					/* aggiornamento della posizione del cursore */
					Point p = MouseInfo.getPointerInfo().getLocation();
					SwingUtilities.convertPointFromScreen(p, this);
					cursor.setLocation( camX+(p.x-(cursor.getBounds().width/2)), camY+(p.y-(cursor.getBounds().height/2)));
					
					/* aggiornamento posizione camera/inquadratura */
					camX = (player.getCentralX() - GameSettings.getInstance().getWRes()/2 );
					camY = (player.getCentralY() - GameSettings.getInstance().getHRes()/2);
					//camX = (player.getCentralX()+cursor.getCentralX())/2 - GameSettings.getInstance().getWRes()/2;
					//camY = (player.getCentralY()+cursor.getCentralY())/2 - GameSettings.getInstance().getHRes()/2;
					
					hud.update(camX, camY, player.getHealth());
					
					chronometer.updateTime();
					
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
			if(gameState == 0 /*(player.getHealth()>0 && !(mapManager.lastRoom() && enemyManager.roomClear())*/)
				player.processPressEvent(e);
				
			/* comandi testing */
			if(e.getKeyCode() == KeyEvent.VK_V)
				mapManager.changeState(1);
			if(e.getKeyCode() == KeyEvent.VK_B)
				mapManager.changeState(2);
			if(e.getKeyCode() == KeyEvent.VK_N)
				mapManager.changeState(3);
			
			
			if(e.getKeyCode() == KeyEvent.VK_P) {
				if( gameState == 1 ) {
					gameState = 0;
					
					try {soundtrack.resumeSound();} 
						catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} 
						catch (IOException e1) 					 {e1.printStackTrace();} 
						catch (LineUnavailableException e1) 	 {e1.printStackTrace();}
					
				}
				else if( gameState == 0 ) {
					
					gameState = 1;
					soundtrack.pauseSound();
					chronometer.updatePause();
					
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_R && gameState == 2) {
				soundtrack.stopSound();
				isRunning = false;
				
				if(bot != null) {
					bot.closeBotX();
					controller.showGame(bot.getAuthToken(), bot.getChannel(), bot.getName());
				}else {
					controller.showGame();
				}
			}
			
			if(e.getKeyCode() == KeyEvent.VK_ESCAPE && gameState != 0) {
				soundtrack.stopSound();
				if(bot != null) bot.closeBotX();
				isRunning = false;
				controller.showMenu();
			}
				
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
