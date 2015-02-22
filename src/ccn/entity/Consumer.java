package ccn.entity;

import java.util.List;

import ccn.entity.stack.ConsumerStackFactory;
import ccn.entity.stack.NetworkStack;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;
import ccn.network.Point;

public class Consumer extends Node {
	
	protected NetworkStack stack;
	boolean sent = false;

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = ConsumerStackFactory.buildDefault(this);
	}

	@Override
	protected void runComponent(long time) {
		if (!sent) {
			Interest interest = new Interest("lci:/some/thing");
			System.out.println("Consumer issuing interest " + interest);
			stack.sendInterest(interest);
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

	@Override
	protected void processNACKFromInterface(String interfaceId, NACK nack, long time) {
		System.out.println("Consumer " + identity + " received " + nack + " at time " + time);
		nack.setProcessed();
	}

	@Override
	protected void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time) {
		System.out.println("Consumer " + identity + " received " + message);
		stack.processRIPMessage(interfaceId, message);
	}

}
