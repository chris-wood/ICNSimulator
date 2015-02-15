package ccn.stack;

import java.util.ArrayList;
import java.util.List;

import ccn.message.Message;

public class PendingInterestTableEntry {
	
	private String name;
	private List<String> interfaces;
	private List<Message> messages;
	
	public PendingInterestTableEntry() {
		this.interfaces = new ArrayList<String>();
		this.messages = new ArrayList<Message>();
	}
	
	public void addMessageFromInterface(String interfaceId, Message message) {
		interfaces.add(interfaceId);
		messages.add(message);
	}
	
	public List<String> getInterfaces() {
		return interfaces;
	}

}
