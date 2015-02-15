package ccn.stack;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ccn.message.Message;

public class PendingInterestTable {
	
	protected Map<String, PendingInterestTableEntry> pit;
	
	public PendingInterestTable() {
		this.pit = new HashMap<String, PendingInterestTableEntry>();
	}
	
	public boolean isInterestPresent(String name) {
		return pit.containsKey(name);
	}
	
	public void insertInterest(String name, String interfaceId, Message message) {
		if (!isInterestPresent(name)) {
			pit.put(name, new PendingInterestTableEntry());
		}
		pit.get(name).addMessageFromInterface(interfaceId, message);
	}
	
	public List<String> clearEntryAndGetEntries(String name) {
		PendingInterestTableEntry entry = pit.get(name);
		pit.remove(name);
		return entry.getInterfaces();
	}

}
