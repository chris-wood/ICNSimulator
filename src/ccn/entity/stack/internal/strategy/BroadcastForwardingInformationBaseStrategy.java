package ccn.entity.stack.internal.strategy;

import java.util.ArrayList;
import java.util.List;

import ccn.entity.stack.internal.ForwardingInformationBase;

public class BroadcastForwardingInformationBaseStrategy extends ForwardingInformationBaseStrategy {
private ForwardingInformationBase fib;
	
	public BroadcastForwardingInformationBaseStrategy(ForwardingInformationBase contextFib) {
		fib = contextFib;
	}
	
	public boolean routePresent(String name) {
		return fib.getRoutingTable().containsKey(name);
	}
	
	public List<String> getRoutes(String name) {
		List<String> routes = new ArrayList<String>();
		for (String prefix : fib.getRoutingTable().keySet()) {
			if (name.startsWith(prefix)) {
				routes.add(fib.getRoutingTable().get(prefix));
			}
		}
		return routes;
	}
	
	public void installRoute(String prefix, String interfaceId) {
		fib.getRoutingTable().put(prefix, interfaceId);
	}
}
