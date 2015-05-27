package room13.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import room13.message.Message;
import room13.message.messages.*;

public class Room {
	
	private String name;
	private List<User> users = new ArrayList<User>();
	private Map<String,User> usernames = new HashMap<String, User>();
	private int lastId = 0;
	private String password = "";
	
	public Room(String name) {
		this(name,"");
	}
	
	public Room(String name, String pass){
		this.name = name;
		this.password = pass;
	}
	
	public void setPassword(String pass){
		password = pass;
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
	/*
	 * Sets the name of the room
	 * @param String name
	 */
	public void setName(String name){
		this.name = name;
	}
	/*
	 * Used to get the name of the room
	 * @return String name
	 */
	public String getName(){
		return this.name;
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
	/*
	 * Adds a user to the room
	 * @param Client client
	 * @param String password
	 * @throws Exception when the password is wrong
	 */
	public void addUser(Client client,String pass) throws Exception{
		/*
		 *I need to send an event after clement creates it ATTENTION!!!!!!!!! 
		 */
		if(pass == this.password){ //check password
			User user = new User(client,this,this.generateId());  //create user
			this.users.add(user); //add the user
		}else{
			throw new Exception("Wrong password!"); //oops!!! something went wrong
		}
	}
	/*
	 * This is where all messages are routed to their respective handlers depending on the 
	 * message type
	 * @param User user
	 * @param Message msg
	 */
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
	/*
	 * Handles any message that needs to be sent
	 * @param User user
	 * @param SendMessage msg
	 */
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
	/*
	 * Handles any broadcast message
	 * @param User user
	 * @param BroadcastMessage msg
	 */
	public void handleBroadcast(User user,BroadcastMessage msg){
		for(User recipient : this.users){
			try {
				recipient.send(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * Handles the name change, if the user wants to change their name
	 * @param user user
	 * @param NameMessage msg
	 */
	public void handleName(User user,NameMessage msg){
		if(!this.usernames.containsKey(msg.getName())){
			this.usernames.put(msg.getName(),user);
			try {
				user.send(new OkMessage(msg.getReqId()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.USER_NAME_UNAVAILABLE));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/*
	 * Handles the members message by returning a list of all the members names
	 * @param User user
	 * @param MembersMessage msg
	 */
	public void handleMembers(User user,MembersMessage msg){
		MembersListMessage members = new MembersListMessage(this.name,msg.getReqId());
		for(String name : this.usernames.keySet()){
			members.addMember(name);
		}
		try {
			user.send(members);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * Handles the leave room message
	 * @param User user
	 * @param LeaveRoomMessagemsg
	 */
	public void handleLeaveRoom(User user,LeaveRoomMessage msg){
		this.users.remove(user);
		this.usernames.remove(user.getName(),user);
		//i need to notify all the users when clement creates an event notification
	}
}
