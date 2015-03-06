package ccn.entity.stack;

import java.util.List;

import ccn.entity.Node;
import ccn.entity.stack.internal.ContentStore;
import ccn.entity.stack.internal.ForwardingInformationBase;
import ccn.entity.stack.internal.PendingInterestTable;
import ccn.entity.stack.internal.ContentStore.CachePolicy;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;

public class NetworkStack {
	
	protected ContentStore contentStore;
	protected ForwardingInformationBase internalFib;
	protected PendingInterestTable internalPit;
	protected Node parentNode;
	
	private NetworkStack(Node node, ContentStore cs, ForwardingInformationBase fib, PendingInterestTable pit) {
		parentNode = node;
		contentStore = cs;
		internalFib = fib;
		internalPit = pit;
	}
	
	public void processNACK(String interfaceId, NACK nack) {
		nack.setProcessed();
		List<String> downstreamInterfaces = internalPit.clearEntryAndGetEntries(nack.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			NACK newNack = new NACK(nack.getName(), nack.getError());
			parentNode.send(downstreamInterface, newNack);
		}
	}
	
	public void processRIPMessage(String interfaceId, RIPMessage routeMessage) {
		routeMessage.setProcessed();
		RIPMessage newMessage = new RIPMessage(routeMessage.getName(), routeMessage.getPrefix());
		internalFib.installRoute(routeMessage.getPrefix(), interfaceId);
		parentNode.broadcast(newMessage, interfaceId);
	}
	
	public boolean sendInterest(Interest interest) {
		List<String> outputInterfaces = internalFib.getRoutes(interest.getName());
		if (outputInterfaces.isEmpty()) {
			return false;
		} else {
			for (String outputInterface : outputInterfaces) {
				Interest newInterest = new Interest(interest.getName());
				parentNode.send(outputInterface, newInterest);
			}
			return true;
		}
	}
	
	public void sendVirtualInterest(VirtualInterest vint) {
		List<String> outputInterfaces = internalFib.getRoutes(vint.getName());
		for (String outputInterface : outputInterfaces) {
			VirtualInterest newInterest = new VirtualInterest(vint.getName());
			parentNode.send(outputInterface, newInterest);
		}
	}
	
	public void sendInterestWithNACK(String interfaceId, Interest interest) {
		boolean success = sendInterest(interest);
		if (!success) {
			NACK nack = new NACK(interest.getName(), "Route to " + interest.getName() + " not found.");
			parentNode.send(interfaceId, nack);
		}
	}
	
	public void processInterest(String interfaceId, Interest interest) {
		interest.setProcessed();
		if (contentStore.hasContent(interest.getName())) {
			ContentObject cachedMessage = contentStore.retrieveContentByName(interest.getName());
			parentNode.send(interfaceId, cachedMessage);
			
			VirtualInterest virtualInterest = new VirtualInterest(interest.getName());
			sendVirtualInterest(virtualInterest);
		} else if (internalPit.isInterestPresent(interest.getName())) {
			internalPit.appendInterest(interest.getName(), interfaceId, interest);
		} else {
			internalPit.insertInterest(interest.getName(), interfaceId, interest);
			sendInterestWithNACK(interfaceId, interest);
		}
	}
	
	public void processContentObject(String interfaceId, ContentObject content) {
		content.setProcessed();
		contentStore.insertContent(content.getName(), content);
		List<String> downstreamInterfaces = internalPit.clearEntryAndGetEntries(content.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			ContentObject newContentObject = new ContentObject(content.getName(), content.getPayload());
			parentNode.send(downstreamInterface, newContentObject);
		}
	}
	
	public void processVirtualInterest(String interfaceId, VirtualInterest vint) {
		vint.setProcessed();
		sendVirtualInterest(vint);
	}
	
	public static NetworkStack buildDefaultNetworkStack(Node node) {
		return buildDefaultNetworkStackWithFiniteCacheCapacity(node, Integer.MAX_VALUE);
	}
	
	public static NetworkStack buildDefaultNetworkStackWithFiniteCacheCapacity(Node node, int capacity) {
		ContentStore cs = new ContentStore(CachePolicy.CachePolicy_LRU, capacity);
		ForwardingInformationBase fib = new ForwardingInformationBase();
		PendingInterestTable pit = new PendingInterestTable(); 
		return new NetworkStack(node, cs, fib, pit);
	}
}
