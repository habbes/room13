package room13.message;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * writes a raw message to an output stream
 * @author Habbes
 *
 */
public class MessageWriter {
	
	private OutputStream stream;
	
	/**
	 * creates message writer on the given output stream with optional buffering if needed.
	 * @param s the output stream to which the message is written
	 * @param buffer whether or not to buffer writes in case the stream is not already buffered.
	 */
	public MessageWriter(OutputStream s, boolean buffer){
		if(buffer){
			s = new BufferedOutputStream(s);
		}
		stream = s;
	}
	
	/**
	 * creates message writer on the given output stream.
	 * No buffering is added, so the stream should ideally already be buffered.
	 * @param s the output stream to which the message is written
	 */
	public MessageWriter(OutputStream s){
		this(s, true);
	}
	
	/**
	 * write a message to the stream
	 * @param msg
	 * @throws IOException
	 */
	public void write(RawMessage msg) throws IOException{
		byte[] encoded = msg.encode();
		stream.write(encoded);
		stream.flush();
	}
	
	public void write(Message msg) throws IOException {
		write(msg.getRaw());
	}
	
	/**
	 * close the underlying stream
	 * @throws IOException
	 */
	public void close() throws IOException{
		stream.close();
	}
}
