package ccn.message;

public class VirtualInterest extends Message {

	public VirtualInterest(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return "VirtualInterest[" + header.getName() + "]";
	}

}
