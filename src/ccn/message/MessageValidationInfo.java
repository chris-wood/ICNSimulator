package ccn.message;

import java.util.Arrays;

public class MessageValidationInfo {
	
	private byte[] data;
	
	public MessageValidationInfo() {
		data = new byte[0];
	}
	
	public MessageValidationInfo(byte[] inputData) {
		data = Arrays.copyOf(inputData, inputData.length);
	}
	
	public byte[] getData() {
		return data;
	}

	public int getSizeInBits() {
		return data.length * 8;
	}
}
