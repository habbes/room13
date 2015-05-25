package room13.message;



public class MessageBuilder {
	
	
	
	public static Message build(RawMessage msg){
		int id = msg.getMsgId();
		switch(id){
		case Message.ERROR:
			return new room13.message.messages.ErrorMessage(msg);
		case Message.OK:
			return new room13.message.messages.OkMessage(msg);
		case Message.KEEP_ALIVE:
			return new room13.message.messages.KeepAliveMessage(msg);
		case Message.DISCONNECT:
			return new room13.message.messages.DisconnectMessage(msg);
		case Message.ROOMS:
			return new room13.message.messages.RoomsMessage(msg);
		case Message.ROOM_LIST:
			return new room13.message.messages.RoomListMessage(msg);
		case Message.NEW_ROOM:
			return new room13.message.messages.NewRoomMessage(msg);
		case Message.JOIN_ROOM:
			return new room13.message.messages.JoinRoomMessage(msg);
		case Message.LEAVE_ROOM:
			return new room13.message.messages.LeaveRoomMessage(msg);
		case Message.SEND:
			return new room13.message.messages.SendMessage(msg);
		case Message.BROADCAST:
			return new room13.message.messages.BroadcastMessage(msg);
		case Message.NAME:
			return new room13.message.messages.NameMessage(msg);
		case Message.MEMBERS:
			return new room13.message.messages.MembersMessage(msg);
		case Message.MEMBERS_LIST:
			return new room13.message.messages.MembersListMessage(msg);
		default:
			throw new InvalidMessageException("Unknown message type.");

		}
		return null;
		
	}

}
