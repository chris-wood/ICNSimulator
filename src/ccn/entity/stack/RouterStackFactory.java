package ccn.entity.stack;

import ccn.entity.Router;

public class RouterStackFactory {

	public static NetworkStack buildDefault(Router router, long capacity) {
		return NetworkStack.buildDefaultNetworkStackWithFiniteCacheCapacity(router, capacity);
	}
}
