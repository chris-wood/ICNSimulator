package ccn.entity;

import java.util.ArrayList;
import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.network.Point;
import framework.Channel;
import framework.Component;
import framework.Event;

public abstract class Node extends Component {
	
	protected Point location;
	protected List<String> interfaces;

	public Node(String identity, Point location, List<String> interfaces) {
		super(identity);
		this.location = location;
		this.interfaces = new ArrayList<String>();
		this.interfaces.addAll(interfaces);
		for (String interfaceId : interfaces) {
			Channel channel = new Channel(interfaceId);
			this.addChannelInterface(channel.getIdentity(), channel);
		}
	}
	
	protected abstract void processInterestFromInterface(String interfaceId, Interest interest, long time);
	protected abstract void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time);
	protected abstract void processContentObjectFromInterface(String interfaceId, ContentObject content, long time);

	@Override
	protected void processInputEventFromInterface(String interfaceId, Event event, long time) {
		if (event instanceof Interest) {
			processInterestFromInterface(interfaceId, (Interest) event, time);
		} else if (event instanceof ContentObject) {
			processContentObjectFromInterface(interfaceId, (ContentObject) event, time);
		} else if (event instanceof VirtualInterest) {
			processVirtualInterestFromInterface(interfaceId, (VirtualInterest) event, time);
		} else {
			System.err.println("Invalid message type received at Node " + identity + ": " + event);
		}
	}
}
