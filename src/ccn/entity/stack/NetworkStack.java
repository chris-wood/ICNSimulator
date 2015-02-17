package ccn.entity.stack;

import java.util.List;

import ccn.entity.Consumer;
import ccn.entity.Node;
import ccn.entity.Producer;
import ccn.entity.Router;
import ccn.entity.stack.internal.ContentStore;
import ccn.entity.stack.internal.ForwardingInformationBase;
import ccn.entity.stack.internal.PendingInterestTable;
import ccn.entity.stack.internal.ContentStore.CachePolicy;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.Message;
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
	
	public ContentStore getContentStore() {
		return contentStore;
	}
	
	public void processInterest(String interfaceId, Interest interest) {
		interest.setProcessed();
		if (contentStore.hashContent(interest.getName())) {
			ContentObject cachedMessage = contentStore.retrieveContentByName(interest.getName());
			node.send(interfaceId, cachedMessage);
		} else {
			pit.insertInterest(interest.getName(), interfaceId, interest);
			String outputInterface = fib.index(interest.getName());
			
			Interest newInterest = new Interest(interest.getName()); 
			node.send(outputInterface, newInterest);
		} 
	}
	
	public void processContentObject(String interfaceId, ContentObject content) {
		content.setProcessed();
		contentStore.insertContent(content.getName(), content);
		List<String> downstreamInterfaces = pit.clearEntryAndGetEntries(content.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			ContentObject newContentObject = new ContentObject(content.getName(), content.getPayload());
			node.send(downstreamInterface, newContentObject);
		}
	}
	
	public void processVirtualInterest(String interfaceId, VirtualInterest vint) {
		// TODO
	}
	
	public ForwardingInformationBase getForwardingInformationBase() {
		return fib;
	}
	
	public PendingInterestTable getPendingInterestTable() {
		return pit;
	}
	
	public static NetworkStack buildDefaultNetworkStack(Node node) {
		ContentStore cs = new ContentStore(CachePolicy.CachePolicy_LRU);
		ForwardingInformationBase fib = new ForwardingInformationBase();
		PendingInterestTable pit = new PendingInterestTable(); 
		return new NetworkStack(node, cs, fib, pit);
	}
	
	public static NetworkStack buildRouterStack(Router router) {
		return buildDefaultNetworkStack(router);
	}
	
	public static NetworkStack buildConsumerStack(Consumer consumer) {
		return buildDefaultNetworkStack(consumer);
	}
	
	public static NetworkStack buildProducerStack(Producer producer) {
		return buildDefaultNetworkStack(producer);
	}
}
