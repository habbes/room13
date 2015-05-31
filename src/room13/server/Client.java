package room13.server;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import room13.message.*;

/**
 * represents a connected client
 * @author Habbes
 *
 */
public class Client {
	
	
	private MessageWriter writer;
	private MessageReader reader;
	
	private Socket socket;
	
	private List<User> users = new ArrayList<User>();
	
	private boolean connected = true;
	
	public final static int DEFAULT_TIMEOUT = 1000;
	
	/**
	 * Creates a client 
	 * @param String address
	 * @param int port
	 * @throws IOException 
	 */
	public Client(Socket sock) throws IOException {
		socket = sock;
		socket.setSoTimeout(DEFAULT_TIMEOUT);
		writer = new MessageWriter(socket.getOutputStream());
		reader = new MessageReader(socket.getInputStream());
		connected = true;
	}
	
	/**
	 * Adds a user to its list of users
	 * @param User user
	 */
	public void addUser(User user){
		this.users.add(user);
	}
	
	/*
	 * Returns the list of all users
	 * @return List<User> users
	 */
	public List<User> getUsers(){
		return this.users;
	}
	
	/**
	 * checks whether the client is connected to the server
	 * @return
	 */
	public boolean isConnected(){
		return connected;
	}
	
	/**
	 * Disconnect from the client connection
	 */
	public void disconnect(){
		if(connected){
			try {
				socket.close();
				connected = false;
			} catch (IOException e) {}
		}
	}
	
	/*
	 * Sends the message
	 */
	public void send(Message msg) throws IOException{
		writer.write(msg);	
	}
	
	/**
	 * waits for an incoming message and reads it
	 * @return the Message read, or null if remote peer disconnected
	 * @throws RemoteConnectionClosedException 
	 * @throws IOException 
	 * @throws MessageException
	 */
	public Message receive() throws IOException, RemoteConnectionClosedException{
		return MessageBuilder.build(reader.read());
	}	
	
}