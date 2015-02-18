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
	
	public void insertInterest(String name, String interfaceId, Interest interest) {
		if (!isInterestPresent(name)) {
			pit.put(name, new PendingInterestTableEntry());
		}
		pit.get(name).addInterestFromInterface(interfaceId, interest);
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
}
