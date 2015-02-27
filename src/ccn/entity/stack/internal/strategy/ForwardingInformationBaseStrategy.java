package ccn.entity.stack.internal.strategy;

import java.util.List;

public abstract class ForwardingInformationBaseStrategy {
	public abstract boolean routePresent(String name);
	public abstract List<String> getRoutes(String name);
	public abstract void installRoute(String prefix, String interfaceId);
}
