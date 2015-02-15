package ccn.message;

import framework.Event;

public class Message extends Event {

	protected String name;
	
	public Message(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
}
