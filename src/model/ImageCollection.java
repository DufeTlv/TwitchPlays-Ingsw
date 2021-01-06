package model;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/* ImageCollection rappresenta una classe Singleton che implementa un Flyweight
 * in questo modo ogni volta che viene creato un nuovo Object, l'immagine in esso contenuta
 * farà riferimento ad una già esistente nel Hashtable di questa classe, risparmiando memoria */

public class ImageCollection {
	
	private Hashtable<String, Image> collection;		// Flyweight
	private static ImageCollection instance = null;		// Singleton
	
	private ImageCollection() {
		collection = new Hashtable<String, Image>();
	}
	
	public static ImageCollection getInstance() {
		if(instance == null)
			instance = new ImageCollection();
		
		return instance;
	}
	
	public Image getImage(String filePath) throws IOException {
		
		/* ritorna direttamente l'immagine se è già stata caricata in precedenza */
		if(collection.containsKey(filePath)) {
			//System.out.println("numero di sprite caricate: " + collection.size() + " stampa a riga 37 di ImageCollection");
			return collection.get(filePath);
		}
		
		/* altrimenti carica la nuova immagine */
		BufferedImage bimg = ImageIO.read(new File(filePath));
		
		/* ne scala la dimensione */
		int _width  = bimg.getWidth()*3;
		int _height = bimg.getHeight()*3;
		Image image = bimg.getScaledInstance(_width, _height, Image.SCALE_DEFAULT);
		
		/* la salva nell'hashtable */
		collection.put(filePath, image);
		
		return image;
	}
	
	

}