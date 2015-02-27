package ccn.entity.stack;

import ccn.entity.Consumer;

public class ConsumerStackFactory {
	
	public static NetworkStack buildDefault(Consumer consumer) {
		return NetworkStack.buildDefaultNetworkStack(consumer);
	}

}
