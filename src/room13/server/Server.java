package room13.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import room13.message.*;
import room13.message.messages.*;


public class Server {

	public Server() {
		// TODO Auto-generated constructor stub
	}
	
	List<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
	Map<String, Room> rooms = new HashMap<String,Room>();
	
	/**
	 * gets the room with the specified name
	 * @param name
	 * @return
	 */
	public synchronized Room getRoom(String name){
		return rooms.get(name);
	}
	
	/**
	 * gets the sets of the names of all the rooms
	 * @return
	 */
	public synchronized Set<String> getRoomNames(){
		return rooms.keySet();
	}
	
	/**
	 * Creates a new room with the given name and password, if the name is available
	 * @param name
	 * @param password
	 * @return the created Room, or null if the name is not available
	 */
	public synchronized Room createRoom(String name, String password){
		if(rooms.containsKey(name))
			return null;
		Room room = new Room(name, password);
		rooms.put(name, room);
		return room;
	}
	
	public void removeClientHandler(ClientHandler handler){
		clientHandlers.remove(handler);
	}
	

}
