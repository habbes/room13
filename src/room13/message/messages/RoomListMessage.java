package room13.message.messages;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import room13.message.Message;
import room13.message.BodyCoder;
import room13.message.RawMessage;

/**
 * Message containing the list of available rooms on the server,
 * sent as a response to a RoomsMessage
 * @author Habbes
 *
 */
public class RoomListMessage extends Message {
	
	private List<String> roomList;
	
	/**
	 * whether the room lists has been encoded into the inner raw message's body
	 * used to prevent re-encoding the list when nothing has been added
	 */
	private boolean roomsEncoded = false;

	public RoomListMessage() {
		this(0);
	}
	
	/**
	 * 
	 * @param reqId id of the RoomsMessage that requested this list
	 */
	public RoomListMessage(int reqId){
		setRespId(reqId);
		roomList = new ArrayList<String>();
		
	}
	
	public RoomListMessage(RawMessage rm) {
		super(rm);
		parseBody();
		
	}

	@Override
	public int getMsgId() {
		return Message.ROOM_LIST;
	}
	
	public void addRoom(String r){
		roomList.add(r);
		roomsEncoded = false;
	}
	
	public String getRoom(int pos){
		return roomList.get(pos);
	}
	
	public List<String> getRooms(){
		return roomList;
	}
	
	public void setRoom(int pos, String r){
		roomList.set(pos, r);
		roomsEncoded = false;
	}
	
	public void setRooms(List<String> rooms){
		roomList = rooms;
		roomsEncoded = false;
	}
	
	/**
	 * decodes the message body and retrieves the roomList
	 */
	private void parseBody(){
		try {
			roomList = BodyCoder.decodeSimpleList(raw.getBody());
			roomsEncoded = true;
		}
		catch(IOException e){}
		
	}
	
	/**
	 * encodes the roomList in the Message body
	 */
	private void encodeRooms(){
		try {
			raw.setBody(BodyCoder.encodeSimpleList(roomList));
			roomsEncoded = true;
		} catch (IOException e) {}
	}
	
	private void checkRoomsEncoded(){
		if(!roomsEncoded){
			encodeRooms();
		}
	}
	
	@Override
	public RawMessage getRaw(){
		checkRoomsEncoded();
		return super.getRaw();
	}
	
	@Override
	public byte[] encode(){
		checkRoomsEncoded();
		return super.encode();
	}

}
