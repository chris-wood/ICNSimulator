package framework;

import java.util.ArrayList;
import java.util.List;

public class ComponentConnection {

	protected List<Message> inputQueue;
	protected List<Message> outputQueue;
	
	public void connect(Component src, Component dst) {
		src.addOutput(this);
		dst.addInput(this);
	}
	
	public void putMessage(Message m) {
		if (outputQueue == null) {
			outputQueue = new ArrayList<Message>();
		}
		outputQueue.add(m);
	}
	
	public Message removeMessage() {
		if (inputQueue == null) {
			inputQueue = new ArrayList<Message>();
			return null;
		} else if (!inputQueue.isEmpty()) {
			Message msg = inputQueue.get(0);
			inputQueue.remove(0);
			return msg;
		}
		return null;
	}
	
}
