package room13.message;

/**
 * exception thrown when a message of unknown type has been encountered i.e. one with an unknown message id
 * @author Habbes
 *
 */
public class UnknownMessageException extends MessageException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8118654265198169767L;

	public UnknownMessageException() {
		// TODO Auto-generated constructor stub
	}

	public UnknownMessageException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public UnknownMessageException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownMessageException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UnknownMessageException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
