package ccn.network;

import java.util.List;

import framework.Event;

public class Router extends Node {

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		Event newEvent = event.copy();
		System.out.println("Router " + identity + " is forwarding " + event + " at time " + time);
		broadcastEvent(newEvent);
		event.setProcessed();
	}

	@Override
	protected void runComponent(long time) {
		
	}
}
