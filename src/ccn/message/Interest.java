package ccn.message;

public class Interest extends Message {

	public Interest(String name) {
		super(name);
	}
	
	@Override
	public String toString() {
		return "INTEREST[" + name + "]";
	}

}
