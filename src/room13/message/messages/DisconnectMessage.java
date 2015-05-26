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
		// TODO Auto-generated constructor stub
	}

	public DisconnectMessage(RawMessage rm) {
		super(rm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getMsgId() {
		// TODO Auto-generated method stub
		return Message.DISCONNECT;
	}

}
