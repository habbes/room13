package room13.message;

/**
 * Exception thrown when a message does not adhere to the general message structure of the protocol
 * @author Habbes
 *
 */
public class InvalidMessageFormatException extends MessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7776311877921074711L;
	
	public InvalidMessageFormatException(){
		super();
	}
	
	public InvalidMessageFormatException(String message){
		super(message);
	}
	
	public InvalidMessageFormatException(String message, Throwable cause){
		super(message, cause);
	}
	

}
