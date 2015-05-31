package room13.server;

import java.io.IOException;

import room13.message.*;
import room13.message.messages.BaseRoomMessage;
import room13.message.messages.DisconnectMessage;
import room13.message.messages.ErrorMessage;
import room13.message.messages.JoinRoomMessage;
import room13.message.messages.KeepAliveMessage;
import room13.message.messages.NewRoomMessage;
import room13.message.messages.OkMessage;
import room13.message.messages.RoomListMessage;
import room13.message.messages.RoomsMessage;

/**
 * handles messages from a specific client
 * @author Habbes
 *
 */
public class ClientHandler {

	private Client client;
	private Server server;
	
	public Client getClient(){
		return client;
	}
	
	public void loop(){
		
		
		
	}
	
	/**
	 * forward message to be handled in the appropriate room
	 * @param client
	 * @param msg
	 */
	public void routeMessage(Client client, BaseRoomMessage msg){
		
		//find room to route message to
		String name = msg.getRoomName();
		Room room = server.getRoom(name);
		User user = null;
		if(room == null){
			for(User u : client.getUsers()){
				if(u.getRoom() == room){
					user = u;
				}
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
		for(String name : server.getRoomNames()){
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
		Room room = server.getRoom(msg.getRoomName());
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
				User user = room.addUser(client, msg.getRoomPassword());
				client.send(new OkMessage(msg.getReqId()));
				room.notifyUserJoined(user);
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
		Room room = server.createRoom(msg.getRoomName(), msg.getRoomPassword());
		if(room == null){
			try {
				client.send(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NAME_UNAVAILABLE));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
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
		server.removeClientHandler(this);
	}
	

}
