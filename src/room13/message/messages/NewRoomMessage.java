/**
 * 
 */
package room13.message.messages;

import room13.message.InvalidMessageFormatException;
import room13.message.Message;
import room13.message.RawMessage;

/**
 * Request to create a new room on the server
 * @author Habbes
 *
 */
public class NewRoomMessage extends Message {
	
	String roomName;
	String roomPassword;

	/**
	 * 
	 */
	public NewRoomMessage() {
		this("",null);
	}
	
	/**
	 * 
	 * @param name name of the room
	 */
	public NewRoomMessage(String name){
		super();
		setRoomName(name);
	}
	
	/**
	 * 
	 * @param name name of the room
	 * @param password password of the room, if password is null, the room will be public
	 */
	public NewRoomMessage(String name, String password){
		this(name);
		setRoomPassword(password);
	}

	/**
	 * @param rm
	 */
	public NewRoomMessage(RawMessage rm) {
		super(rm);
		String name = rm.getValue(0);
		if(name == null){
			throw new InvalidMessageFormatException("Room name not specified");
		}
		roomName = name;
		roomPassword = rm.getValue(1);
		
	}

	/* (non-Javadoc)
	 * @see room13.message.Message#getMsgId()
	 */
	@Override
	public int getMsgId() {
		return Message.NEW_ROOM;
	}
	
	/**
	 * 
	 * @param name
	 */
	public void setRoomName(String name){
		roomName = name;
		raw.setValue(0, name);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRoomName(){
		return roomName;
	}
	
	/**
	 * set password of the room, leave unset to create
	 * a public room
	 * @param password
	 */
	public void setRoomPassword(String password){
		roomPassword = password;
		raw.setValue(1, password);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRoomPassword(){
		return roomPassword;
	}


}
