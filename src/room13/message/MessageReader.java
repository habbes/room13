package room13.message;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * reads a raw message from an input stream
 * @author Habbes
 *
 */
public class MessageReader {
	
	private InputStream stream;
	
	/**
	 * creates a new reader on the given input stream
	 * @param s the input stream from which to read messages
	 * @param buffer whether to buffer reads from input, in case its not already buffered
	 */
	public MessageReader(InputStream s, boolean buffer){
		if(buffer){
			s = new BufferedInputStream(s);
		}
		stream = s;
	}
	
	/**
	 * creates a reader on the given input stream.
	 * No buffering is added so the input stream should ideally already be buffered.
	 * @param s the input stream from which to read messages
	 */
	public MessageReader(InputStream s){
		this(s, true);
	}
	
	/**
	 * read a message from the stream
	 * @return the message read
	 * @throws IOException when I/O error occurs while reading the stream
	 * @throws RemoteConnectionClosedException when the stream returns -1
	 */
	public RawMessage read() throws IOException, RemoteConnectionClosedException{
		ByteBuffer lenbuf = ByteBuffer.allocate(4);
		lenbuf.order(ByteOrder.BIG_ENDIAN);
		for(int i = 0; i < 4; i++){
			lenbuf.put((byte)stream.read());
		}
		int length = lenbuf.getInt(0) + RawMessage.IDS_LENGTH;
		
		byte[] encoded = new byte[length];
		
		int bytesRead = stream.read(encoded);
		
		if(bytesRead == -1){
			throw new RemoteConnectionClosedException();
		}
		else if(bytesRead != length)
		{
			throw new IncorrectMessageLengthException("The message is too short");
		}
		
		return RawMessage.decode(encoded);
	}
	
	/**
	 * close the underlying input stream
	 * @throws IOException
	 */
	public void close() throws IOException{
		stream.close();
	}
}
