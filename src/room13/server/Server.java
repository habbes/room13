package room13.server;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
	
	private ServerSocket socket;
	
	/**
	 * used to execute ClientHandlers asynchronously
	 */
	private ExecutorService handlerExecutor;
	
	private boolean terminateRequested = false;
	
	public static final int DEFAULT_PORT = 13500;
	public static final int DEFAULT_TIMEOUT = 1000;
	
	/**
	 * Creates a server and binds it to the specified port
	 * @param port
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		socket = new ServerSocket(port);
		socket.setSoTimeout(DEFAULT_TIMEOUT);
		handlerExecutor = Executors.newCachedThreadPool();
	}
	
	/**
	 * Creates a server and binds it to the DEFAULT_PORT
	 * @throws IOException
	 */
	public Server() throws IOException{
		this(DEFAULT_PORT);
	}
	
	List<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();
	Map<String, Room> rooms = new HashMap<String,Room>();
	
	/**
	 * gets the room with the specified name
	 * @param name
	 * @return
	 */
	public synchronized Room getRoom(String name){
		return rooms.get(name);
	}
	
	/**
	 * gets the sets of the names of all the rooms
	 * @return
	 */
	public synchronized Set<String> getRoomNames(){
		return rooms.keySet();
	}
	
	/**
	 * Creates a new room with the given name and password, if the name is available
	 * @param name
	 * @param password
	 * @return the created Room, or null if the name is not available
	 */
	public synchronized Room createRoom(String name, String password){
		if(rooms.containsKey(name))
			return null;
		Room room = new Room(name, password);
		rooms.put(name, room);
		return room;
	}
	
	/**
	 * removes a ClientHandler from the server's list
	 * @param handler
	 */
	public void removeClientHandler(ClientHandler handler){
		clientHandlers.remove(handler);
	}
	
	/**
	 * add a ClientHandler to the server's list
	 * @param handler
	 */
	public void addClientHandler(ClientHandler handler){
		clientHandlers.add(handler);
	}
	
	/**
	 * Request connection handler loop to terminate
	 */
	public void requestTerminate(){
		terminateRequested = true;
	}
	
	/**
	 * listens for incoming client connections and assign them handlers
	 */
	public void handleConnections(){
		while(!terminateRequested){
			try{
				Socket clientSocket = socket.accept();
				ClientHandler handler = new ClientHandler(new Client(clientSocket), this);
				addClientHandler(handler);
				handlerExecutor.submit(handler);
			}
			catch(InterruptedIOException e){
				continue;
			}
			catch(IOException e){
				break;
			}
			
		}
	}
	

}
