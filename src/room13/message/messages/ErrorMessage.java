package room13.message.messages;

import java.io.UnsupportedEncodingException;
import room13.message.Message;
import room13.message.RawMessage;

/**
 * Response message sent when error occurs
 * @author Habbes
 *
 */
public class ErrorMessage extends Message {

	private String message;
	private String type;
	
	public static final String GENERIC_ERROR = "Err";
	public static final String USER_NOT_FOUND = "UsrNtFnd";
	public static final String USER_NAME_UNAVAILABLE = "NmNtAvai";
	public static final String ROOM_NAME_UNAVAILABLE = "RmNtAvai";
	public static final String ROOM_ACCESS_DENIED = "RmAccDen";
	public static final String ROOM_NOT_FOUND = "RmNtFnd";
	
	public ErrorMessage() {
		
	}
	
	/**
	 * 
	 * @param reqId the id of the message for which this is the response
	 * @param type identifies the type of error
	 */
	public ErrorMessage(int reqId, String type){
		setRespId(reqId);
		setType(type);
	}

	public ErrorMessage(RawMessage rm) {
		super(rm);
		try {
			message = new String(rm.getBody(), Message.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e){}
	}

	@Override
	public int getMsgId() {
		return Message.ERROR;
	}
	
	public void setType(String t){
		type = t;
		raw.setValue(0, t);
	}
	
	public String getType(){
		return type;
	}
	
	public void setMessage(String m){
		message = m;
		try {
			raw.setBody(m.getBytes(Message.DEFAULT_CHARSET));
		} catch (UnsupportedEncodingException e){}
	}
	
	public String getMessage(){
		return message;
	}
	


}
