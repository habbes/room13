package room13.message;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * utility class for encoding and decoding standard collections of items stored in a Message's body
 * @author Habbes
 *
 */
public class BodyCoder {

	private BodyCoder() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * decode an encoded simple linear list (single column) of items stored
	 * in a Message body
	 * @param message the encoded body of the Message
	 * @return a List containing the decoded items
	 * @throws IOException
	 */
	public static List<String> decodeSimpleList(byte[] message) throws IOException {
		List<String> list = new ArrayList<String>();
		
		InputStream reader = new ByteArrayInputStream(message);
		
		//each element in the list is preceded by its length
		int length = reader.read();
		byte[] buffer;
		while(length != -1){
			//read next element and store in in the buffer
			buffer = new byte[length];
			reader.read(buffer);
			//store buffer item in the list
			list.add(new String(buffer, Message.DEFAULT_CHARSET));
			
			length = reader.read();
		}
		
		return list;
		
	}
	
	/**
	 * encode a simple linear list (single-column) to a byte array that can
	 * be stored as a Message's body
	 * @param list the list of items to encode
	 * @return the encoded data
	 * @throws IOException
	 */
	public static byte[] encodeSimpleList(List<String> list) throws IOException {
		List<Byte> data = new ArrayList<Byte>();
		byte[] buffer;
		for(String item : list){
			
			buffer = item.getBytes(Message.DEFAULT_CHARSET);
			
			//precede each item by its length
			data.add((byte) buffer.length);
			//copy each character of the item
			
			for(byte b : buffer){
				data.add(b);
			}
		}
		
		//copy to byte array
		int count = data.size();
		byte[] encoded = new byte[count];
		
		for(int i = 0; i < count; ++i){
			encoded[i] = data.get(i);
		}
		
		return encoded;
		
	}
	
	public static String decodeText(byte[] data){
		String decoded = null;
		try {
			decoded = new String(data, Message.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {}
		return decoded;
	}
	
	public static byte[] encodeText(String text){
		byte[] encoded = null;
		try {
			encoded = text.getBytes(Message.DEFAULT_CHARSET);
		} catch (UnsupportedEncodingException e) {}
		
		return encoded;
	}

}
