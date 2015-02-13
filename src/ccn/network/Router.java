package ccn.network;

import java.util.List;

import framework.Event;

public class Router extends Node {

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event) {
		System.out.println("router processing input");
		this.sendEvent(interfaces.get(1), event);
	}

	@Override
	protected void cycle(long currentTime) {
		System.out.println("Router " + this.identity + " cycling");
	}

}
