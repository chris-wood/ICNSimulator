package framework;

import java.util.ArrayList;
import java.util.List;

public class ComponentInterface {
	
	protected ComponentConnection connection;
	
	protected List<Message> inputQueue;
	protected List<Message> outputQueue;
	
	public void insertOutputMessage(Message m) {
		if (outputQueue == null) {
			outputQueue = new ArrayList<Message>();
		}
		outputQueue.add(m);
	}
	
	public void insertInputMessage(Message m) {
		if (inputQueue == null) {
			inputQueue = new ArrayList<Message>();
		}
		inputQueue.add(m);
	}
	
	public Message removeOutputMessage() {
		if (outputQueue == null) {
			outputQueue = new ArrayList<Message>();
			return null;
		} else if (!outputQueue.isEmpty()) {
			Message msg = outputQueue.get(0);
			outputQueue.remove(0);
			return msg;
		}
		return null;
	}
	
	public Message removeInputMessage() {
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
