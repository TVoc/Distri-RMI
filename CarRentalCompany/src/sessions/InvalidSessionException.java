package sessions;

public class InvalidSessionException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6777630509898530388L;

	public InvalidSessionException() {
		super();
	}
	
	public InvalidSessionException(String message) {
		super(message);
	}

}
