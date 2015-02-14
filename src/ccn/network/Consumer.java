package ccn.network;

import java.util.List;

import framework.Event;

public class Consumer extends Node {
	
	private int modulus = 5;
	private int state = 0;

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event, long time) {
		
	}

	@Override
	protected void runComponent(long time) {
		if (state == 0) {
			Event event = new Event();
			System.out.println("Consumer " + identity + " is sending " + event + " at time " + time);
			broadcastEvent(event);
		} else {
			System.out.println("Consumer " + this.identity + " cycling at time " + time);
		}
		state = (state + 1) % modulus;
	}

}
