package room13.message.messages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import room13.message.*;

public class MembersListMessage extends BaseRoomMessage {
	
	private List<String> membersList;
	private boolean membersEncoded = false;

	public MembersListMessage() {
		this(null, 0);
	}

	public MembersListMessage(String room) {
		super(room);
		setRespId(0);
		membersList = new ArrayList<String>();
	}
	
	/**
	 * 
	 * @param room
	 * @param reqId the id of the MembersMessage that request this list
	 */
	public MembersListMessage(String room, int reqId){
		this(room);
		setRespId(reqId);
	}

	public MembersListMessage(RawMessage rm) {
		super(rm);
		parseBody();
	}

	@Override
	public int getMsgId() {
		return Message.MEMBERS_LIST;
	}
	
	public void setMembers(List<String> list){
		membersList = list;
		membersEncoded = false;
	}
	
	public void addMember(String member){
		membersList.add(member);
		membersEncoded = false;
	}
	
	public void setMember(int pos, String member){
		membersList.set(pos, member);
		membersEncoded = false;
	}
	
	public List<String> getMembers(){
		return membersList;
	}
	
	public String getMember(int pos){
		return membersList.get(pos);
	}
	
	/**
	 * decodes the message body and retrieves the roomList
	 */
	private void parseBody(){
		try {
			membersList = BodyCoder.decodeSimpleList(raw.getBody());
			membersEncoded = true;
		}
		catch(IOException e){}
		
	}
	
	/**
	 * encodes the roomList in the Message body
	 */
	private void encodeMembers(){
		try {
			raw.setBody(BodyCoder.encodeSimpleList(membersList));
			membersEncoded = true;
		} catch (IOException e) {}
	}
	
	private void checkMembersEncoded(){
		if(!membersEncoded){
			encodeMembers();
		}
	}
	
	@Override
	public RawMessage getRaw(){
		checkMembersEncoded();
		return super.getRaw();
	}
	
	@Override
	public byte[] encode(){
		checkMembersEncoded();
		return super.encode();
	}

}
