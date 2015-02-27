package ccn.message;

public class ContentObject extends Message {

	public ContentObject(String name, byte[] data) {
		super(name);
		super.payload = new MessagePayload(data);
	}

	@Override
	public String toString() {
		return "CONTENT[" + header.getName() + "]";
	}
}
