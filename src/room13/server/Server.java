package room13.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import room13.message.*;
import room13.message.messages.*;


public class Server {

	public Server() {
		// TODO Auto-generated constructor stub
	}
	
	List<Client> clients = new ArrayList<Client>();
	Map<String, Room> rooms = new HashMap<String,Room>();
	
	public void findRoom(Client client, BaseRoomMessage msg){
		
	}
	
	public void routeMessage(Room room, User user, Message msg){
		
	}

	public void handleMessage(Client client, Message msg){
		
	}
	
	public void handleRooms(Client client, RoomsMessage msg){
		
	}
	
	public void handleJoinRoom(Client client, JoinRoomMessage msg){
		
	}
	
	public void handleKeepAlive(Client client, KeepAliveMessage msg){
		
	}
	
	public void handleDisconnect(Client client, DisconnectMessage msg){
		
	}
	

}
