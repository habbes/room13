package room13.message.messages;

import room13.message.*;

/**
 * Request for the list of members in a room
 * @author Habbes
 *
 */
public class MembersMessage extends BaseRoomMessage {

	public MembersMessage() {
		super();
	}

	public MembersMessage(String room) {
		super(room);
	}

	public MembersMessage(RawMessage rm) {
		super(rm);
	}

	@Override
	public int getMsgId() {
		return Message.MEMBERS;
	}

}
