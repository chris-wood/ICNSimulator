package simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.ho.yaml.Yaml;

import ccn.network.Node;
import ccn.network.Topology;
import ccn.network.TopologyParser;
import dispatch.Clock;
import dispatch.Dispatcher;
import framework.Component;

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
			
			List<Node> nodes = topology.getNodes();
			List<Component> components = new ArrayList<Component>();
			for (int i = 0; i < nodes.size(); i++) {
				components.add(nodes.get(i));
			}
			
			Dispatcher dispatcher = new Dispatcher(config.time, components);
			
			System.err.println("Starting dispatcher");
			dispatcher.run();
			System.err.println("Ending dispatcher");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			Configuration simultationConfig = Yaml.loadType(new File(args[0]), Configuration.class);
			TopologyParser topologyParser = TopologyParser.getParserForFile(args[1]);
			
			Simulator simulation = new Simulator(simultationConfig, topologyParser);
			simulation.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
