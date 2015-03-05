package ccn.statistics;

import ccn.message.Message;

public class MessageStatisticsContainer {
	
	protected int totalAccumulatedBytes;
	protected int totalReceivedMessages;
	protected String messageTypeName;
	
	public MessageStatisticsContainer(String name) {
		messageTypeName = name;
	}
	
	public void logMessage(Message msg) {
		totalReceivedMessages++;
		totalAccumulatedBytes += msg.getSizeInBytes();
	}
	
	public int getTotalBytes() {
		return totalAccumulatedBytes;
	}
	
	public int getTotalMessages() {
		return totalReceivedMessages;
	}
	
	public String getCountString(int total) {
		return "MESSAGE COUNT " + messageTypeName + " " + totalReceivedMessages + " (" + ((float) totalReceivedMessages / (float) total * 100) + "%)";
	}
	
	public String getSizeString(int total) {
		return "MESSAGE SIZE " + messageTypeName + " " + totalAccumulatedBytes + " (" + ((float) totalAccumulatedBytes / (float) total * 100) + "%)";
	}
	
	public String getTimeString(int total) {
		return "";
	}
}
