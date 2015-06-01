package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Request to leave a room
 * @author Habbes
 *
 */
public class LeaveRoomMessage extends BaseRoomMessage {

	public LeaveRoomMessage() {
		super();
	}
	
	public LeaveRoomMessage(String room){
		super(room);
	}

	public LeaveRoomMessage(RawMessage rm) {
		super(rm);
	}
	
	
	@Override
	public int getMsgId() {
		return Message.LEAVE_ROOM;
	}

}
