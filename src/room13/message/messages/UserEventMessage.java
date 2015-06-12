package room13.message.messages;

import room13.message.RawMessage;

/**
 * Base Message for events that notify something about a specific user in a room
 * @author Habbes
 *
 */
public class UserEventMessage extends EventMessage {

	private String user;
	
	public UserEventMessage(String type, String user){
		super(type);
		//user is second value param
		raw.addValue(user); //TODO should it be a Key/Val instead
		this.user = user;
	}
	
	public UserEventMessage(String type) {
		this(type, "");
	}

	public UserEventMessage() {
		super();
	}

	public UserEventMessage(RawMessage rm) {
		super(rm);
		user = rm.getValue(1);
	}
	
	public void setUser(String user){
		this.user = user;
		raw.setValue(1, user);
	}
	
	public String getUser(){
		return user;
	}

}
