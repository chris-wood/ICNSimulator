package ccn.network;

import java.util.List;

import framework.Event;

public class Consumer extends Node {
	
	int modulus = 5;
	int state = 0;

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
	}

	@Override
	protected void processInputEvent(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void cycle(long currentTime) {
		System.out.println("Consumer " + this.identity + " cycling");
		if (state == 0) {
			Event event = new Event();
			this.sendEvent(interfaces.get(0), event);
		}
		state = (state + 1) % modulus;
	}

}
