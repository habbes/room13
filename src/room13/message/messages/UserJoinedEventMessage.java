package room13.message.messages;

import room13.message.RawMessage;

/**
 * Event message notifying that a user has joined the room
 * @author Habbes
 *
 */
public class UserJoinedEventMessage extends UserEventMessage {

	public UserJoinedEventMessage(String user) {
		super(EventMessage.USER_JOINED, user);
	}

	public UserJoinedEventMessage() {
		this("");
	}

	public UserJoinedEventMessage(RawMessage rm) {
		super(rm);
	}

}
