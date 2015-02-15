package ccn.network;

import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.stack.NetworkStack;

public class Producer extends Node {
	
	protected NetworkStack stack;

	public Producer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildProducerStack();
	}

	@Override
	protected void runComponent(long time) {
		// we do nothing but respond to interests
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		ContentObject content = new ContentObject(interest.getName());
		System.out.println("Received interest " + interest + ", responding with content object " + content);
		interest.setProcessed();
		send(interfaceId, content);
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
	}
}
