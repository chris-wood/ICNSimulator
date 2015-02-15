package ccn.network;

import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.Message;
import ccn.message.VirtualInterest;
import ccn.stack.NetworkStack;
import framework.Event;

public class Router extends Node {
	
	protected NetworkStack stack;

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildRouterStack();
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
			if (stack.getPendingInterestTable().isInterestPresent(interest.getName())) {
				stack.getPendingInterestTable().insertInterest(interest.getName(), null, interest);
			}
			String outputInterface = stack.getForwardingInformationBase().index(interest.getName());
			send(outputInterface, interest);
		} 
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
		// TODO: implement forwarding logic
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId,
			ContentObject content, long time) {
		stack.getContentStore().insertContent(content.getName(), content);
	}
}
