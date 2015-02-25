package simulation;

import dispatch.Dispatcher;

public class SimulationDispatcher extends Dispatcher {

	public SimulationDispatcher(long time) {
		super(time);
	}
	
	@Override
	public void beginEpoch(long time) {
		System.out.println();
	}
	
	@Override
	public void endEpoch(long time) {
		// pass
	}

}
