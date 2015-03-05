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

}
