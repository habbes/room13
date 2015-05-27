package room13.server;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client {
	
	private String address;
	private int port;
	
	private Socket socket;
	
	private List<User> users = new ArrayList<User>();
	
	/**
	 * Creates a client 
	 * @param String address
	 * @param int port
	 */
	public Client(String address, int port) {
		this.address = address;
		this.port = port;
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
	}
	/*
	 * Returns the list of all users
	 * @return List<User> users
	 */
	public List<User> getUsers(){
		return this.users;
	}
	
}