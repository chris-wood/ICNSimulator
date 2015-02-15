package ccn.message;

public class ContentObject extends Message {

	public ContentObject(String name) {
		super(name);
	}

	@Override
	public String toString() {
		return "CONTENT[" + name + "]";
	}
}
