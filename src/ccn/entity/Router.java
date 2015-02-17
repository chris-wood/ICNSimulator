package ccn.entity;

import java.util.List;

import ccn.entity.stack.NetworkStack;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.network.Point;

public class Router extends Node {
	
	protected NetworkStack stack;

	public Router(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildRouterStack(this);
		
		// TODO: this will be replaced by the actual routing protocol
		for (String interfaceId : interfaces) {
			stack.getForwardingInformationBase().installRoute("lci:/", interfaceId); // pick any interface
		}
	}

	@Override
	protected void runComponent(long time) {
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		System.out.println("Router " + identity + " received " + interest);
		stack.processInterest(interfaceId, interest);
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest vint, long time) {
		System.out.println("Router " + identity + " received " + vint);
		stack.processVirtualInterest(interfaceId, vint);
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		System.out.println("Router " + identity + " received " + content);
		stack.processContentObject(interfaceId, content);
	}
}
