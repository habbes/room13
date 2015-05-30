package room13.server;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import room13.message.*;

public class Client {
	
	
	private MessageWriter writer;
	private MessageReader reader;
	
	private Socket socket;
	
	private List<User> users = new ArrayList<User>();
	
	/**
	 * Creates a client 
	 * @param String address
	 * @param int port
	 * @throws IOException 
	 */
	public Client(Socket sock) throws IOException {
		socket = sock;
		writer = new MessageWriter(socket.getOutputStream());
		reader = new MessageReader(socket.getInputStream());
	}
	
	public void disconnect(){
		try {
			socket.close();
		} catch (IOException e) {}
	}
	
	/**
	 * Adds a user to its list of users
	 * @param User user
	 */
	public void addUser(User user){
		this.users.add(user);
	}
	/**
	 * Removes the user
	 * notifies the rooms connected to the user the user has left
	 * @params User user
	 */
	public void removeUser(User user){
			for(User existingUser : this.users){
				Room room = existingUser.getRoom();
					try{
						room.removeUser(user);
					}catch (Exception e){
						e.printStackTrace();
				}
			}
		this.users.remove(user);
	}
	/*
	 * Returns the list of all users
	 * @return List<User> users
	 */
	public List<User> getUsers(){
		return this.users;
	}
	/*
	 * Sends the message
	 */
	public void send(Message msg) throws IOException{
		writer.write(msg);
	}
	
	
	
}