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
	
	public void connect(Component component, MessageQueue queue) throws Exception {
		addOutputQueue(queue);
		component.addInputQueue(queue);
	}
	
	public String getIdentity() {
		return identity;
	}
	
	public void addOutputQueue(MessageQueue queue) throws Exception {
		if (outputQueues.containsKey(queue.getIdentity())) {
			throw new Exception("Output queue already exists");
		}
		outputQueues.put(queue.getIdentity(), queue);
	}
	
	public void addInputQueue(MessageQueue queue) throws Exception {
		if (inputQueues.containsKey(queue.getIdentity())) {
			throw new Exception("Input queue already exists");
		}
		inputQueues.put(queue.getIdentity(), queue);
	}
	
	public void sendMessage(String queueKey, Message msg) {
		outputQueues.get(queueKey).putMessage(msg);
	}
	
}
