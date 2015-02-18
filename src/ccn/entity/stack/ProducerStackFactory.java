package ccn.entity.stack;

import ccn.entity.Producer;

public class ProducerStackFactory {

	public static NetworkStack buildDefault(Producer producer) {
		return NetworkStack.buildDefaultNetworkStack(producer);
	}
	
}
