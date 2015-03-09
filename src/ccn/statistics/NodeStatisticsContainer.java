package ccn.statistics;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.Stream;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.Message;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;

public class NodeStatisticsContainer {

	protected Map<String, MessageStatisticsContainer> messageContainerMap;
	protected MessageStatisticsContainer messageContainer;
	
	public NodeStatisticsContainer() {
		messageContainerMap = new HashMap<String, MessageStatisticsContainer>();
		messageContainerMap.put(Interest.class.getName(), new MessageStatisticsContainer(Interest.class.getName()));
		messageContainerMap.put(ContentObject.class.getName(), new MessageStatisticsContainer(ContentObject.class.getName()));
		messageContainerMap.put(VirtualInterest.class.getName(), new MessageStatisticsContainer(VirtualInterest.class.getName()));
		messageContainerMap.put(NACK.class.getName(), new MessageStatisticsContainer(NACK.class.getName()));
		messageContainerMap.put(RIPMessage.class.getName(), new MessageStatisticsContainer(RIPMessage.class.getName()));
		messageContainer = new MessageStatisticsContainer(Message.class.getName());
	}
	
	public void logMessage(Message msg, long time) {
		messageContainer.logMessage(msg, time);
	}
	
	public void logInterest(Interest interest, long time) {
		messageContainerMap.get(Interest.class.getName()).logMessage(interest, time);
		logMessage(interest, time);
	}
	
	public void logContentObject(ContentObject contentObject, long time) {
		messageContainerMap.get(ContentObject.class.getName()).logMessage(contentObject, time);
		logMessage(contentObject, time);
	}
	
	public void logVirtualInterest(VirtualInterest virtualInterest, long time) {
		messageContainerMap.get(VirtualInterest.class.getName()).logMessage(virtualInterest, time);
		logMessage(virtualInterest, time);
	}
	
	public void logRIPMessage(RIPMessage ripMessage, long time) {
		messageContainerMap.get(RIPMessage.class.getName()).logMessage(ripMessage, time);
		logMessage(ripMessage, time);
	}
	
	public void logNACK(NACK nack, long time) {
		messageContainerMap.get(NACK.class.getName()).logMessage(nack, time);
		logMessage(nack, time);
	}
	
	public Stream<String> generateCSVStatisticsStream() {
		int totalNumberOfMessages = messageContainer.getTotalMessages();
		int totalMessageBytes = messageContainer.getTotalBytes();
		long totalProcessingTime = messageContainer.getTotalTimeSpentProcessing();
		Stream<String> statisticStream = messageContainerMap.values().stream().map(container -> container.getCSVString(totalNumberOfMessages, totalMessageBytes, totalProcessingTime));	
		return statisticStream;
	}
}
