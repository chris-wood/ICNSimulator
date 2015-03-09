package ccn.statistics;

import ccn.message.Message;

public class MessageStatisticsContainer {
	
	protected int totalAccumulatedBytes;
	protected int totalReceivedMessages;
	protected long totalTimeSpentProcessing;
	protected String messageTypeName;
	
	public MessageStatisticsContainer(String name) {
		messageTypeName = name;
	}
	
	public void logMessage(Message msg, long time) {
		totalReceivedMessages++;
		totalAccumulatedBytes += msg.getSizeInBytes();
	}
	
	public int getTotalBytes() {
		return totalAccumulatedBytes;
	}
	
	public int getTotalMessages() {
		return totalReceivedMessages;
	}
	
	public long getTotalTimeSpentProcessing() {
		return totalTimeSpentProcessing;
	}
	
	public String getCSVString(int totalCount, int totalSize, long totalTime) {
		String csv = messageTypeName + ",";
		csv = csv + "count," + totalReceivedMessages + "," + ((float) totalReceivedMessages / (float) totalCount * 100) + ",";
		csv = csv + "time," + totalTimeSpentProcessing + "," + ((float) totalTimeSpentProcessing / (float) totalTime * 100) + ",";
		csv = csv + "size," + totalAccumulatedBytes + "," + ((float) totalAccumulatedBytes / (float) totalSize * 100);
		return csv;
	}
	
	public String getTimeString(int total) {
		return "";
	}
}
