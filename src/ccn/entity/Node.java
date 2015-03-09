package ccn.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.Message;
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
	private Map<String, Long> pendingMessageTable;
	private static final Logger logger = Logger.getConsoleLogger(Node.class.getName());

	public Node(String identity, Point nodeLocation, List<String> nodeInterfaces) {
		super(identity);
		location = nodeLocation;
		interfaces = new ArrayList<String>();
		interfaces.addAll(nodeInterfaces);
		statTracker = new NodeStatisticsContainer();
		pendingMessageTable = new HashMap<String, Long>();
		
		for (String interfaceId : interfaces) {
			LinkInterface linkInterface = new LinkInterface(interfaceId, Integer.MAX_VALUE); // max write throughput, for now
			this.addChannelInterface(interfaceId, linkInterface);
		}
	}
	
	private void startMessageProcessing(Message msg, long time) {
		pendingMessageTable.put(msg.toString(), time);
	}
	
	public void finishMessageProcessing(Message msg, long time) {
		String eventKey = msg.toString();
		if (!(pendingMessageTable.containsKey(eventKey))) {
			System.err.println("Error: event processing never started for " + eventKey);
		} else {
			long processingTime = time - pendingMessageTable.get(eventKey);
			pendingMessageTable.remove(eventKey);
			finalizeMessageProcessing(msg, processingTime);
		}
		msg.setProcessed();
	}
	
	private void finalizeMessageProcessing(Message msg, long processingTime) {
		if (msg instanceof Interest) {
			statTracker.logInterest((Interest) msg, processingTime);
		} else if (msg instanceof ContentObject) {
			statTracker.logContentObject((ContentObject) msg, processingTime);
		} else if (msg instanceof VirtualInterest) {
			statTracker.logVirtualInterest((VirtualInterest) msg, processingTime);
		} else if (msg instanceof NACK) {
			statTracker.logNACK((NACK) msg, processingTime);
		} else if (msg instanceof RIPMessage) {
			statTracker.logRIPMessage((RIPMessage) msg, processingTime);
		}
	}
	
	protected abstract void processNACKFromInterface(String interfaceId, NACK nack, long time);
	protected abstract void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time);
	protected abstract void processInterestFromInterface(String interfaceId, Interest interest, long time);
	protected abstract void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time);
	protected abstract void processContentObjectFromInterface(String interfaceId, ContentObject content, long time);
	
	private void processInputMessageFromInterface(String interfaceId, Message msg, long time) {
		startMessageProcessing(msg, time);
		if (msg instanceof Interest) {
			processInterestFromInterface(interfaceId, (Interest) msg, time);
		} else if (msg instanceof ContentObject) {
			processContentObjectFromInterface(interfaceId, (ContentObject) msg, time);
		} else if (msg instanceof VirtualInterest) {
			processVirtualInterestFromInterface(interfaceId, (VirtualInterest) msg, time);
		} else if (msg instanceof NACK) {
			processNACKFromInterface(interfaceId, (NACK) msg, time);
		} else if (msg instanceof RIPMessage) {
			processRIPMessageFromInterface(interfaceId, (RIPMessage) msg, time);
		} else {
			logger.log(LogLevel.LogLevel_WARNING, time, "Invalid message type received at Node " + identity + ": " + msg);
		}
		finishMessageProcessing(msg, time + 1);
	}

	@Override
	protected void processInputEventFromInterface(String interfaceId, Event event, long time) {
		processInputMessageFromInterface(interfaceId, (Message) event, time);
	}
	
	public Stream<String> generateCSVStatisticsStream() {
		Stream<String> nodeStream = statTracker.generateCSVStatisticsStream().map(string -> identity + "," + string);
		return nodeStream;
	}
}
