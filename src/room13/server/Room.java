package room13.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import room13.message.Message;
import room13.message.messages.*;

public class Room {
	
	private List<User> users = new ArrayList<User>();
	private Map<String,User> usernames = new HashMap<String, User>();
	private int lastId = 0;
	private String password = "";
	
	public Room() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * creates a room user from a client appends the user to the users list
	 * @param Client client
	 */
	public void createUser(Client client){
		this.createUser(client,false);
	}
	/**
	 * creates a room user from a client appends 
	 * the user to the users list
	 * @param Client client
	 * @param Boolean isAdmin
	 */
	public void createUser(Client client,Boolean isAdmin){
		int id = this.generateId();
		User user = new User(client,this,id);
		if(isAdmin){
			user.setAdmin();
			this.setAdmin(user);
		}
	}
	/**
	 * returns a new last id (adds one to the current last id)
	 * @return int (lastId + 1)
	 */
	private int generateId(){
		return ++lastId;
	}
	/**
	 * Sets a name for the username if the name doesn't exist
	 * @param User user
	 * @param String name
	 * @throws Exception
	 */
	public void setUserName(User user,String name) throws Exception{
		if(usernames.containsKey(name))
			throw new Exception("User name in use");
		user.setName(name);
		usernames.put(name, user);
	}
	/**
	 * Removes a user from a room
	 * @param User user 
	 */
	public void removeUser(User user) throws Exception{
		if(!this.users.contains(user))
			throw new Exception("User doesnt exist");
		this.users.remove(user);
		this.usernames.remove(user.getName());
	}
	/**
	 * Returns users
	 * @return List users
	 */
	public List<User> getUsers(){
		return this.users;
	}
	/*
	 * Returns a User of the name
	 * @return User user
	 */
	public User findUser(String name){
		return this.usernames.get(name);
	}
	/**
	 * Sets Admin to a room
	 * @param user
	 */
	public void setAdmin(User user){
		user.setAdmin();
		this.setAdmin(user);
	}
	public void addUser(Client client,String pass) throws Exception{
		if(pass == this.password){
			User user = new User(client,this,this.generateId());
			this.users.add(user);
		}else{
			throw new Exception("Wrong password!");
		}
	}
	public void handleMessage(User user,Message msg){
		switch(msg.getMsgId()){
			case Message.BROADCAST:
				this.handleBroadcast(user,(BroadcastMessage)msg);
				break;
			case Message.SEND:
				this.handleSend(user,(SendMessage)msg);
				break;
			case Message.NAME:
				this.handleName(user,(NameMessage)msg);
				break;
			case Message.MEMBERS:
				this.handleMembers(user,(MembersMessage)msg);
				break;
			case Message.LEAVE_ROOM:
				this.handleLeaveRoom(user,(LeaveRoomMessage)msg);
				break;
			default:
			try {
				user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.ROOM_NOT_FOUND));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
	}
	public void handleSend(User user,SendMessage msg){
		try {
			User recipient = this.findUser(msg.getRecipient());
			if(recipient == null){
				user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.USER_NOT_FOUND));
			}else{
				recipient.send(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void handleBroadcast(User user,BroadcastMessage msg){
		
	}
	public void handleName(User user,NameMessage msg){
		
	}
	public void handleMembers(User user,MembersMessage msg){
		
	}
	public void handleLeaveRoom(User user,LeaveRoomMessage msg){
		
	}
	public void handleJoinRoom(User user,JoinRoomMessage msg){
		
	}
}
