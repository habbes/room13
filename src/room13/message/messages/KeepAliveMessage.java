package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Message sent to keep connection alive despite inactivity
 * @author Habbes
 *
 */
public class KeepAliveMessage extends Message {

	public KeepAliveMessage() {
		// TODO Auto-generated constructor stub
	}

	public KeepAliveMessage(RawMessage rm) {
		super(rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMsgId() {
		// TODO Auto-generated method stub
		return Message.KEEP_ALIVE;
	}

}
