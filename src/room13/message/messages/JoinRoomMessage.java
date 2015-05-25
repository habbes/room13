package room13.message.messages;

import room13.message.*;
import room13.message.Message;
import room13.message.RawMessage;

public class JoinRoomMessage extends Message {
	
	private String roomName;
	private String roomPassword;

	public JoinRoomMessage() {
		this("", null);
	}
	
	public JoinRoomMessage(String name){
		super();
		setRoomName(name);
	}
	
	public JoinRoomMessage(String name, String password){
		this(name);
		setRoomPassword(password);
	}

	public JoinRoomMessage(RawMessage rm) {
		super(rm);
		roomName = rm.getValue(0);
		if(roomName == null){
			throw new InvalidMessageException("Room name not specified.");
		}
		roomPassword = rm.getValue(1);
	}
	
	public void setRoomName(String name){
		roomName = name;
		raw.setValue(0, name);
	}
	
	public String getRoomName(){
		return roomName;
	}
	
	public void setRoomPassword(String password){
		roomPassword = password;
		raw.setValue(1, password);
	}
	
	public String getRoomPassword(){
		return roomPassword;
	}

	@Override
	public int getMsgId() {
		return Message.JOIN_ROOM;
	}

}
