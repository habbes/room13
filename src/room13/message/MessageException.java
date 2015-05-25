package room13.message;

/**
 * exception thrown when an issue with reading or decoding a message occurs
 * @author Habbes
 *
 */
public class MessageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7269954866575451619L;

	public MessageException() {
		// TODO Auto-generated constructor stub
	}

	public MessageException(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MessageException(Throwable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public MessageException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public MessageException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

}
