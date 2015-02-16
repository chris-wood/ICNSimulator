package ccn.message;

import java.util.Arrays;

public class MessageValidationInfo {
	
	private byte[] data;
	
	public MessageValidationInfo(byte[] inputData) {
		this.data = Arrays.copyOf(inputData, inputData.length);
	}

}
