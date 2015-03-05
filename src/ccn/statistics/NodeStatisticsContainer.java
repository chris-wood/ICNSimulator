package ccn.statistics;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.Message;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;

public class NodeStatisticsContainer {
	
	protected int numReceivedInterests;
	protected int numReceivedContentObjects;
	protected int numReceivedVirtualInterests;
	protected int numReceivedRIPMessages;
	protected int numReceivedNACKs;
	protected int numReceivedMessages;
	
	protected int totalMessageBytes;
	
	public NodeStatisticsContainer() {
		// pass
	}
	
	public void logMessage(Message msg) {
		numReceivedMessages++;
		totalMessageBytes += msg.getSizeInBytes() / 8;
	}
	
	public void logInterest(Interest interest) {
		numReceivedInterests++;
		logMessage(interest);
	}
	
	public void logContentObject(ContentObject contentObject) {
		numReceivedContentObjects++;
		logMessage(contentObject);
	}
	
	public void logVirtualInterest(VirtualInterest virtualInterest) {
		numReceivedVirtualInterests++;
		logMessage(virtualInterest);
	}
	
	public void logRIPMessage(RIPMessage ripMessage) {
		numReceivedRIPMessages++;
		logMessage(ripMessage);
	}
	
	public void logNACK(NACK nack) {
		numReceivedNACKs++;
		logMessage(nack);
	}
	
	// TODO: this could possibly take a stream as input
	public void display(String nodeIdentity) {
		float interestPercentage = ((float)numReceivedInterests / (float)numReceivedMessages) * 100;
		float contentObjectPercentage = ((float)numReceivedContentObjects / (float)numReceivedMessages) * 100;
		float virtualInterestPercentage = ((float)numReceivedVirtualInterests / (float)numReceivedMessages) * 100;
		float ripMessagePercentage = ((float)numReceivedRIPMessages / (float)numReceivedMessages) * 100;
		float nackPercentage = ((float)numReceivedNACKs / (float)numReceivedMessages) * 100;
		
		System.out.println("["+ nodeIdentity + "] Received messages = "+ numReceivedMessages);
		System.out.println("["+ nodeIdentity + "] Received interests = "+ numReceivedInterests + " (" + interestPercentage + "%)");
		System.out.println("["+ nodeIdentity + "] Received content objects = "+ numReceivedContentObjects + " (" + contentObjectPercentage + "%)");
		System.out.println("["+ nodeIdentity + "] Received virtual interests = "+ numReceivedVirtualInterests + " (" + virtualInterestPercentage + "%)");
		System.out.println("["+ nodeIdentity + "] Received RIP messages = "+ numReceivedRIPMessages + " (" + ripMessagePercentage + "%)");
		System.out.println("["+ nodeIdentity + "] Received NACK messages = "+ numReceivedNACKs + " (" + nackPercentage + "%)");
	}

}
