package room13.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketTimeoutException;

import room13.message.*;
import room13.message.messages.*;

/**
 * handles messages from a specific client
 * @author Habbes
 *
 */
public class ClientHandler implements Runnable {

	private Client client;
	private Server server;
	
	public ClientHandler(Client client, Server server){
		this.client = client;
		this.server = server;
	}
	
	public Client getClient(){
		return client;
	}
	
	public void run(){
		handleClient();
	}
	
	public void handleClient(){
		
		while(true){
			
			try {
				Message msg = client.receive();
				handleMessage(msg);
			}
			catch(MessageException e){
				ErrorMessage error = new ErrorMessage();
				error.setType(ErrorMessage.GENERIC_ERROR);
				sendMessage(error);
			}
			catch(SocketTimeoutException e){
				continue;
			}
			catch(IOException | RemoteConnectionClosedException e) {
				handleDisconnect();				
			}
			
		}
	}
	
	/**
	 * send message to the client
	 * @param msg
	 */
	public void sendMessage(Message msg){
		try {
			client.send(msg);
		}
		catch(InterruptedIOException e){
			
		}
		catch(IOException e){
			handleDisconnect();
		}
	}
	
	/**
	 * forward message to be handled in the appropriate room
	 * @param msg
	 */
	public void routeMessage(BaseRoomMessage msg){
		
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
			sendMessage(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NOT_FOUND));
			return;
		}
		
		room.handleMessage(user, msg);
	}

	public void handleMessage(Message msg){
		switch(msg.getMsgId()){
		case Message.ROOMS:
			handleRooms((RoomsMessage) msg);
			break;
		
		case Message.JOIN_ROOM:
			handleJoinRoom((JoinRoomMessage) msg);
			break;
		
		case Message.NEW_ROOM:
			handleNewRoom((NewRoomMessage) msg);
			break;
		case Message.KEEP_ALIVE:
			handleKeepAlive((KeepAliveMessage) msg);
			break;
		case Message.DISCONNECT:	
			handleDisconnect((DisconnectMessage) msg);
			break;
		case Message.BROADCAST:
		case Message.LEAVE_ROOM:
		case Message.MEMBERS:
		case Message.NAME:
		case Message.SEND:
			routeMessage((BaseRoomMessage) msg);
		default:
			sendMessage(new ErrorMessage(msg.getReqId(), ErrorMessage.GENERIC_ERROR));
		}
	}
	
	public void handleRooms(RoomsMessage msg){
		RoomListMessage resp = new RoomListMessage(msg.getReqId());
		for(String name : server.getRoomNames()){
			resp.addRoom(name);
		}
		sendMessage(resp);
		
	}
	
	public void handleJoinRoom(JoinRoomMessage msg){
		Room room = server.getRoom(msg.getRoomName());
		if(room == null){
			sendMessage(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NOT_FOUND));
		}
		else {
			try {
				User user = room.addUser(client, msg.getRoomPassword());
				sendMessage(new OkMessage(msg.getReqId()));
				room.notifyUserJoined(user);
			} catch (Exception e) {
				sendMessage(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_ACCESS_DENIED));
			}
		}
	}
	
	public void handleNewRoom(NewRoomMessage msg){
		Room room = server.createRoom(msg.getRoomName(), msg.getRoomPassword());
		if(room == null){
			sendMessage(new ErrorMessage(msg.getReqId(), ErrorMessage.ROOM_NAME_UNAVAILABLE));
		}
		else {
			room.createUser(client, true);
			sendMessage(new OkMessage());
		}
	}
	
	public void handleKeepAlive(KeepAliveMessage msg){
		//do nothing
	}
	
	public void handleDisconnect(DisconnectMessage msg){
		handleDisconnect();
	}
	
	public void handleDisconnect(){
		
		client.disconnect();
		
		for(User user : client.getUsers()){
			user.getRoom().notifyUserDisconnected(user);
		}
		
		//remove client
		server.removeClientHandler(this);
	}
	
	
	

}
