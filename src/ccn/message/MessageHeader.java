package ccn.message;

public class MessageHeader {
	
	private String name;
	private int hopCount;
	
	public MessageHeader(String name, int hopCount) {
		this.name = name;
		this.hopCount = hopCount;
	}
	
	public String getName() {
		return name;
	}

}
