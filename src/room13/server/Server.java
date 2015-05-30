package room13.server;

import java.io.IOException;
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
	
	
	
	/**
	 * forward message to be handled in the appropriate room
	 * @param client
	 * @param msg
	 */
	public void routeMessage(Client client, BaseRoomMessage msg){
		
		//find room to route message to
		String name = msg.getRoomName();
		Room room = rooms.get(name);
		User user = null;
		for(User u : client.getUsers()){
			if(u.getRoom() == room){
				user = u;
			}
		}
		if(user == null){
			try {
				client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NOT_FOUND));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		room.handleMessage(user, msg);
	}

	public void handleMessage(Client client, Message msg){
		switch(msg.getMsgId()){
		case Message.ROOMS:
			handleRooms(client, (RoomsMessage) msg);
			break;
		
		case Message.JOIN_ROOM:
			handleJoinRoom(client, (JoinRoomMessage) msg);
			break;
		
		case Message.NEW_ROOM:
			handleNewRoom(client, (NewRoomMessage) msg);
			break;
		case Message.KEEP_ALIVE:
			handleKeepAlive(client, (KeepAliveMessage) msg);
			break;
		case Message.DISCONNECT:	
			handleDisconnect(client, (DisconnectMessage) msg);
			break;
		case Message.BROADCAST:
		case Message.LEAVE_ROOM:
		case Message.MEMBERS:
		case Message.NAME:
		case Message.SEND:
			routeMessage(client, (BaseRoomMessage) msg);
		default:
			try {
				client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.GENERIC_ERROR));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleRooms(Client client, RoomsMessage msg){
		RoomListMessage resp = new RoomListMessage(msg.getReqId());
		for(String name : rooms.keySet()){
			resp.addRoom(name);
		}
		try {
			client.send(resp);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void handleJoinRoom(Client client, JoinRoomMessage msg){
		Room room = rooms.get(msg.getRoomName());
		if(room == null){
			try {
				client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NOT_FOUND));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				room.addUser(client, msg.getRoomPassword());
				client.send(new OkMessage(msg.getReqId()));
			} catch (Exception e) {
				try {
					client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_ACCESS_DENIED));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	public void handleNewRoom(Client client, NewRoomMessage msg){
		String name = msg.getRoomName();
		if(rooms.containsKey(name)){
			try {
				client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NAME_UNAVAILABLE));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			Room room = new Room(name, msg.getRoomPassword());
			rooms.put(name, room);
			room.createUser(client, true);
			try {
				client.send(new OkMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void handleKeepAlive(Client client, KeepAliveMessage msg){
		//do nothing
	}
	
	public void handleDisconnect(Client client, DisconnectMessage msg){
		
		client.disconnect();
		
		for(User user : client.getUsers()){
			user.getRoom().notifyUserDisconnected(user);
		}
		
		//remove client
		clients.remove(client);
	}
	

}
