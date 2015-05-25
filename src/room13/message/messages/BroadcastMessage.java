package room13.message.messages;

import room13.message.BodyCoder;
import room13.message.Message;
import room13.message.RawMessage;

public class BroadcastMessage extends BaseRoomMessage {

	public String message;
	
	public BroadcastMessage() {
		super();
	}

	public BroadcastMessage(String room) {
		super(room);
	}

	public BroadcastMessage(RawMessage rm) {
		super(rm);
		message = BodyCoder.decodeText(rm.getBody());
	}

	@Override
	public int getMsgId() {
		return Message.BROADCAST;
	}
	
	public void setMessage(String m){
		message = m;
		raw.setBody(BodyCoder.encodeText(m));
	}
	
	public String getMessage(){
		return message;
	}

}
