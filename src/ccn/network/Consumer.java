package ccn.network;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import framework.Event;

public class Consumer extends Node {
	
	private int modulus = 5;
	private int state = 0;
	private final static Logger LOGGER = Logger.getLogger(Consumer.class.getName());

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void runComponent(long time) {
		LOGGER.log(Level.INFO, "Consumer " + this.identity + " cycling");
		if (state == 0) {
			Event event = new Event();
			
			// TODO: note, in a smart consumer, the FIB will tell us which
			// interface to send an interest
			this.sendEvent(interfaces.get(0), event);
		}
		state = (state + 1) % modulus;
	}

}
