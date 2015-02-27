package ccn.message;

public class NACK extends Message {
	
	protected String error;

	public NACK(String name, String errorMessage) {
		super(name);
		error = errorMessage;
	}
	
	public String getError() {
		return error;
	}

	@Override
	public String toString() {
		return "NACK[" + header.getName() + "]";
	}
}
