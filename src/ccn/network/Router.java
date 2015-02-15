package ccn.network;

import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.stack.NetworkStack;

public class Router extends Node {
	
	protected NetworkStack stack;

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildRouterStack();
		
		for (String interfaceId : interfaces) {
			stack.getForwardingInformationBase().installRoute("lci:/", interfaceId); // pick any interface
		}
	}

	@Override
	protected void runComponent(long time) {
		
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		if (stack.getContentStore().hashContent(interest.getName())) {
			ContentObject cachedMessage = stack.getContentStore().retrieveContentByName(interest.getName());
			send(interfaceId, cachedMessage);
		} else {
			stack.getPendingInterestTable().insertInterest(interest.getName(), interfaceId, interest);
			String outputInterface = stack.getForwardingInformationBase().index(interest.getName());
			
			System.out.println("Forwarding interest " + interest + " to " + outputInterface);
			
			Interest newInterest = new Interest(interest.getName()); 
			send(outputInterface, newInterest);
			interest.setProcessed();
		} 
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
		// TODO: implement forwarding logic
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		System.out.println("Router " + identity + " received " + content);
		content.setProcessed();
		stack.getContentStore().insertContent(content.getName(), content);
		
		List<String> downstreamInterfaces = stack.getPendingInterestTable().clearEntryAndGetEntries(content.getName());
		for (String downstreamInterface : downstreamInterfaces) {
			ContentObject newContentObject = new ContentObject(content.getName());
			send(downstreamInterface, newContentObject);
		}
	}
}
