package simulation;

import java.io.File;
import java.util.List;

import org.ho.yaml.Yaml;

import ccn.entity.Node;
import ccn.network.Link;
import ccn.network.Topology;
import ccn.network.TopologyParser;

public class Simulator extends Thread {
	
	private long simulationTime;
	private TopologyParser topologyParser;
	
	public Simulator(long configTime, TopologyParser topologyParser) {
		this.simulationTime = configTime;
		this.topologyParser = topologyParser;
	}
	
	@Override
	public void run() {
		try {
			Topology topology = topologyParser.parse();
			
			SimulationDispatcher dispatcher = new SimulationDispatcher(simulationTime);
			
			for (Node node : topology.getNodes()) {
				node.setDispatcher(dispatcher);
			}
			for (Link link : topology.getLinks()) {
				dispatcher.addActor(link);
			}
			
			dispatcher.run();
			
			List<String> stats = dispatcher.generateCSVStatistics();
			for (String stat : stats) {
				System.out.println(stat);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			TopologyParser topologyParser = TopologyParser.getParserForFile(args[1]);
			Simulator simulation = new Simulator(1000L, topologyParser);
			simulation.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
