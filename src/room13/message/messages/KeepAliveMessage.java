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
		super();
	}

	public KeepAliveMessage(RawMessage rm) {
		super(rm);
	}

	@Override
	public int getMsgId() {
		return Message.KEEP_ALIVE;
	}

}
