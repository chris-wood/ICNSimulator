package ccn.network;

import java.util.List;

import framework.Event;

public class Producer extends Node {

	public Producer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
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
