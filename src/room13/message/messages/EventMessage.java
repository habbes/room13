package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Generic message to sent to notify users of an event,
 * this is the base of more specific event messages
 * @author Habbes
 *
 */
public class EventMessage extends Message {
	
	private String type;
	
	public static final String USER_JOINED = "UsrJoined";
	public static final String USER_LEFT = "UsrLeft";
	public static final String USER_DISCONNECTED = "UsrDisc";
	public static final String USER_NAME_CHANGED = "UserName";
		
	public EventMessage(String type){
		super();
		this.type = type;
		raw.addValue(type);
	}
	
	public EventMessage() {
		this("");
	}

	public EventMessage(RawMessage rm) {
		super(rm);
		type = rm.getValue(0);
	}

	@Override
	public int getMsgId() {
		return Message.EVENT;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String t){
		type = t;
		raw.setValue(0, t);
	}

}
