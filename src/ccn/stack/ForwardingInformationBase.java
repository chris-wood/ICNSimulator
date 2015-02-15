package ccn.stack;

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
		return routingTable.get(name);
	}

}
