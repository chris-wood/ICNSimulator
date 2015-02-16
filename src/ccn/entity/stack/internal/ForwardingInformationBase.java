package ccn.entity.stack.internal;

import java.util.HashMap;
import java.util.Map;

public class ForwardingInformationBase {
	
	protected Map<String, String> routingTable;
	
	public ForwardingInformationBase() {
		this.routingTable = new HashMap<String, String>();
	}
	
	public boolean routePresent(String name) {
		return routingTable.containsKey(name);
	}
	
	public String index(String name) {
		String matchingPrefix = "";
		for (String prefix : routingTable.keySet()) {
			if (name.startsWith(prefix) && prefix.length() > matchingPrefix.length()) {
				matchingPrefix = prefix;
			}
		}
		return routingTable.get(matchingPrefix);
	}
	
	public void installRoute(String prefix, String interfaceId) {
		routingTable.put(prefix, interfaceId);
	}

}
