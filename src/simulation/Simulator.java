package simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;

import ccn.entity.Node;
import ccn.network.Link;
import ccn.network.Topology;
import ccn.network.TopologyParser;

public class Simulator extends Thread {
	
	private long simulationTime;
	private TopologyParser topologyParser;
	private SimulationDispatcher dispatcher;
	
	public Simulator(long configTime, TopologyParser topologyParser) {
		this.simulationTime = configTime;
		this.topologyParser = topologyParser;
	}
	
	@Override
	public void run() {
		try {
			Topology topology = topologyParser.parse();
			dispatcher = new SimulationDispatcher(simulationTime);
			
			for (Node node : topology.getNodes()) {
				node.setDispatcher(dispatcher);
			}
			for (Link link : topology.getLinks()) {
				dispatcher.addActor(link);
			}
			
			dispatcher.run();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public List<String> getStatistics() {
		return dispatcher.generateCSVStatistics();
	}
	
	public static void main(String[] args) {
		try {
			String topologyFileName = args[1];
			TopologyParser topologyParser = TopologyParser.getParserForFile(topologyFileName);
			
			Simulator simulator = new Simulator(1000L, topologyParser);
			simulator.run();
			simulator.join();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(topologyFileName.replaceFirst(".json", ".csv")));
			List<String> statistics = simulator.getStatistics();
			for (String stat : statistics) {
				System.out.println(stat);
				writer.write(stat);
				writer.newLine();
			}
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
