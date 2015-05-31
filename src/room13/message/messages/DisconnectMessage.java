package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Request notifying server of client's disconnection
 * @author Habbes
 *
 */
public class DisconnectMessage extends Message {

	public DisconnectMessage() {
		super();
	}

	public DisconnectMessage(RawMessage rm) {
		super(rm);
	}

	@Override
	public int getMsgId() {
		return Message.DISCONNECT;
	}

}
