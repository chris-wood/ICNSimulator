package ccn.message;

import java.util.Arrays;

public class MessagePayload {
	
	private byte[] data;
	 
	public MessagePayload(byte[] inputData) {
		this.data = Arrays.copyOf(inputData, inputData.length);
	}
	
	public byte[] getBytes() {
		return data;
	}

}
