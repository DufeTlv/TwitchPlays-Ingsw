package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import controller.GameSettings;
import model.Object;

public class MenuPanel extends JPanel implements ActionListener, Runnable{
	
	private Timer timer;
	private long fpsTimer;
	private final int fpsDELAY = 17;
	private Thread gameThread = null;
	private CardLayoutGameController controller;
	
	private JTextField textField0, textField1, textField2;
	
	private JComboBox choiceBox;
	private String[] choices = {"800x600", "1280x720", "1920x1080"};
	
	private boolean isRunning;
	
	private Rectangle menuScreen;
	private Object startC, startO, settings, exit, backArrow;
	private Object cursor;
	
	private int menuSection;
	

	public MenuPanel(CardLayoutGameController c) {
		addMouseListener(new MouseInputManager());
		
		setFocusable(true);
		setOpaque(false);
		setBackground(Color.BLACK);
		setFont(new Font("TimesRoman", Font.BOLD, 20));
		setLayout(null);
		
		controller = c;
		
		// variabili/oggetti di gioco
		menuScreen = new Rectangle(0,0, GameSettings.getInstance().getWRes(), GameSettings.getInstance().getHRes());
		
		startC 		= new Object(GameSettings.getInstance().getWRes()/2 ,GameSettings.getInstance().getHRes()/2, 			 "gameAssets/sprites/NewGameText.png"); 
		startO		= new Object(GameSettings.getInstance().getWRes()/2 ,startC.y  +startC.height 		+ startC.height/2, 	 "gameAssets/sprites/OnlineText.png"); 
		settings	= new Object(GameSettings.getInstance().getWRes()/2 ,startO.y  +startO.height 		+ startO.height/2, 	 "gameAssets/sprites/SettingsText.png"); 
		exit		= new Object(GameSettings.getInstance().getWRes()/2 ,settings.y+settings.height 	+ startC.height/2, 	 "gameAssets/sprites/ExitText.png");
		backArrow 	= new Object(50,50, "gameAssets/sprites/BackArrowMenu.png");
		
		startC.x 	-= startC.width/2;
		startO.x 	-= startO.width/2;
		settings.x 	-= settings.width/2;
		exit.x 		-= exit.width/2;
		
		cursor = new Object(0,0, "gameAssets/sprites/cursor.png");
		menuSection = 0;
		
		textField0 = new JTextField();
		textField0.setBounds(300, 250, 200, 25);
		textField0.setEditable(false);
		textField0.setVisible(false);
		
		textField1 = new JTextField();
		textField1.setBounds(300, 300, 200, 25);
		textField1.setEditable(false);
		textField1.setVisible(false);
		
		textField2 = new JTextField();
		textField2.setBounds(300, 350, 200, 25);
		textField2.setEditable(false);
		textField2.setVisible(false);
		
		choiceBox = new JComboBox(choices);
		choiceBox.setBounds(GameSettings.getInstance().getWRes()/3, GameSettings.getInstance().getHRes()/3, 270, 25);
		choiceBox.setEnabled(false);
		choiceBox.setVisible(false);
		
		add(textField0);
		add(textField1);
		add(textField2);
		add(choiceBox);		
		
		// settaggio dei thread
		isRunning = true;
		
		gameThread = new Thread(this);
		gameThread.start();
		
		fpsTimer = System.currentTimeMillis();
		
	}
	
	@Override
    public void paintComponent(Graphics g) {
		g.setColor(Color.CYAN.darker());
		g.fillRect(menuScreen.x, menuScreen.y, menuScreen.width, menuScreen.height);
		
		g.setColor(Color.WHITE);
		if(menuSection == 0) { // menu
			startC.draw((Graphics2D)g);
			startO.draw((Graphics2D)g);
			settings.draw((Graphics2D)g);
			exit.draw((Graphics2D)g);
			
		}else if(menuSection == 1) { // sotto menu partita online
			backArrow.draw((Graphics2D)g);
			
			g.drawString("Token", GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Token")/2, 250);
			g.drawString("Nome Canale", GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Nome Canale")/2, 300);
			
		}else { // impostazioni
			g.drawString("Impostazioni", (GameSettings.getInstance().getWRes()/2-g.getFontMetrics().stringWidth("Impostazioni")/2) , GameSettings.getInstance().getHRes()/4);
			backArrow.draw((Graphics2D)g);
		}
		
		cursor.draw((Graphics2D)g);
		
		Toolkit.getDefaultToolkit().sync();
		
	}
	
	@Override
	public void run() {
		while(isRunning) {
			if(System.currentTimeMillis() > fpsTimer + fpsDELAY) {
				fpsTimer = System.currentTimeMillis();
				
				/* aggiornamento della posizione del cursore */
				Point p = MouseInfo.getPointerInfo().getLocation();
				SwingUtilities.convertPointFromScreen(p, this);
				cursor.setLocation( (p.x-(cursor.getBounds().width/2)), (p.y-(cursor.getBounds().height/2)));
				
				repaint();
			}
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		
	}
	
	/*			  Class MouseInputManager			*/
	private class MouseInputManager extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e) {
			if(SwingUtilities.isLeftMouseButton(e)) {
				
				if(backArrow.contains(cursor.getCentralX(), cursor.getCentralY()) && menuSection != 0) {
					menuSection = 0;
					textField0.setEditable(false);
					textField0.setVisible(false);
					textField1.setEditable(false);
					textField1.setVisible(false);
					textField2.setEditable(false);
					textField2.setVisible(false);
					choiceBox.setEnabled(false);
					choiceBox.setVisible(false);
				}
				
				if(startC.contains(cursor.getCentralX(), cursor.getCentralY())) { // partita classica
					controller.showGame();
				}
				
				else if(startO.contains(cursor.getCentralX(), cursor.getCentralY())) { // partita online
					menuSection = 1;
					choiceBox.setEnabled(false);
					choiceBox.setVisible(false);
					textField0.setEditable(true);
					textField0.setVisible(true);
					textField1.setEditable(true);
					textField1.setVisible(true);
					textField2.setEditable(true);
					textField2.setVisible(true);
				}
				
				else if(settings.contains(cursor.getCentralX(), cursor.getCentralY())) { // impostazioni
					menuSection = 2;
					choiceBox.setEnabled(true);
					choiceBox.setVisible(true);
					textField0.setEditable(false);
					textField0.setVisible(false);
				}
				
				else if(exit.contains(cursor.getCentralX(), cursor.getCentralY())){ // esci dal gioco
					isRunning = false;
					System.exit(0);
				}
				
			}
		}
		
	}

}
