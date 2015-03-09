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
	
	public String getCSVString(int totalCount, int totalSize) {
		String csv = "count," + messageTypeName + ",";
		csv = csv + totalReceivedMessages + "," + ((float) totalReceivedMessages / (float) totalCount * 100);
		csv = csv + totalAccumulatedBytes + "," + ((float) totalAccumulatedBytes / (float) totalSize * 100);
		return csv;
	}
	
	public String getTimeString(int total) {
		return "";
	}
}
