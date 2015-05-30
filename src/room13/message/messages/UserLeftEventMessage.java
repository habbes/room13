package room13.message.messages;

import room13.message.RawMessage;

/**
 * Event message notifying that a user has left a room
 * @author Habbes
 *
 */
public class UserLeftEventMessage extends UserEventMessage {

	public UserLeftEventMessage(String user) {
		super(EventMessage.USER_LEFT);
	}

	public UserLeftEventMessage() {
		this("");
	}

	public UserLeftEventMessage(RawMessage rm) {
		super(rm);
	}

}
