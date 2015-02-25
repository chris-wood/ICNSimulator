package ccn.entity.stack.internal.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ccn.entity.stack.internal.ForwardingInformationBase;

public class LPMForwardingInformationBaseStrategy extends ForwardingInformationBaseStrategy {
	private ForwardingInformationBase fib;
	
	public LPMForwardingInformationBaseStrategy(ForwardingInformationBase contextFib) {
		fib = contextFib;
	}
	
	public boolean routePresent(String name) {
		return fib.getRoutingTable().containsKey(name);
	}
	
	public List<String> getRoutes(String name) {
		String matchingPrefix = "";
		boolean foundRoute = false;
		for (String prefix : fib.getRoutingTable().keySet()) {
			if (name.startsWith(prefix) && prefix.length() > matchingPrefix.length()) {
				matchingPrefix = prefix;
				foundRoute = true;
			}
		}
		if (foundRoute) {
			List<String> singleton = new ArrayList<String>();
			singleton.add(fib.getRoutingTable().get(matchingPrefix));
			return singleton;
		} else {
			return Collections.emptyList();
		}
	}
	
	public void installRoute(String prefix, String interfaceId) {
		fib.getRoutingTable().put(prefix, interfaceId);
	}
}
