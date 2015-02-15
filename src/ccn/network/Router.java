package ccn.network;

import java.util.List;

import ccn.message.Message;
import ccn.stack.NetworkStack;
import framework.Event;

public class Router extends Node {
	
	protected NetworkStack stack;

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildRouterStack();
	}

	@Override
	protected void processInputEventFromInterface(String queueKey, Event event, long time) {
		Message message = (Message) event;
		event.setProcessed();
		
		if (stack.getContentStore().hashContent(message.getName())) {
			// return content back to downstream interface
		} else {
			if (stack.getPendingInterestTable().isInterestPresent(message.getName())) {
				stack.getPendingInterestTable().insertInterest(message.getName(), null, message);
				// add to PIT
			}
			// forward using FIB 
		} 
		
//		System.out.println("Router " + identity + " is forwarding " + event + " at time " + time);
//		broadcastEvent(message);
	}

	@Override
	protected void runComponent(long time) {
		
	}
}
