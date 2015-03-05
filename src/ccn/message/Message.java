package ccn.message;

import dispatch.event.Event;

public class Message extends Event {

	protected MessageHeader header;
	protected MessagePayload payload;
	protected MessageValidationInfo validationInfo;
	
	public Message(String name) {
		header = new MessageHeader(name, Integer.MAX_VALUE);
		payload = new MessagePayload();
		validationInfo = new MessageValidationInfo();
	}
	
	public Message(String name, int hopCount) {
		header = new MessageHeader(name, hopCount);
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
	
	public int getSizeInBits() {
		return header.getSizeInBits() + payload.getSizeInBits() + validationInfo.getSizeInBits();
	}
	
	public int getSizeInBytes() {
		int bitLength = getSizeInBits();
		if (bitLength % 8 == 0) {
			return bitLength / 8;
		} else {
			return (bitLength / 8) + 1;
		}
	}
	
}
