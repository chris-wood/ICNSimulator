package ccn.entity;

import java.util.List;

import ccn.entity.stack.NetworkStack;
import ccn.entity.stack.RouterStackFactory;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;
import ccn.network.Point;
import ccn.util.LogLevel;
import ccn.util.Logger;

public class Router extends Node {
	
	protected NetworkStack stack;
	private static final Logger logger = Logger.getConsoleLogger(Router.class.getName());

	public Router(String identity, Point location, List<String> interfaces, int capacity) {
		super(identity, location, interfaces);
		stack = RouterStackFactory.buildDefault(this, capacity);
	}

	@Override
	protected void runComponent(long time) {
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Router " + identity + " received " + interest);
		stack.processInterest(interfaceId, interest);
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest vint, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Router " + identity + " received " + vint);
		stack.processVirtualInterest(interfaceId, vint);
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Router " + identity + " received " + content);
		stack.processContentObject(interfaceId, content);
	}

	@Override
	protected void processNACKFromInterface(String interfaceId, NACK nack, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Router " + identity + " received " + nack);
		stack.processNACK(interfaceId, nack);
	}

	@Override
	protected void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Router " + identity + " received " + message);
		stack.processRIPMessage(interfaceId, message);
	}
}
