package ccn.network;

import java.util.List;

import ccn.stack.NetworkStack;
import framework.Event;

public class Consumer extends Node {
	
	protected NetworkStack stack;

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildConsumerStack();
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		
	}

	@Override
	protected void runComponent(long time) {
	}

}
