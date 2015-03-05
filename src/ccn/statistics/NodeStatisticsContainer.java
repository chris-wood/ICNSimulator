package ccn.statistics;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.stream.DoubleStream;
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
	
	public void logMessage(Message msg) {
		messageContainer.logMessage(msg);
	}
	
	public void logInterest(Interest interest) {
		messageContainerMap.get(Interest.class.getName()).logMessage(interest);
		logMessage(interest);
	}
	
	public void logContentObject(ContentObject contentObject) {
		messageContainerMap.get(ContentObject.class.getName()).logMessage(contentObject);
		logMessage(contentObject);
	}
	
	public void logVirtualInterest(VirtualInterest virtualInterest) {
		messageContainerMap.get(VirtualInterest.class.getName()).logMessage(virtualInterest);
		logMessage(virtualInterest);
	}
	
	public void logRIPMessage(RIPMessage ripMessage) {
		messageContainerMap.get(RIPMessage.class.getName()).logMessage(ripMessage);
		logMessage(ripMessage);
	}
	
	public void logNACK(NACK nack) {
		messageContainerMap.get(NACK.class.getName()).logMessage(nack);
		logMessage(nack);
	}
	
	// TODO: this could possibly take a stream as input
	public void display(String nodeIdentity) {
		int totalNumberOfMessages = messageContainer.getTotalMessages();
		int totalMessageBytes = messageContainer.getTotalBytes();
		Stream<String> totalPercentages = messageContainerMap.values().stream().map(container -> container.getCountString(totalNumberOfMessages));
		Stream<String> bytePercentages = messageContainerMap.values().stream().map(container -> container.getSizeString(totalMessageBytes));
		
		totalPercentages.forEach(new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println("["+ nodeIdentity + "] " + t);
			}

		});
		
		bytePercentages.forEach(new Consumer<String>() {

			@Override
			public void accept(String t) {
				System.out.println("["+ nodeIdentity + "] " + t);
			}

		});
	}

}
