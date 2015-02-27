package ccn.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ccn.entity.stack.NetworkStack;
import ccn.entity.stack.ProducerStackFactory;
import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.NACK;
import ccn.message.RIPMessage;
import ccn.message.VirtualInterest;
import ccn.network.Point;
import ccn.util.LogLevel;
import ccn.util.Logger;

public class Producer extends Node {
	
	protected NetworkStack stack;
	protected List<String> prefixes;
	protected boolean hasBroadcastedPrefixes;
	private static final Logger logger = Logger.getConsoleLogger(Producer.class.getName());

	public Producer(String identity, Point location, List<String> interfaces, List<String> routerPrefixes) {
		super(identity, location, interfaces);
		stack = ProducerStackFactory.buildDefault(this);
		prefixes = new ArrayList<String>();
		hasBroadcastedPrefixes = false;
		for (String prefix : routerPrefixes) {
			prefixes.add(prefix);
		}
	}

	@Override
	protected void runComponent(long time) {
		if (hasBroadcastedPrefixes) {
			// pass
		} else {
			for (String prefix : prefixes) {
				RIPMessage message = new RIPMessage(prefix, prefix);
				logger.log(LogLevel.LogLevel_INFO, time, "Broadcasting router RIPMessage " + message);
				broadcast(message);
			}
			hasBroadcastedPrefixes = true;
		}
	}

	@Override
	protected void processInterestFromInterface(String interfaceId, Interest interest, long time) {
		byte[] payload = new byte[20];
		Random rng = new Random();
		rng.nextBytes(payload);
		
		ContentObject content = new ContentObject(interest.getName(), payload);
		logger.log(LogLevel.LogLevel_INFO, time, "Producer " + identity + " received interest " + interest + ", responding with content object " + content);
		interest.setProcessed();
		send(interfaceId, content);
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId, VirtualInterest interest, long time) {
		logger.log(LogLevel.LogLevel_INFO, time, "Producer " + identity + " received vint " + interest);
		interest.setProcessed();
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId, ContentObject content, long time) {
		// pass
	}

	@Override
	protected void processNACKFromInterface(String interfaceId, NACK nack, long time) {
		// pass
	}

	@Override
	protected void processRIPMessageFromInterface(String interfaceId, RIPMessage message, long time) {
		// pass
	}
}
