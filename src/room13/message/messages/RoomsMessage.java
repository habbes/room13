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
		super();
	}

	public RoomsMessage(RawMessage rm) {
		super(rm);
	}

	@Override
	public int getMsgId() {
		return Message.ROOMS;
	}

}
