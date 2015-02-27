package ccn.message;

public class Interest extends Message {

	public Interest(String name) {
		super(name);
	}
	
	public Interest(String name, byte[] payload) {
		super(name);
		super.payload = new MessagePayload(payload);
	}
	
	@Override
	public String toString() {
		return "INTEREST[" + header.getName() + "]";
	}

}
