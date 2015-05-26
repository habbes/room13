package room13.message.messages;

import room13.message.InvalidMessageException;
import room13.message.Message;
import room13.message.RawMessage;

/**
 * Base class for Messages that are directed to a particular Room on a server
 * @author Habbes
 *
 */
public abstract class BaseRoomMessage extends Message {
	
	protected String roomName;

	public BaseRoomMessage() {
		super();
	}
	
	public BaseRoomMessage(String room){
		super();
		setRoomName(room);
	}


	public BaseRoomMessage(RawMessage rm) {
		super(rm);
		roomName = rm.getDictValue("room");
		if(roomName == null || roomName ==""){
			throw new InvalidMessageException("Room not specified.");
		}
	}

	public void setRoomName(String name){
		roomName = name;
		raw.setDictValue("room", name);
	}
	
	public String getRoomName(){
		return roomName;
	}

}
