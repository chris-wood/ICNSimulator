package ccn.message;

public class RIPMessage extends Message {
	
	protected String routeNamePrefix;
	
	public RIPMessage(String name, String prefix) {
		super(name);
		routeNamePrefix = prefix;
	}
	
	public String getPrefix() {
		return routeNamePrefix;
	}

	@Override
	public String toString() {
		return "RIPMessage[" + header.getName() + "]";
	}

}
