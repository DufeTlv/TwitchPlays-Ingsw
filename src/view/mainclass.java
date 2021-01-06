package view;

import javax.swing.JFrame;

import controller.GameSettings;

import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class mainclass extends JFrame{
	
	public mainclass() {
		setSize(GameSettings.getInstance().getWRes(), GameSettings.getInstance().getHRes());
		add(new GamePanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Creare un cursore vuoto e passarlo al JFrame (this)
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		getContentPane().setCursor(blankCursor);
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
