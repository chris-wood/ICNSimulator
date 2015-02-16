package ccn.entity.stack;

import ccn.entity.stack.internal.ContentStore;
import ccn.entity.stack.internal.ForwardingInformationBase;
import ccn.entity.stack.internal.PendingInterestTable;
import ccn.entity.stack.internal.ContentStore.CachePolicy;

public class NetworkStack {
	
	protected ContentStore contentStore;
	protected ForwardingInformationBase fib;
	protected PendingInterestTable pit;
	
	private NetworkStack(ContentStore cs, ForwardingInformationBase fib, PendingInterestTable pit) {
		this.contentStore = cs;
		this.fib = fib;
		this.pit = pit;
	}
	
	public ContentStore getContentStore() {
		return contentStore;
	}
	
	public ForwardingInformationBase getForwardingInformationBase() {
		return fib;
	}
	
	public PendingInterestTable getPendingInterestTable() {
		return pit;
	}
	
	public static NetworkStack buildRouterStack() {
		ContentStore cs = new ContentStore(CachePolicy.CachePolicy_LRU);
		ForwardingInformationBase fib = new ForwardingInformationBase();
		PendingInterestTable pit = new PendingInterestTable(); 
		return new NetworkStack(cs, fib, pit);
	}
	
	public static NetworkStack buildConsumerStack() {
		return buildRouterStack();
	}
	
	public static NetworkStack buildProducerStack() {
		return buildRouterStack();
	}
}
