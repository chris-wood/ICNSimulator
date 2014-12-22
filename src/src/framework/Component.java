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
	public final void crankOutput(long currentTime) {
		cycle(currentTime);
	}
	
	public final void crankInput(long currentTime) {
		for (String queueKey : inputQueues.keySet()) {
			MessageQueue queue = inputQueues.get(queueKey);
			System.out.println(identity + " is processing input queue " + queue.toString() + " at time " + currentTime);
			while (!queue.isEmpty()) {
				handleInputMessage(queue.getMessage());
			}
		}
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
		if (!outputQueues.containsKey(queueKey)) {
			System.err.println("Error: " + this.identity + " output queue key: " + queueKey + " not found");
		} else {
			System.out.println(identity + " is sending message " + msg.toString());
			outputQueues.get(queueKey).putMessage(msg);
		}
	}
	
}
