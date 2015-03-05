package ccn.entity;

import java.util.ArrayList;
import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;
import ccn.network.LinkInterface;
import ccn.network.Point;
import ccn.statistics.NodeStatisticsContainer;
import ccn.util.LogLevel;
import ccn.util.Logger;
import dispatch.component.Component;
import dispatch.event.Event;

public abstract class Node extends Component {
	
	protected Point location;
	protected List<String> interfaces;
	protected NodeStatisticsContainer statTracker;
	private static final Logger logger = Logger.getConsoleLogger(Node.class.getName());

	public Node(String identity, Point location, List<String> interfaces) {
		super(identity);
		this.location = location;
		this.interfaces = new ArrayList<String>();
		this.interfaces.addAll(interfaces);
		this.statTracker = new NodeStatisticsContainer();
		
		for (String interfaceId : interfaces) {
			LinkInterface linkInterface = new LinkInterface(interfaceId, Integer.MAX_VALUE); // max write throughput, for now
			this.addChannelInterface(interfaceId, linkInterface);
		}
	}
	
	protected abstract void processNACKFromInterface(String interfaceId, NACK nack, long time);
	protected abstract void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time);
	protected abstract void processInterestFromInterface(String interfaceId, Interest interest, long time);
	protected abstract void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time);
	protected abstract void processContentObjectFromInterface(String interfaceId, ContentObject content, long time);

	@Override
	protected void processInputEventFromInterface(String interfaceId, Event event, long time) {
		if (event instanceof Interest) {
			statTracker.logInterest((Interest) event);
			processInterestFromInterface(interfaceId, (Interest) event, time);
		} else if (event instanceof ContentObject) {
			statTracker.logContentObject((ContentObject) event);
			processContentObjectFromInterface(interfaceId, (ContentObject) event, time);
		} else if (event instanceof VirtualInterest) {
			statTracker.logVirtualInterest((VirtualInterest) event);
			processVirtualInterestFromInterface(interfaceId, (VirtualInterest) event, time);
		} else if (event instanceof NACK) {
			statTracker.logNACK((NACK) event);
			processNACKFromInterface(interfaceId, (NACK) event, time);
		} else if (event instanceof RIPMessage) {
			statTracker.logRIPMessage((RIPMessage) event);
			processRIPMessageFromInterface(interfaceId, (RIPMessage) event, time);
		} else {
			logger.log(LogLevel.LogLevel_WARNING, time, "Invalid message type received at Node " + identity + ": " + event);
		}
	}
	
	public void displayStatistics() {
		statTracker.display(identity);
	}
}
