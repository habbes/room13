package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Message request for the list of rooms at the server
 * @author Habbes
 *
 */
public class RoomsMessage extends Message {

	public RoomsMessage() {
		// TODO Auto-generated constructor stub
	}

	public RoomsMessage(RawMessage rm) {
		super(rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMsgId() {
		// TODO Auto-generated method stub
		return Message.ROOMS;
	}

}
