/**
 * Clément Habinshuti
 * Started: 24.10.2014
 */
package room13.message;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RawMessage {
	
	private byte msgId;
	private byte reqId;
	private byte respId;
	private byte[] body;
	private List<String> valueParams;
	private Map<String, String> dictParams;
	private int contentLength;
	
	/**
	 * the size of the part of the headers that include the content length and the req, resp and msg IDs
	 */
	public static final int PREFIX_LENGTH = 7;
	/**
	 * the size of the part of the headers that include the req, resp and msf ids
	 */
	public static final int IDS_LENGTH = 3;
	public static final String CHARSET = "US-ASCII";
	
	//error messages
	private static final String LENGTH_ERROR = "Could not correctly read message based on specified length";
	
	public int getMsgId(){
		return msgId;
	}
	
	public void setMsgId(int id){
		msgId = (byte) id;
	}
	
	public int getReqId(){
		return reqId;
	}
	
	public void setReqId(int id){
		reqId = (byte) id;
	}
	
	public int getRespId(){
		return respId;
	}
	
	public void setRespId(int id){
		respId = (byte) id;
	}
	
	public byte[] getBody(){
		return body;
	}
	
	public void setBody(byte[] b){
		body = b;
	}
	
	public List<String> getValueParams(){
		return valueParams;
	}
	
	public void addValue(String val){
		valueParams.add(val);
	}
	
	public void setValue(int pos, String val){
		valueParams.set(pos, val);
	}
	
	public String getValue(int pos){
		try{
			return valueParams.get(pos);
		}
		catch (IndexOutOfBoundsException e){
			return null;
		}
	}
	
	public Map<String, String> getDictParams(){
		return dictParams;
	}
	
	public void setDictValue(String key, String value){
		dictParams.put(key, value);
	}
	
	public String getDictValue(String key){
		return dictParams.get(key);
	}
	
	
	public RawMessage(){
		valueParams = new ArrayList<String>();
		dictParams = new HashMap<String, String>();
	}
	
	/**
	 * decode byte message
	 * @param encoded bytes composing message
	 * @return raw message containing the information encoded in the message
	 */
	public static RawMessage decode(byte[] encoded){
		RawMessage msg = new RawMessage();
		msg.reqId = encoded[0];
		msg.respId = encoded[1];
		msg.msgId = encoded[2];
		
		//read params
		int paramLength;
		int pos = 3;
		while(pos < encoded.length && encoded[pos] != 0){
			paramLength = encoded[pos];
			if(paramLength > 0){
				//read value param
				pos++;
				String param = "";
				try {
					param = new String(encoded, pos, paramLength, CHARSET);
					msg.contentLength += 1 + param.length();
				} catch (UnsupportedEncodingException e){
					//required
				} catch (IndexOutOfBoundsException e ){
					throw new InvalidMessageFormatException(LENGTH_ERROR, e);
				}
				msg.valueParams.add(param);
				pos = pos + paramLength;
				
			}
			else {
				//read key/value param
				paramLength = -paramLength;
				pos++;
				String key = "";
				try {
					key = new String(encoded, pos, paramLength, CHARSET);
					msg.contentLength += 1 + key.length();
				
				} catch (IndexOutOfBoundsException e){
					throw new InvalidMessageFormatException(LENGTH_ERROR,e);
				} catch (UnsupportedEncodingException e) {
					//required
				}
				pos = pos + paramLength;
				paramLength = encoded[pos];
				pos++;
				String val = "";
				try {
					val = new String(encoded, pos, paramLength, CHARSET);
					msg.contentLength += 1 + val.length();
				
				} catch (IndexOutOfBoundsException e){
					throw new RuntimeException("", e);
					
				} catch (UnsupportedEncodingException e) {
					//required
				}
			
				pos = pos + paramLength;
				msg.dictParams.put(key, val);
			}
		}
		
		//read body
		if(pos < encoded.length){
			pos++;
			try {
				msg.body = Arrays.copyOfRange(encoded, pos, encoded.length);
				msg.contentLength += 1 + msg.body.length;
			} catch(Exception e){
				throw new InvalidMessageFormatException(LENGTH_ERROR,e);
			}
			
		}

		return msg;
	}
	
	/**
	 * encode message into byte format suitable for transport
	 * @return the encoded message
	 */
	public byte[] encode(){
		
		int totalLen = PREFIX_LENGTH + contentLength;
		
		byte[] msg = new byte[totalLen];
		
		//prefix the message with the content length
		ByteBuffer lenBuffer = ByteBuffer.allocate(4);
		lenBuffer.order(ByteOrder.BIG_ENDIAN);
		lenBuffer.putInt(contentLength);
		for(int i = 0; i < 4; i++){
			msg[i] = lenBuffer.get(i);
		}
		msg[4] = reqId;
		msg[5] = respId;
		msg[6] = msgId;
		int pos = 7;
		for(String param : valueParams){
			msg[pos++] = (byte) param.length();
			for(char c : param.toCharArray()){
				msg[pos++] = (byte) c;
			}
		}
		
		String value;
		for(String key : dictParams.keySet()){
			value = dictParams.get(key);
			msg[pos++] = (byte) (- key.length());
			for(char c : key.toCharArray()){
				msg[pos++] = (byte) c;
			}
			msg[pos++] = (byte) value.length();
			for(char c : value.toCharArray()){
				msg[pos++] = (byte) c;
			}
		}
		
		if(body != null && body.length != 0){
			msg[pos++] = (byte) 0;
			for(byte b : body){
				msg[pos++] = b;
			}
		}
		
		return msg;
	}
}
