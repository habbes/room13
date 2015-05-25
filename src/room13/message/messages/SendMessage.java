package room13.message.messages;


import room13.message.BodyCoder;
import room13.message.InvalidMessageException;
import room13.message.Message;
import room13.message.RawMessage;

public class SendMessage extends BaseRoomMessage {
	
	private String message;
	private String recipient;

	public SendMessage() {
		super();
	}
	
	public SendMessage(String room){
		super(room);
	}
	
	public SendMessage(String room, String recipient){
		super(room);
		setRecipient(recipient);
	}
	

	public SendMessage(RawMessage rm) {
		super(rm);
		recipient = rm.getValue(0);
		if(recipient == null || recipient == ""){
			throw new InvalidMessageException("Recipient not specified.");
		}
		message = BodyCoder.decodeText(rm.getBody());
	}

	@Override
	public int getMsgId() {
		return Message.SEND;
	}
	
	/**
	 * 
	 * @param r the user name of the recipient
	 */
	public void setRecipient(String r){
		recipient = r;
		raw.setValue(0, r);
	}
	
	/**
	 * 
	 * @return the user name of the recipient
	 */
	public String getRecipient(){
		return recipient;
	}
	
	public void setMessage(String m){
		message = m;
		raw.setBody(BodyCoder.encodeText(m));
	}
	
	public String getMessage(){
		return message;
	}

}
