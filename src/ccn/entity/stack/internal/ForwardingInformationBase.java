package ccn.entity.stack.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ccn.entity.stack.internal.strategy.ForwardingInformationBaseStrategy;
import ccn.entity.stack.internal.strategy.LPMForwardingInformationBaseStrategy;

public class ForwardingInformationBase {
	
	protected Map<String, String> routingTable;
	protected ForwardingInformationBaseStrategy strategy;
	
	public ForwardingInformationBase() {
		this.routingTable = new HashMap<String, String>();
		this.strategy = new LPMForwardingInformationBaseStrategy(this);
	}
	
	public Map<String, String> getRoutingTable() {
		return routingTable;
	}
	
	public boolean routePresent(String name) {
		return strategy.routePresent(name);
	}
	
	public List<String> getRoutes(String name) {
		return strategy.getRoutes(name);
	}
	
	public void installRoute(String prefix, String interfaceId) {
		strategy.installRoute(prefix, interfaceId);
	}

}
