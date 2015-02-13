package ccn.network;

import framework.EventQueue;

public class Channel extends EventQueue {
	
	private String identity;
	private int dataRate;
	
	public Channel(String identity, int dataRate) {
		super(identity);
		this.identity = identity;
		this.dataRate = dataRate;
	}

}
