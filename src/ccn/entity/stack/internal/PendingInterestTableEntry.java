package ccn.entity.stack.internal;

import java.util.ArrayList;
import java.util.List;

import ccn.message.Interest;

public class PendingInterestTableEntry {
	
	private List<String> interfaces;
	private List<Interest> messages;
	
	public PendingInterestTableEntry() {
		this.interfaces = new ArrayList<String>();
		this.messages = new ArrayList<Interest>();
	}
	
	public void addInterestFromInterface(String interfaceId, Interest interest) {
		interfaces.add(interfaceId);
		messages.add(interest);
	}
	
	public List<String> getInterfaces() {
		return interfaces;
	}

}
