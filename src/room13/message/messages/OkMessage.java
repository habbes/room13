package room13.message.messages;

import room13.message.Message;
import room13.message.RawMessage;

/**
 * Response message sent after a successful request
 * @author Habbes
 *
 */
public class OkMessage extends Message {

	public OkMessage() {
		this(0);
	}
	
	/**
	 * 
	 * @param reqId id of the message to which this is the response
	 */
	public OkMessage(int reqId){
		super();
		setRespId(reqId);
	}

	public OkMessage(RawMessage rm) {
		super(rm);
	}

	@Override
	public int getMsgId() {
		return Message.OK;
	}

}
