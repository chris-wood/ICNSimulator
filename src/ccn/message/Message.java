package ccn.message;

import framework.Event;

public class Message extends Event {

	protected MessageHeader header;
	protected MessagePayload payload;
	
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
	
}
