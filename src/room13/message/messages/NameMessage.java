package room13.message.messages;

import room13.message.*;

/**
 * Request to set a user's name in a room
 * @author Habbes
 *
 */
public class NameMessage extends BaseRoomMessage {

	private String name;
	
	public NameMessage() {
		super();
	}

	public NameMessage(String room) {
		super(room);
	}
	
	public NameMessage(String room, String name){
		this(room);
		this.name = name;
		raw.addValue(name);
	}

	public NameMessage(RawMessage rm) {
		super(rm);
		name = rm.getValue(0);
		if(name == null || name == ""){
			throw new InvalidMessageException("Name not specified");
		}
	}

	@Override
	public int getMsgId() {
		return Message.NAME;
	}
	
	public void setName(String n){
		name = n;
		raw.setValue(0, n);
	}
	
	public String getName(){
		return name;
	}

}
