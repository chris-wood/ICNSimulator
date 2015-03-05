package ccn.message;

public class InvalidMessageException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public InvalidMessageException(String msg) {
		super(msg);
	}

}
