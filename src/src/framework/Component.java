package framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Component {
	
	protected List<MessageQueue> queues;
	protected Map<String, Connection> connections;
	
	public Component() {
		queues = new ArrayList<MessageQueue>();
		connections = new HashMap<String, Connection>();
	}
	
	// template
	public abstract void handleInputMessage(Message msg);
	
	// template 
	public abstract void cycle(long currentTime);

	// final
	public final void crank(long currentTime) {
		for (int i = 0; i < queues.size(); i++) {
			MessageQueue queue = queues.get(i);
			while (!queue.isEmpty()) {
				handleInputMessage(queue.getMessage());
			}
		}
		cycle(currentTime);
	}
	
	public void addConnection(String connectionKey, Component component, int queueIndex) {
		connections.put(connectionKey, new Connection(component, queueIndex));
	}
	
	public void sendMessage(String connectionKey, Message msg) {
		connections.get(connectionKey).insertMessage(msg);
	}
	
	public void receiveMessage(int queueIndex, Message msg) throws IndexOutOfBoundsException {
		queues.get(queueIndex).putMessage(msg);
	}
	
	public Message getMessage(int queueIndex) {
		return queues.get(queueIndex).getMessage();
	}
	
}
