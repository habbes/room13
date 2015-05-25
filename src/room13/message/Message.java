package room13.message;

public abstract class Message {
	
	protected RawMessage raw;
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	//ids for the different types of messages
	public static final int ERROR = 1;
	public static final int OK = 2;
	public static final int KEEP_ALIVE = 3;
	public static final int DISCONNECT = 4;
	public static final int ROOMS = 5;
	public static final int ROOM_LIST = 6;
	public static final int NEW_ROOM =7;
	public static final int JOIN_ROOM = 8;
	public static final int LEAVE_ROOM = 9;
	public static final int SEND = 10;
	public static final int BROADCAST = 11;
	public static final int NAME = 12;
	public static final int MEMBERS = 13;
	public static final int MEMBERS_LIST = 14;

	
	public Message(){
		raw = new RawMessage();
		setMsgId(getMsgId());
	}
	
	public Message(RawMessage rm){
		raw = rm;
	}
	
	/**
	 * get the inner raw message
	 * @return
	 */
	public RawMessage getRaw(){
		return raw;
	}
	
	abstract public int getMsgId();
	
	protected void setMsgId(int id){
		raw.setMsgId(id);
	}
	
	public int getReqId(){
		return raw.getReqId();
	}
	
	public void setReqId(int id){
		raw.setReqId(id);
	}
	
	public int getRespId(){
		return raw.getRespId();
	}
	
	public void setRespId(int id){
		raw.setRespId(id);
	}
	
	/**
	 * encode the message in byte form suitable for transport
	 * @return
	 */
	public byte[] encode(){
		return raw.encode();
	}
	
}
