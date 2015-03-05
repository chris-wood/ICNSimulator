package simulation;

import java.io.File;
import java.util.List;

import org.ho.yaml.Yaml;

import ccn.entity.Node;
import ccn.network.Link;
import ccn.network.Topology;
import ccn.network.TopologyParser;
import dispatch.Dispatcher;

public class Simulator extends Thread {
	
	private Configuration config;
	private TopologyParser topologyParser;
	
	public Simulator(Configuration simConfig, TopologyParser topologyParser) {
		this.config = simConfig;
		this.topologyParser = topologyParser;
	}
	
	@Override
	public void run() {
		try {
			Topology topology = topologyParser.parse();
			
			SimulationDispatcher dispatcher = new SimulationDispatcher(config.time);
			
			for (Node node : topology.getNodes()) {
				node.setDispatcher(dispatcher);
			}
			for (Link link : topology.getLinks()) {
				dispatcher.addActor(link);
			}
			
			System.err.println("Starting dispatcher");
			dispatcher.run();
			System.err.println("Displaying statistics");
			dispatcher.displayStats();
			System.err.println("Ending dispatcher");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			// TODO: need cmdline parser 
			Configuration simultationConfig = Yaml.loadType(new File(args[0]), Configuration.class);
			TopologyParser topologyParser = TopologyParser.getParserForFile(args[1]);
			
			Simulator simulation = new Simulator(simultationConfig, topologyParser);
			simulation.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
