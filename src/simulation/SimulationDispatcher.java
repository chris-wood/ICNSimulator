package simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
	
	public List<String> generateCSVStatistics() {
		List<String> stats = new ArrayList<String>();
		for (int i = 0; i < this.actors.size(); i++) {
			if (this.actors.get(i) instanceof Node) {
				Node node = (Node) this.actors.get(i);
				Stream<String> nodeStats = node.generateCSVStatisticsStream();
				stats.addAll(nodeStats.collect(Collectors.toList()));
			}
		}
		return stats;
	}

}
