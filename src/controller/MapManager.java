package controller;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import model.Room;

public class MapManager {
	
	private ArrayList<Room> rooms;
	private ArrayList<Object> bridges;
	private int currentRoom;
	
	public MapManager() {
		rooms = new ArrayList<Room>();
		currentRoom = 0;
		
		bridges = new ArrayList<Object>();
		
		populateMap();
	}
	
	public Rectangle getCurrentRoomFloor() {
		return rooms.get(currentRoom).getFloor();
	}
	
	/* disegna la mappa, adattare per disegnare solo le stanze nelle immediate vicinanze*/
	public void drawMap(Graphics2D g2d) {
		rooms.get(currentRoom).drawRoom(g2d);
		for(Room r: rooms)
			r.draw(g2d);
	}
	
	public void changeState(int state) {
		rooms.get(currentRoom).changeWaterState(state);
	}
	
	public void populateMap() {
		rooms.add(new Room(0, 0));
		currentRoom = 0;
		
		// genera un numero randomico di stanze da creare compreso tra 5 e 7
		int nRooms = 1;//new Random().nextInt(2)+5;
		
		// partendo dalla prima stanza inserisce delle stanze addiacenti e dei ponti per raggiungerle
		while(nRooms > 0) {
			
			for(int r = 0; r < rooms.size() && nRooms > 0; ++r) {
				
				// decide in che direzione creare le stanze
				boolean dir[] = new boolean[4];
				dir[0] = dir[1] = dir[2] = dir[3] = true;
				
				// controlla se sono presenti stanze nei dintorni di quella attuale
				for(Room room: rooms) {
					Rectangle rect = rooms.get(r).getFloor();
					
					if(room.getFloor().x == rect.x && room.getFloor().y == rect.y-rect.height-16*3) //up
						dir[0] = false;
					if(room.getFloor().x == rect.x+rect.width+16*3 && room.getFloor().y == rect.y) // right
						dir[1] = false;
					if(room.getFloor().x == rect.x && room.getFloor().y == rect.y+rect.height+16*3) // down
						dir[2] = false;
					if(room.getFloor().x == rect.x-rect.width-16*3 && room.getFloor().y == rect.y) // left
						dir[3] = false;
				}
				
				
				/* per ogni direzione, se lo spazio è libero, decide randomicamente se creare una stanza */
				if(dir[0]) {	// up
					Room roomToAdd = new Room(rooms.get(0).getFloor().x, rooms.get(0).getFloor().y-rooms.get(0).getFloor().height-16*3);
					roomToAdd.setBridge(0, 0);
					rooms.add(roomToAdd);
					
					--nRooms;
				}
				if(dir[1] && new Random().nextBoolean()) {	// right
					Room roomToAdd = new Room(rooms.get(0).getFloor().x+rooms.get(0).getFloor().width+16*3, rooms.get(0).getFloor().y);
					roomToAdd.setBridge(0, 0);
					rooms.add(roomToAdd);
					
					--nRooms;				
				}
				if(dir[2] && new Random().nextBoolean()) {	// down
					Room roomToAdd = new Room(rooms.get(0).getFloor().x, rooms.get(0).getFloor().y+rooms.get(0).getFloor().height+16*3);
					roomToAdd.setBridge(0, 0);
					rooms.add(roomToAdd);
					
					--nRooms;
				}
				if(dir[3] && new Random().nextBoolean()) {	// left
					Room roomToAdd = new Room(rooms.get(0).getFloor().x-rooms.get(0).getFloor().width-16*3, rooms.get(0).getFloor().y);
					roomToAdd.setBridge(0, 0);
					rooms.add(roomToAdd);
					
					--nRooms;
				}
				
			}
			
		}
		
		/*Room roomToAdd = new Room(rooms.get(0).getFloor().x, rooms.get(0).getFloor().y-rooms.get(0).getFloor().height-16*3);
		roomToAdd.setBridge(0, 0);
		rooms.add(roomToAdd);*/
		
		// a quelle stanze aggiungere altre stanze se in quella direzione non è gia presente una stanza
		// in tal caso decidere se aggiungere o meno un ponte
		
		// ad ogni stanza creata decrementare il numero randomico generato
		
	}

}
