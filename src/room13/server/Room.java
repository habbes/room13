package room13.server;

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
	private volatile int lastId = 0;
	private String password = "";
	
	//used to synchronize access to the users list
	private Object usersLock = new Object();
	//used to synchronize increment of lastId
	private Object idLock = new Object();
	
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
	public User createUser(ClientHandler client){
		return createUser(client,false);
	}
	/**
	 * creates a room user from a client appends 
	 * the user to the users list
	 * @param Client client
	 * @param Boolean isAdmin
	 */
	public User createUser(ClientHandler client,Boolean isAdmin){
		int id = this.generateId();
		User user = new User(client,this,id);
		if(isAdmin){
			user.setAdmin();
			this.setAdmin(user);
		} 
		synchronized(users){
			users.add(user);
		}
		return user;
	}
	/**
	 * returns a new last id (adds one to the current last id)
	 * @return int (lastId + 1)
	 */
	private int generateId(){
		synchronized(idLock){
			return ++lastId;
		}
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
		synchronized(usersLock){
			if(usernames.containsKey(name))
				throw new Exception("User name in use");
			user.setName(name);
			usernames.put(name, user);
		}
	}
	/**
	 * Removes a user from a room
	 * @param User user 
	 */
	public void removeUser(User user){
		synchronized(usersLock){
			users.remove(user);
			this.usernames.remove(user.getName());
		}
	}
	/**
	 * Returns users
	 * @return List users
	 */
	public List<User> getUsers(){
		synchronized(usersLock){
			return this.users;
		}
	}
	/*
	 * Returns a User of the name
	 * @return User user
	 */
	public User findUser(String name){
		synchronized(usersLock){
			return this.usernames.get(name);
		}
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
	 * @param client
	 * @param password
	 * @throws Exception when the password is wrong
	 */
	public User addUser(ClientHandler client,String pass) throws Exception{
		if(pass == this.password){ //check password
			User user = new User(client,this,this.generateId());  //create user
			synchronized(usersLock) {
				users.add(user); //add the user
			}
			return user;
		}else{
			throw new Exception("Wrong password!"); //oops!!! something went wrong
		}
	}
	
	/**
	 * Sends a message to all users in the room
	 * @param msg
	 */
	public void sendBroadcast(Message msg){
		synchronized(usersLock){
			for(User user : users){
				user.send(msg);
			}
		}
	}
	
	/**
	 * Sends a message to all users in the room except the specified
	 * user to ignore
	 * @param msg
	 * @param ignoreUser
	 */
	public void sendBroadcast(Message msg, User ignoreUser){
		synchronized(usersLock){
			for(User user : users){
				if(user != ignoreUser){
					user.send(msg);
				}
			}
		}
	}
	
	
	/**
	 * Notifies users that a user has been disconnected and removes the user from the room
	 * @param username
	 * @throws Exception
	 */
	public void notifyUserDisconnected(User user){
		removeUser(user);
		sendBroadcast(new UserDisconnectedEventMessage(user.getName()));
	}
	
	/**
	 * Notifies users that a user has left the room and removes the user from the room
	 * @param user
	 * @param msg
	 */
	public void notifyUserLeft(User user){
		removeUser(user);
		sendBroadcast(new UserLeftEventMessage(user.getName()));
	}
	
	/**
	 * Notifies users that a user has joined the room
	 * @param user
	 */
	public void notifyUserJoined(User user){
		sendBroadcast(new UserJoinedEventMessage(user.getName()), user);
	}
	
	/**
	 * Notifies users that a user has change names
	 * @param user
	 * @param oldName the user's old name
	 */
	public void notifyUserNameChanged(User user, String oldName){
		sendBroadcast(new UserNameChangedEventMessage(oldName, user.getName()));
	}
	
	/*
	 * This is where all messages are routed to their respective handlers depending on the 
	 * message type
	 * @param user
	 * @param msg
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
				user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.ROOM_NOT_FOUND));				
		}
	}
	/*
	 * Handles any message that needs to be sent
	 * @param user
	 * @param msg
	 */
	public void handleSend(User user,SendMessage msg){
		User recipient = this.findUser(msg.getRecipient());
		if(recipient == null){
			user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.USER_NOT_FOUND));
		}else{
			recipient.send(msg);
			user.send(new OkMessage(msg.getReqId()));
		}
	}
	/*
	 * Handles a user defined broadcast message
	 * @param User user the sender
	 * @param BroadcastMessage msg
	 */
	public void handleBroadcast(User user,BroadcastMessage msg){
		sendBroadcast(msg, user);
		user.send(new OkMessage(msg.getReqId()));
	}
	/*
	 * Handles the name change, if the user wants to change their name
	 * @param user user
	 * @param NameMessage msg
	 */
	public void handleName(User user,NameMessage msg){
		if(!this.usernames.containsKey(msg.getName())){
			this.usernames.put(msg.getName(),user);
			user.send(new OkMessage(msg.getReqId()));
			notifyUserNameChanged(user, msg.getName());
		}else{
			user.send(new ErrorMessage(msg.getReqId(),ErrorMessage.USER_NAME_UNAVAILABLE));
		}
	}
	/*
	 * Handles the members message by returning a list of all the members names
	 * @param User user
	 * @param MembersMessage msg
	 */
	public void handleMembers(User user,MembersMessage msg){
		MembersListMessage members = new MembersListMessage(this.name,msg.getReqId());
		synchronized(usersLock){
			for(String name : this.usernames.keySet()){
				members.addMember(name);
			}
		}
		user.send(members);
	}
	/*
	 * Handles the leave room message
	 * @param User user
	 * @param LeaveRoomMessagemsg
	 */
	public void handleLeaveRoom(User user,LeaveRoomMessage msg){
		notifyUserLeft(user);
	}
}
