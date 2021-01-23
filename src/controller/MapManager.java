package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import model.Room;
import model.Object;

public class MapManager {
	
	private ArrayList<Room> rooms;
	private ArrayList<Object> bridges;
	private int currentRoomIndex;
	
	public MapManager() {
		rooms = new ArrayList<Room>();
		currentRoomIndex = 0;
		
		bridges = new ArrayList<Object>();
		
		populateMap();
	}
	
	public void setCurrentRoomIndex(Rectangle player) {
		for(int i = 0; i < rooms.size(); ++i) {
			if(rooms.get(i).getFloor().contains(player))
				currentRoomIndex = i;		
		}
			
	}
	
	public Room getCurrentRoom() {
		return rooms.get(currentRoomIndex);
	}
	
	public Rectangle getCurrentRoomFloor() {
		return rooms.get(currentRoomIndex).getFloor();
	}
	
	/* disegna la mappa, adattare per disegnare solo le stanze nelle immediate vicinanze*/
	public void drawMap(Graphics2D g2d) {
		rooms.get(currentRoomIndex).drawRoom(g2d);
		for(Room r: rooms)
			r.draw(g2d);
		for(Object b: bridges)
			b.draw(g2d);
	}
	
	public void changeState(int state) {
		rooms.get(currentRoomIndex).changeWaterState(state);
	}
	
	public void populateMap() {
		rooms.add(new Room(0, 0));
		currentRoomIndex = 0;
		
		// genera un numero randomico di stanze da creare compreso tra 5 e 7
		int nRooms = 5;//new Random().nextInt(2)+5;
		
		// partendo dalla prima stanza inserisce delle stanze addiacenti e dei ponti per raggiungerle
		while(nRooms > 0) {
			
			for(int r = 0; r < rooms.size() && nRooms > 0; ++r) {
				
				// decide in che direzione creare le stanze
				boolean dir[] = new boolean[4];
				dir[0] = dir[1] = dir[2] = dir[3] = true;
				
				// controlla se sono presenti stanze nei dintorni di quella attuale
				
				// prendo le collisioni della stanza attuale
				Rectangle floor = rooms.get(r).getFloor();
				
				for(Room room: rooms) {
					if(room.getFloor().x == floor.x && room.getFloor().y == floor.y-floor.height-48*3) //up
						dir[0] = false;
					if(room.getFloor().x == floor.x+floor.width+48*3 && room.getFloor().y == floor.y) // right
						dir[1] = false;
					if(room.getFloor().x == floor.x && room.getFloor().y == floor.y+floor.height+48*3) // down
						dir[2] = false;
					if(room.getFloor().x == floor.x-floor.width-48*3 && room.getFloor().y == floor.y) // left
						dir[3] = false;
					
				}
				
				/* per ogni direzione, se lo spazio è libero, decide randomicamente se creare una stanza 
				 * edit : deve controllare se ha gia creato il numero necessario di stanze */
				if(dir[0] && nRooms>0) {	// up
					Room roomToAdd = new Room(floor.x, floor.y-floor.height-48*3);
					rooms.add(roomToAdd);
					
					Object bridge = new Object((int)floor.getCenterX()-8*3, floor.y-48*3, "gameAssets/sprites/verticalBridge.png");
					bridges.add(bridge);
					
					Rectangle bBounds = bridge.getBounds();
					rooms.get(r).setBridge(0, bBounds);
					roomToAdd.setBridge(2, bBounds);
					
					--nRooms;
				}
				if(dir[1] && nRooms>0 /*&&  new Random().nextBoolean()*/) {	// right
					Room roomToAdd = new Room(floor.x+floor.width+48*3, floor.y);
					rooms.add(roomToAdd);
					
					Object bridge = new Object(floor.x+floor.width, (int)floor.getCenterY()-8*3, "gameAssets/sprites/horizontalBridge.png");
					bridges.add(bridge);
					
					Rectangle bBounds = bridge.getBounds();
					rooms.get(r).setBridge(1, bBounds);
					roomToAdd.setBridge(3, bBounds);
					
					--nRooms;		
				}
				if(dir[2] && nRooms>0 && new Random().nextBoolean()) {	// down
					Room roomToAdd = new Room(floor.x, floor.y+floor.height+48*3);
					rooms.add(roomToAdd);
					
					Object bridge = new Object((int)floor.getCenterX()-8*3, floor.y+floor.height, "gameAssets/sprites/verticalBridge.png");
					bridges.add(bridge);
					
					Rectangle bBounds = bridge.getBounds();
					rooms.get(r).setBridge(2, bBounds);
					roomToAdd.setBridge(0, bBounds);
					
					--nRooms;
				}
				if(dir[3] && nRooms>0 && new Random().nextBoolean()) {	// left
					Room roomToAdd = new Room(floor.x-floor.width-48*3, floor.y);
					rooms.add(roomToAdd);
					
					Object bridge = new Object(floor.x-48*3, (int)floor.getCenterY()-8*3, "gameAssets/sprites/horizontalBridge.png");
					bridges.add(bridge);
					
					Rectangle bBounds = bridge.getBounds();
					rooms.get(r).setBridge(3, bBounds);
					roomToAdd.setBridge(1, bBounds);
					
					--nRooms;
				}
				
			}
			
		}
		
	}

}
