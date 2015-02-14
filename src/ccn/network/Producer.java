package ccn.network;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import framework.Event;

public class Producer extends Node {
	
	private final static Logger LOGGER = Logger.getLogger(Consumer.class.getName());

	public Producer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		System.out.println("Producer " + identity + " received " + event + " at time " + time);
	}

	@Override
	protected void runComponent(long time) {
		// TODO Auto-generated method stub
		
	}

}
