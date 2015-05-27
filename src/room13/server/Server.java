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
	 * find the room based on the client and room identifier in the message header
	 * @param client
	 * @param msg
	 * @return
	 */
	public Room findRoom(Client client, BaseRoomMessage msg){
		String name = msg.getRoomName();
		Room room = rooms.get(name);
		for(User user : client.getUsers()){
			if(user.getRoom() == room){
				return room;
			}
		}
		
		return null;
	}
	
	/**
	 * forward message to be handled in the appropriate room
	 * @param room
	 * @param user
	 * @param msg
	 */
	public void routeMessage(Room room, User user, Message msg){
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
		
	}
	
	public void handleKeepAlive(Client client, KeepAliveMessage msg){
		
	}
	
	public void handleDisconnect(Client client, DisconnectMessage msg){
		
	}
	

}
