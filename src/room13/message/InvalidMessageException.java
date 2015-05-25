package room13.message;

/**
 * Exception thrown when message does not contain enough info to be fully constructed or used,
 * such as when message does not have required parameters for its type
 * @author Habbes
 *
 */
public class InvalidMessageException extends MessageException {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3684378037056701567L;

	public InvalidMessageException() {
		// TODO Auto-generated constructor stub
	}

	public InvalidMessageException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidMessageException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public InvalidMessageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public InvalidMessageException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
