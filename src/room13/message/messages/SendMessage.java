package room13.message.messages;


import room13.message.BodyCoder;
import room13.message.InvalidMessageException;
import room13.message.Message;
import room13.message.RawMessage;

/**
 * A user-defined message sent to a particular recipient in a room
 * @author Habbes
 *
 */
public class SendMessage extends BaseRoomMessage {
	
	private String message;
	private String sender;
	private String recipient;

	public SendMessage() {
		super();
	}
	
	public SendMessage(String room){
		this(room, "");
	}
	
	public SendMessage(String room, String recipient){
		super(room);
		this.recipient = recipient;
		raw.addValue(recipient);
		setSender("");
	}
	

	public SendMessage(RawMessage rm) {
		super(rm);
		recipient = rm.getValue(0);
		if(recipient == null || recipient.isEmpty()){
			throw new InvalidMessageException("Recipient not specified.");
		}
		sender = rm.getDictValue("sender");
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
	 * sets the recipient of the message, this should be set at the client
	 * @return the user name of the recipient
	 */
	public String getRecipient(){
		return recipient;
	}
	
	/**
	 * sets the sender of the message, this should be set at the server
	 * @param s the user name of the sender
	 */
	public void setSender(String s){
		sender = s;
		raw.setDictValue("sender", s);
	}
	
	public String getSender(){
		return sender;
	}
	
	public void setMessage(String m){
		message = m;
		raw.setBody(BodyCoder.encodeText(m));
	}
	
	public String getMessage(){
		return message;
	}

}
