package room13.message.messages;

import room13.message.RawMessage;

/**
 * Event message notifying that a user has been disconnected
 * @author Habbes
 *
 */
public class UserDisconnectedEventMessage extends UserEventMessage {

	public UserDisconnectedEventMessage(String user) {
		super(EventMessage.USER_DISCONNECTED, user);
	}

	public UserDisconnectedEventMessage() {
		this("");
	}

	public UserDisconnectedEventMessage(RawMessage rm) {
		super(rm);
	}

}
