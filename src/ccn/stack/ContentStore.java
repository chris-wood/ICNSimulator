package ccn.stack;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ccn.message.Message;

public class ContentStore {
	
	private CachePolicy policy;
	private Map<String, Message> store;
	private List<String> usageAttempts;
	
	public enum CachePolicy { 
		CachePolicy_LRU,
		CachePolicy_MRU,
		CachePolicy_RR,
	}
	
	public ContentStore(CachePolicy policy) {
		this.policy = policy;
		this.store = new HashMap<String, Message>();
		this.usageAttempts = new ArrayList<String>();
	}
	
	public boolean hashContent(String name) {
		return store.containsKey(store);
	}
	
	public Message retrieveContentByName(String name) {
		return store.get(name);
	}
	
	public void evict() {
		store.remove(findEvictionTarget());
	}
	
	public String findEvictionTarget() {
		switch (policy) {
		case CachePolicy_LRU:
		case CachePolicy_MRU:
		case CachePolicy_RR:
		default:
			return usageAttempts.get(usageAttempts.size() - 1);
		}
	}

}
