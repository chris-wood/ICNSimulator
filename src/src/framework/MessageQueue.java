package framework;

import java.util.ArrayList;
import java.util.List;

public class MessageQueue {
	
	protected String identity;
	protected List<Message> queue;
	
	public MessageQueue(String identity) {
		this.identity = identity;
		queue = new ArrayList<Message>();
	}
	
	public String getIdentity() {
		return identity;
	}
	
	public boolean isEmpty() {
		return queue.isEmpty();
	}
	
	public void putMessage(Message msg) {
		queue.add(msg);
	}
	
	public Message getMessage() {
		Message msg = queue.get(0);
		queue.remove(0);
		return msg;
	}

}
