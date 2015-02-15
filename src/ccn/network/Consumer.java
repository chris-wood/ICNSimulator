package ccn.network;

import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.stack.NetworkStack;

public class Consumer extends Node {
	
	protected NetworkStack stack;
	boolean sent = false;

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildConsumerStack();
	}

	@Override
	protected void runComponent(long time) {
		if (!sent) {
			Interest interest = new Interest("lci:/some/thing");
			System.out.println("Issuing interest " + interest);
			broadcast(interest);
			sent = true;
		}
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		// pass
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
		// pass
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		System.out.println("Consumer " + identity + " received " + content + " at time " + time);
		content.setProcessed();
	}

}
