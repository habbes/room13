package room13.message;

/**
 * exception thrown when the actual length of the message does not match the length specified in the prefix
 * @author Habbes
 *
 */
public class IncorrectMessageLengthException extends InvalidMessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1594872165028965396L;

	public IncorrectMessageLengthException() {
		// TODO Auto-generated constructor stub
	}

	public IncorrectMessageLengthException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IncorrectMessageLengthException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public IncorrectMessageLengthException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public IncorrectMessageLengthException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
