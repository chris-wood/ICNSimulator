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
import dispatch.math.PoissonDistribution;
import dispatch.math.UniformDistribution;

public class Consumer extends Node {
	
	protected NetworkStack stack;
	protected int interestNonceRange = 10;
	protected long arrivalRate = 500;
	protected PoissonDistribution arrivalDistribution;
	protected UniformDistribution uniformDistribution;
	
	private static final Logger logger = Logger.getConsoleLogger(Consumer.class.getName());

	public Consumer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = ConsumerStackFactory.buildDefault(this);
		uniformDistribution = new UniformDistribution(0, interestNonceRange);
		arrivalDistribution = new PoissonDistribution(50);
	}

	@Override
	protected void runComponent(long time) {
		int sequenceNumber = (int) uniformDistribution.sample();
		String name = "lci:/test/"+ sequenceNumber;
		Interest interest = new Interest(name);
		logger.log(LogLevel.LogLevel_INFO, time, "Consumer issuing interest " + interest);
		stack.sendInterest(interest);
		
		int waitTime = (int) arrivalDistribution.sample();
		yield(waitTime);
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
