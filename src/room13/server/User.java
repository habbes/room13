package room13.server;

import java.io.IOException;

import room13.message.Message;
import room13.message.messages.ErrorMessage;

public class User {

	private String name = "";
	private boolean isAdmin = false;
	private Client client;
	private Room room;
	private int id = 0;
	
	/**
	 * Creates a room user from a client 
	 * @param Client client
	 * @param int id
	 */
	public User(Client client,Room room, int id) {
		this.client = client;
		this.room = room;
		this.id = id;
		client.addUser(this);
	}
	/**
	 * Check if a user is an admin
	 * @return boolean
	 */
	public boolean isAdmin(){
		return this.isAdmin;
	}
	/**
	 * sets the name of the user
	 * @param string name
	 */
	public void setName(String name){
		this.name = name;
	}
	/**
	 * gets the name of the user
	 * @return string name
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * Returns a client from the user
	 * @return Client
	 */
	public Client getClient(){
		return this.client;
	}
	/**
	 * Returns a room from the user
	 * @return
	 */
	public Room getRoom(){
		return this.room;
	}
	/**
	 * Sets admin to the user
	 */
	public void setAdmin(){
		this.isAdmin = true;
	}
	public void send(Message msg) throws IOException {
			this.client.send(msg);		
	}

}
