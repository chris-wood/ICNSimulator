package ccn.statistics;

public class NodeStatisticsContainer {
	
	protected int numReceivedInterests;
	protected int numReceivedContentObjects;
	protected int numReceivedVirtualInterests;
	protected int numReceivedRIPMessages;
	protected int numReceivedNACKs;
	protected int numReceivedMessages;
	
	public NodeStatisticsContainer() {
		// pass
	}
	
	public void logMessage() {
		numReceivedMessages++;
	}
	
	public void logInterest() {
		numReceivedInterests++;
		logMessage();
	}
	
	public void logContentObject() {
		numReceivedContentObjects++;
		logMessage();
	}
	
	public void logVirtualInterest() {
		numReceivedVirtualInterests++;
		logMessage();
	}
	
	public void logRIPMessage() {
		numReceivedRIPMessages++;
		logMessage();
	}
	
	public void logNACK() {
		numReceivedNACKs++;
		logMessage();
	}
	
	// TODO: this could possibly take a stream as input
	public void display() {
		System.out.printf("Received messages = %d\n", numReceivedMessages);
		System.out.printf("Received interests = %d (%%f)\n", numReceivedInterests, (double)numReceivedInterests / (double)numReceivedMessages);
		System.out.printf("Received content objects = %d (%%f)\n", numReceivedContentObjects, (double)numReceivedContentObjects / (double)numReceivedMessages);
		System.out.printf("Received virtual interests = %d (%%f)\n", numReceivedVirtualInterests, (double)numReceivedVirtualInterests / (double)numReceivedMessages);
		System.out.printf("Received RIP messages = %d (%%f)\n", numReceivedRIPMessages, (double)numReceivedRIPMessages / (double)numReceivedMessages);
		System.out.printf("Received NACKs = %d (%%f)\n", numReceivedNACKs, (double)numReceivedNACKs / (double)numReceivedMessages);
	}

}
