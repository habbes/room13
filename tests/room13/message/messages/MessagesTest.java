package room13.message.messages;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

import room13.message.*;
import room13.message.messages.*;

public class MessagesTest {
	
	public Message sendReceive(Message input){
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		MessageWriter writer = new MessageWriter(outStream);
		try {
			writer.write(input);
		} catch (IOException e) {}
		
		byte[] sent = outStream.toByteArray();
		
		ByteArrayInputStream inStream = new ByteArrayInputStream(sent);
		MessageReader reader = new MessageReader(inStream);
		
		Message output = null;
		try {
			output = MessageBuilder.build(reader.read());
		} catch (IOException | RemoteConnectionClosedException e) {}
		
		return output;
	}

	@Test
	public void testBroadcastMessage() {
		String room = "room";
		String body = "Hello";
		BroadcastMessage input = new BroadcastMessage(room);
		input.setMessage(body);
		
		BroadcastMessage output = (BroadcastMessage) sendReceive(input);
		assertEquals("Room preserved", room, output.getRoomName());
		assertEquals("Body preserved", body, output.getMessage());
	}
	
	@Test
	public void testDisconnectMessage(){
		DisconnectMessage input = new DisconnectMessage();
		DisconnectMessage output = (DisconnectMessage) sendReceive(input);
		assertEquals("MsgId Preserved", Message.DISCONNECT, output.getMsgId());
	}
	
	@Test
	public void testErrorMessage(){
		String type = ErrorMessage.USER_NAME_UNAVAILABLE;
		String errMsg = "Error";
		int reqId = 10;
		ErrorMessage input = new ErrorMessage(reqId,type);
		input.setMessage(errMsg);
		ErrorMessage output = (ErrorMessage) sendReceive(input);
		assertEquals("RespId properly set", reqId, output.getRespId());
		assertEquals("Type preserved", type, output.getType());
		assertEquals("Message preserved", errMsg, output.getMessage());
	}
	
	@Test
	public void testJoinRoomMessage(){
		String room = "room";
		String pass = "pass";
		JoinRoomMessage input = new JoinRoomMessage(room, pass);
		JoinRoomMessage output = (JoinRoomMessage) sendReceive(input);
		assertEquals("Room preserved", room, output.getRoomName());
		assertEquals("Pass preserved", pass, output.getRoomPassword());
	}
	
	@Test
	public void testKeepAliveMessage(){
		KeepAliveMessage input = new KeepAliveMessage();
		KeepAliveMessage output = (KeepAliveMessage) sendReceive(input);
		assertEquals("MsgId Preserved: ", Message.KEEP_ALIVE, output.getMsgId());
	}
	
	@Test
	public void testLeaveRoomMessage(){
		String room = "room";
		LeaveRoomMessage input = new LeaveRoomMessage(room);
		LeaveRoomMessage output = (LeaveRoomMessage) sendReceive(input);
		assertEquals("Room preserved", room, output.getRoomName());
	}
	
	@Test
	public void testMembersMessage(){
		String room = "room";
		MembersMessage input = new MembersMessage(room);
		MembersMessage output = (MembersMessage) sendReceive(input);
		assertEquals("Room preserved", room, output.getRoomName());
	}
	
	@Test
	public void testMembersListMessage(){
		
		int reqId = 10;
		String room = "room";
		String[] members = {"One","Two","Three"};
		
		MembersListMessage input = new MembersListMessage(room, reqId);
		for(String member : members){
			input.addMember(member);
		}
		MembersListMessage output = (MembersListMessage) sendReceive(input);
	
		assertEquals("RespId properly set", reqId, output.getRespId());
		assertEquals("Room preserved", room, output.getRoomName());
		assertArrayEquals("Members preserved", members, output.getMembers().toArray());
	}
	
