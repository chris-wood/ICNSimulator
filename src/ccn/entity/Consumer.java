package ccn.entity;

import java.util.List;

import ccn.entity.stack.ConsumerStackFactory;
import ccn.entity.stack.NetworkStack;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;
import ccn.network.Point;
import ccn.util.LogLevel;
import ccn.util.Logger;

public class Consumer extends Node {
	
	protected NetworkStack stack;
	protected int interestSequenceNumber = 0;
	protected int interestSequenceRange = 100;
	
	private static final Logger logger = Logger.getConsoleLogger(Consumer.class.getName());

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = ConsumerStackFactory.buildDefault(this);
	}

	@Override
	protected void runComponent(long time) {
		String name = "lci:/test/"+ interestSequenceNumber;
		interestSequenceNumber = (interestSequenceNumber + 1) % interestSequenceRange;
		Interest interest = new Interest(name);
		logger.log(LogLevel.LogLevel_INFO, time, "Consumer issuing interest " + interest);
		stack.sendInterest(interest);
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		// pass
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
		// pass
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Consumer " + identity + " received " + content);
	}

	@Override
	protected void processNACKFromInterface(String interfaceId, NACK nack, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Consumer " + identity + " received " + nack + " at time " + time);
		finishMessageProcessing(nack, time);
	}

	@Override
	protected void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Consumer " + identity + " received " + message);
		stack.processRIPMessage(interfaceId, message);
	}

}
