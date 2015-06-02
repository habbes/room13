package room13.message;
import room13.message.messages.*;


public class MessageBuilder {
	
	
	
	public static Message build(RawMessage msg){
		int id = msg.getMsgId();
		switch(id){
		case Message.ERROR:
			return new ErrorMessage(msg);
		case Message.OK:
			return new OkMessage(msg);
		case Message.EVENT:
			//create appropriate EventMessage subclass based on Event Type
			switch(msg.getValue(0)){
			case EventMessage.USER_DISCONNECTED:
				return new UserDisconnectedEventMessage(msg);
			case EventMessage.USER_JOINED:
				return new UserJoinedEventMessage(msg);
			case EventMessage.USER_LEFT:
				return new UserLeftEventMessage(msg);
			case EventMessage.USER_NAME_CHANGED:
				return new UserNameChangedEventMessage(msg);
			default:
				return new EventMessage(msg);
			}
			
		case Message.KEEP_ALIVE:
			return new KeepAliveMessage(msg);
		case Message.DISCONNECT:
			return new DisconnectMessage(msg);
		case Message.ROOMS:
			return new RoomsMessage(msg);
		case Message.ROOM_LIST:
			return new RoomListMessage(msg);
		case Message.NEW_ROOM:
			return new NewRoomMessage(msg);
		case Message.JOIN_ROOM:
			return new JoinRoomMessage(msg);
		case Message.LEAVE_ROOM:
			return new LeaveRoomMessage(msg);
		case Message.SEND:
			return new SendMessage(msg);
		case Message.BROADCAST:
			return new BroadcastMessage(msg);
		case Message.NAME:
			return new NameMessage(msg);
		case Message.MEMBERS:
			return new MembersMessage(msg);
		case Message.MEMBERS_LIST:
			return new MembersListMessage(msg);
		default:
			throw new InvalidMessageException("Unknown message type.");

		}
		
	}

}