	@Test
	public void testNameMessage(){
		String room = "room";
		String name = "name";
		
		NameMessage input = new NameMessage(room, name);
		NameMessage output = (NameMessage) sendReceive(input);
		
		assertEquals("Room preserved", room, output.getRoomName());
		assertEquals("Name preserved", name, output.getName());
	}
	
	@Test
	public void testOKMessage(){
		int reqId = 10;
		OkMessage input = new OkMessage(reqId);
		OkMessage output = (OkMessage) sendReceive(input);
		assertEquals("RespId set properly", reqId, output.getRespId());
	}
	
	@Test
	public void testRoomsMessage(){
		RoomsMessage input = new RoomsMessage();
		RoomsMessage output = (RoomsMessage) sendReceive(input);
		assertEquals("Preserved MsgId", Message.ROOMS, output.getMsgId());
	}
	
	@Test
	public void testRoomListMessage(){
		int reqId = 10;
		String[] rooms = {"One","Two","Three"};
		
		RoomListMessage input = new RoomListMessage(reqId);
		for(String room : rooms){
			input.addRoom(room);
		}
		
		RoomListMessage output = (RoomListMessage) sendReceive(input);
		assertEquals("RespId properly set", reqId, output.getRespId());
		assertArrayEquals("Rooms preserved", rooms, output.getRooms().toArray());
	}
	
	@Test
	public void testSendMessage(){
		String room = "room";
		String recipient = "user";
		String message = "Hello";
		SendMessage input = new SendMessage(room, recipient);
		assertEquals("Room set", room, input.getRoomName());
		input.setMessage(message);
		SendMessage output = (SendMessage) sendReceive(input);
		
		assertEquals("Room preserved", room, output.getRoomName());
		assertEquals("Recipient preserved", recipient, output.getRecipient());
		assertEquals("Message preserved", message, output.getMessage());
		
		//emulate retransmission at server
		String sender = "sender";
		output.setSender(sender);
		
		SendMessage retrans = (SendMessage) sendReceive(output);
		
		assertEquals("Room preserved again", room, output.getRoomName());
		assertEquals("Recipient preserved again", recipient, output.getRecipient());
		assertEquals("Sender preserved", sender, retrans.getSender());
		assertEquals("Message preserved again", message, output.getMessage());
		
	}
	
	@Test
	public void testUserDisconnectedEventMessage(){
		String user = "user";
		UserDisconnectedEventMessage input = new UserDisconnectedEventMessage(user);
		UserDisconnectedEventMessage output = (UserDisconnectedEventMessage) sendReceive(input);
		assertEquals("User preserved", user, output.getUser());
		assertEquals("Event type preserved", EventMessage.USER_DISCONNECTED, output.getType());
	}
	
	@Test
	public void testUserJoinedEventMessage(){
		String user = "user";
		UserJoinedEventMessage input = new UserJoinedEventMessage(user);
		UserJoinedEventMessage output = (UserJoinedEventMessage) sendReceive(input);
		assertEquals("User preserved", user, output.getUser());
		assertEquals("Event type preserved", EventMessage.USER_JOINED, output.getType());
	}
	
	@Test
	public void testUserLeftEventMessage(){
		String user = "user";
		UserLeftEventMessage input = new UserLeftEventMessage(user);
		UserLeftEventMessage output = (UserLeftEventMessage) sendReceive(input);
		assertEquals("User preserved", user, output.getUser());
		assertEquals("Event type preserved", EventMessage.USER_LEFT, output.getType());
	}
	
	@Test
	public void testUserNameChangedEventMessage(){
		String user = "user";
		String name = "name";
		UserNameChangedEventMessage input = new UserNameChangedEventMessage(user, name);
		UserNameChangedEventMessage output = (UserNameChangedEventMessage) sendReceive(input);
		assertEquals("User preserved", user, output.getUser());
		assertEquals("Name preserved", name, output.getName());
		assertEquals("Event type preserved", EventMessage.USER_NAME_CHANGED, output.getType());
	}

}
