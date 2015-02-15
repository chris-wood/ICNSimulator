package ccn.network;

import java.util.List;

import ccn.stack.NetworkStack;
import framework.Event;

public class Producer extends Node {
	
	protected NetworkStack stack;

	public Producer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildProducerStack();
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		event.setProcessed();
		System.out.println("Producer " + identity + " received " + event + " at time " + time);
	}

	@Override
	protected void runComponent(long time) {
		
	}
}
