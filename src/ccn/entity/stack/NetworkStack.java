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
	protected ForwardingInformationBase fib;
	protected PendingInterestTable pit;
	protected Node node;
	
	private NetworkStack(Node node, ContentStore cs, ForwardingInformationBase fib, PendingInterestTable pit) {
		this.node = node;
		this.contentStore = cs;
		this.fib = fib;
		this.pit = pit;
	}
	
	public void processNACK(String interfaceId, NACK nack) {
		nack.setProcessed();
		List<String> downstreamInterfaces = pit.clearEntryAndGetEntries(nack.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			NACK newNack = new NACK(nack.getName(), nack.getError());
			node.send(downstreamInterface, newNack);
		}
	}
	
	public void processRIPMessage(String interfaceId, RIPMessage routeMessage) {
		routeMessage.setProcessed();
		RIPMessage newMessage = new RIPMessage(routeMessage.getName(), routeMessage.getPrefix());
		fib.installRoute(routeMessage.getPrefix(), interfaceId);
		node.broadcast(newMessage, interfaceId);
	}
	
	public void processInterest(String interfaceId, Interest interest) {
		interest.setProcessed();
		if (contentStore.hashContent(interest.getName())) {
			ContentObject cachedMessage = contentStore.retrieveContentByName(interest.getName());
			node.send(interfaceId, cachedMessage);
		} else {
			pit.insertInterest(interest.getName(), interfaceId, interest);
			String outputInterface = fib.index(interest.getName());
			if (outputInterface != null) {
				Interest newInterest = new Interest(interest.getName());
				node.send(outputInterface, newInterest);
			} else {
				NACK nack = new NACK(interest.getName(), "Route to " + interest.getName() + " not found.");
				node.send(interfaceId, nack);
			}
		} 
	}
	
	public void processContentObject(String interfaceId, ContentObject content) {
		content.setProcessed();
		contentStore.insertContent(content.getName(), content);
		pit.clearEntryAndGetEntries(content.getName());
		List<String> downstreamInterfaces = pit.clearEntryAndGetEntries(content.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			ContentObject newContentObject = new ContentObject(content.getName(), content.getPayload());
			node.send(downstreamInterface, newContentObject);
		}
	}
	
	public void processVirtualInterest(String interfaceId, VirtualInterest vint) {
		// TODO: when everything else is working
	}
	
	public static NetworkStack buildDefaultNetworkStack(Node node) {
		ContentStore cs = new ContentStore(CachePolicy.CachePolicy_LRU);
		ForwardingInformationBase fib = new ForwardingInformationBase();
		PendingInterestTable pit = new PendingInterestTable(); 
		return new NetworkStack(node, cs, fib, pit);
	}
}
