package ccn.network;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import framework.Event;

public class Router extends Node {
	
	private final static Logger LOGGER = Logger.getLogger(Router.class.getName());

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		broadcastEvent(event);
	}

	@Override
	protected void runComponent(long time) {
		// TODO Auto-generated method stub
		
	}
}
