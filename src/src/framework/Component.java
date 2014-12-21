package framework;

import java.util.HashMap;
import java.util.Map;

public abstract class Component {
	
	protected String identity;
	protected Map<String, MessageQueue> inputQueues;
	protected Map<String, MessageQueue> outputQueues;
	
	public Component(String identity) {
		this.identity = identity;
		inputQueues = new HashMap<String, MessageQueue>();
		outputQueues = new HashMap<String, MessageQueue>();
	}
	
	// template
	public abstract void handleInputMessage(Message msg);
	
	// template 
	public abstract void cycle(long currentTime);

	// final
	public final void crank(long currentTime) {
		for (int i = 0; i < inputQueues.size(); i++) {
			MessageQueue queue = inputQueues.get(i);
			while (!queue.isEmpty()) {
				handleInputMessage(queue.getMessage());
			}
		}
		cycle(currentTime);
	}
	
	public void connect(Component component, MessageQueue queue) {
		addOutputQueue(queue);
		component.addInputQueue(queue);
	}
	
	public String getIdentity() {
		return identity;
	}
	
	public void addOutputQueue(MessageQueue queue) {
		outputQueues.put(queue.getIdentity(), queue);
	}
	
	public void addInputQueue(MessageQueue queue) {
		inputQueues.put(queue.getIdentity(), queue);
	}
	
	public void sendMessage(String queueKey, Message msg) {
		outputQueues.get(queueKey).putMessage(msg);
	}
	
}
