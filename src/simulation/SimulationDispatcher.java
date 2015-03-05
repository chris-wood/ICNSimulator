package simulation;

import ccn.entity.Node;
import dispatch.Dispatcher;

public class SimulationDispatcher extends Dispatcher {

	public SimulationDispatcher(long time) {
		super(time);
	}
	
	@Override
	public void beginEpoch(long time) {
		// pass
	}
	
	@Override
	public void endEpoch(long time) {
		// pass
	}
	
	public void computeStatistics() {
		// TODO: later
	}
	
	public void displayStats() {
		for (int i = 0; i < this.actors.size(); i++) {
			if (this.actors.get(i) instanceof Node) {
				Node node = (Node) this.actors.get(i);
				node.displayStatistics();
			}
		}
	}

}
