package ccn.message;

import dispatch.event.Event;

public class Message extends Event {

	protected MessageHeader header;
	protected MessagePayload payload;
	protected MessageValidationInfo validationInfo;
	
	public Message(String name) {
		this.header = new MessageHeader(name, Integer.MAX_VALUE);
	}
	
	public Message(String name, int hopCount) {
		this.header = new MessageHeader(name, hopCount);
	}
	
	public String getName() {
		return header.getName();
	}
	
	public byte[] getPayload() {
		return payload.getBytes();
	}
	
	public byte[] getValidationInfo() {
		return validationInfo.getData();
	}
	
	public int getSize() {
		return 1;
//		return header.getSize() + payload.getSize() + validationInfo.getSize();
	}
	
}
