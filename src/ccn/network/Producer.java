package ccn.network;

import java.util.List;

import ccn.message.ContentObject;
import ccn.message.Interest;
import ccn.message.VirtualInterest;
import ccn.stack.NetworkStack;
import framework.Event;

public class Producer extends Node {
	
	protected NetworkStack stack;

	public Producer(String identity, Point location, List<String> interfaces) {
		super(identity, location, interfaces);
		stack = NetworkStack.buildProducerStack();
	}

	@Override
	protected void runComponent(long time) {
		
	}

	@Override
	protected void processInterestFromInterface(String interfaceId,
			Interest interest, long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processVirtualInterestFromInterface(String interfaceId,
			VirtualInterest interest, long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processContentObjectFromInterface(String interfaceId,
			ContentObject content, long time) {
		// TODO Auto-generated method stub
		
	}
}
