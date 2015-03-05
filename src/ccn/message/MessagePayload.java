package ccn.message;

import java.util.Arrays;

public class MessagePayload {
	
	private byte[] data;
	
	public MessagePayload() {
		data = new byte[0];
	}
	 
	public MessagePayload(byte[] inputData) {
		this.data = Arrays.copyOf(inputData, inputData.length);
	}
	
	public byte[] getBytes() {
		return data;
	}
	
	public int getSizeInBits() {
		return data.length * 8;
	}

}
