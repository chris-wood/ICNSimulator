package ccn.entity.stack.internal;

import java.util.Collections;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ccn.message.Interest;

public class PendingInterestTable {
	
	protected Map<String, PendingInterestTableEntry> pit;
	
	public PendingInterestTable() {
		this.pit = new HashMap<String, PendingInterestTableEntry>();
	}
	
	public boolean isInterestPresent(String name) {
		return pit.containsKey(name);
	}
	
	public void appendInterest(String name, String interfaceid, Interest interest) {
		pit.get(name).addInterestFromInterface(interfaceid, interest);
	}
	
	public void insertInterest(String name, String interfaceId, Interest interest) {
		pit.put(name, new PendingInterestTableEntry());
		appendInterest(name, interfaceId, interest);
	}
	
	public List<String> clearEntryAndGetEntries(String name) {
		PendingInterestTableEntry entry = pit.get(name);
		pit.remove(name);
		if (entry == null) {
			return Collections.<String>emptyList();
		} else {
			return entry.getInterfaces(); 
		}
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String entry : pit.keySet()) {
			builder.append(entry + ": " + pit.get(entry).getNumberOfEntries());
		}
		return builder.toString();
	}
}
