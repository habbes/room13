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
	
	/**
	 * 
	 * @param client the client to handle
	 * @param server the server to which the client is connected
	 */
	public ClientHandler(Client client, Server server){
		this.client = client;
		this.server = server;
	}
	
	/**
	 * Gets the Client being handled
	 * @return
	 */
	public Client getClient(){
		return client;
	}
	
	/**
	 * Runs the message handling loop. This meant to be run
	 * in its own thread.
	 */
	public void run(){
		handleClient();
	}
	
	/**
	 * Handles incoming messages from the client
	 */
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
				break;
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
	
	/**
	 * Handles a message from the client based on the message type
	 * @param msg
	 */
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
	
	/**
	 * Handles request for a list of rooms
	 * @param msg
	 */
	public void handleRooms(RoomsMessage msg){
		RoomListMessage resp = new RoomListMessage(msg.getReqId());
		for(String name : server.getRoomNames()){
			resp.addRoom(name);
		}
		sendMessage(resp);
		
	}
	
	/**
	 * Handles request to join a room
	 * @param msg
	 */
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
	
	/**
	 * Handles request to create a room
	 * @param msg
	 */
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
	
	/**
	 * Handles request to keep connection alive
	 * @param msg
	 */
	public void handleKeepAlive(KeepAliveMessage msg){
		//do nothing
	}
	
	/**
	 * Handles request to disconnect from server
	 * @param msg
	 */
	public void handleDisconnect(DisconnectMessage msg){
		handleDisconnect();
	}
	
	/**
	 * Disconnects from the client and notifies concerned rooms
	 */
	public void handleDisconnect(){
		
		client.disconnect();
		
		for(User user : client.getUsers()){
			user.getRoom().notifyUserDisconnected(user);
		}
		
		//remove client
		server.removeClientHandler(this);
	}
	
	
	

}
