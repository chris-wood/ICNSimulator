package simulation;

import dispatch.Dispatcher;

public class SimulationDispatcher extends Dispatcher {

	public SimulationDispatcher(long time) {
		super(time);
	}
	
	@Override
	public void beginEpoch() {
		System.out.println();
	}

}
