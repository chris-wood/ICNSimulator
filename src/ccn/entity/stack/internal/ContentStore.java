package ccn.entity.stack.internal;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ccn.message.ContentObject;

public class ContentStore {
	
	private int capacity;
	private int occupiedSpace;
	private CachePolicy policy;
	private Map<String, ContentObject> store;
	private List<String> usageAttempts;
	
	public enum CachePolicy { 
		CachePolicy_LRU,
		CachePolicy_MRU,
		CachePolicy_RR,
	}
	
	public ContentStore(CachePolicy policy, int cacheCapacity) {
		this.capacity = cacheCapacity;
		this.policy = policy;
		this.store = new HashMap<String, ContentObject>();
		this.usageAttempts = new ArrayList<String>();
	}
	
	public boolean hasContent(String name) {
		return store.containsKey(name);
	}
	
	public void insertContent(String name, ContentObject contentObject) {
		checkForEviction(contentObject);
		insertContentAndUpdateSpace(name, contentObject);
	}
	
	private void insertContentAndUpdateSpace(String name, ContentObject contentObject) {
		store.put(name, contentObject);
		occupiedSpace += contentObject.getSizeInBits();
	}
	
	public ContentObject retrieveContentByName(String name) {
		return store.get(name);
	}
	
	private void checkForEviction(ContentObject contentObject) {
		int size = contentObject.getSizeInBits();
		if (size + occupiedSpace >= capacity) {
			evict();
		}
	}
	
	private void evict() {
		String evictionKey = selectEvictionTarget();
		ContentObject evictionTarget = store.get(evictionKey);
		store.remove(evictionKey);
		occupiedSpace -= evictionTarget.getSizeInBits();
	}
	
	private String selectEvictionTarget() {
		switch (policy) {
		case CachePolicy_MRU:
		case CachePolicy_RR:
		case CachePolicy_LRU:
		default:
			return usageAttempts.get(usageAttempts.size() - 1);
		}
	}

}
