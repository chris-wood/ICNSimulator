package ccn.entity;

import java.util.List;
import java.util.Random;

import ccn.entity.stack.NetworkStack;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.network.Point;

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
		byte[] payload = new byte[20];
		Random rng = new Random();
		rng.nextBytes(payload);
		
		ContentObject content = new ContentObject(interest.getName(), payload);
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
