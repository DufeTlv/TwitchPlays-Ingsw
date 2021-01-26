package view;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.GameSettings;

import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class mainclass extends JFrame{
	
	private CardLayoutGameController controller;
	
	public mainclass() {
		setSize(GameSettings.getInstance().getWRes(), GameSettings.getInstance().getHRes());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//contentPane = new JPanel();
		CardLayout layout = new CardLayout();
		getContentPane().setLayout(layout);
		
		controller = new CardLayoutGameController(getContentPane(), layout);
				
		add(new MenuPanel(controller), CardLayoutGameController.MENU_PANEL);		
		
		// Creare un cursore vuoto e passarlo al JFrame (this)
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
		
		setTitle("TwitchPlays: Jigokudani Monkey Party");
	    setResizable(false);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
            public void run() {
            	mainclass execute = new mainclass();
                execute.setVisible(true);
            }
        });

	}

}
