package ccn.stack;

import ccn.stack.ContentStore.CachePolicy;

public class NetworkStack {
	
	protected ContentStore contentStore;
	protected ForwardingInformationBase fib;
	protected PendingInterestTable pit;
	
	private NetworkStack(ContentStore cs, ForwardingInformationBase fib, PendingInterestTable pit) {
		this.contentStore = cs;
		this.fib = fib;
		this.pit = pit;
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
