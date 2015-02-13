package ccn.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import json.JSONArray;
import json.JSONObject;

public class TopologyParser {
	
	private String lines;

	private TopologyParser(String lines) {
		this.lines = lines;
	}
	
	public static TopologyParser getParserForFile(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		
		StringBuilder lines = new StringBuilder();
		while (scanner.hasNextLine()) {
			lines.append(scanner.nextLine());
		}
		scanner.close();
		
		TopologyParser parser = new TopologyParser(lines.toString());
		return parser;
	}
	
	public Topology parse() throws Exception {
		Topology topology = new Topology();
		
		JSONObject root = new JSONObject(lines);
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Map<String, Node> nodeMap = new HashMap<String, Node>();
		
		JSONArray nodeContainer = root.getJSONArray("nodes");
		for (int i = 0; i < nodeContainer.length(); i++) {
			JSONObject nodeObject = nodeContainer.getJSONObject(i);
			
			String nodeId = nodeObject.getString("node_id");
			String nodeType = nodeObject.getString("node_type");
			int xCoordinate = nodeObject.getInt("x-coord");
			int yCoordinate = nodeObject.getInt("y-coord");
			
			JSONArray interfaceContainer = nodeObject.getJSONArray("interfaces");
			List<String> interfaces = new ArrayList<String>();
			for (int j = 0; j < interfaceContainer.length(); j++) {
				JSONObject interfaceObject = interfaceContainer.getJSONObject(j);
				String interfaceId = interfaceObject.getString("interface_id");
				interfaces.add(interfaceId);
			}
			
			Point location = new Point(xCoordinate, yCoordinate);
			Node node = null;
			if (nodeType.equals("consumer")) {
				node = new Consumer(nodeId, location, interfaces); 
			} else if (nodeType.equals("producer")) {
				node = new Producer(nodeId, location, interfaces);
			} else if (nodeType.equals("router")) {
				node = new Router(nodeId, location, interfaces);
			} else {
				throw new Exception("Invalid node type: " + nodeType + ", expecting `consumer`, `router`, or `producer`");
			}
			
			nodeList.add(node);
			topology.addNode(node);
			nodeMap.put(nodeId, node);
		}
		
		JSONArray channelContainer = root.getJSONArray("channels");
		Map<String, Channel> channels = new HashMap<String, Channel>();
		for (int i = 0; i < channelContainer.length(); i++) {
			JSONObject channelObject = channelContainer.getJSONObject(i);
			String channelId = channelObject.getString("channel_id");
			int dataRate = channelObject.getInt("data_rate");
			Channel channel = new Channel(channelId, dataRate);
			channels.put(channelId, channel);
		}
		
		JSONArray connectionContainer = root.getJSONArray("connections");
		for (int i = 0; i < connectionContainer.length(); i++) {
			JSONObject containerObject = connectionContainer.getJSONObject(i);
			String sourceId = containerObject.getString("source_id");
			String sourceInterface = containerObject.getString("source_interface");
			String destId = containerObject.getString("destination_id");
			String destInterface = containerObject.getString("destination_interface");
			String channelId = containerObject.getString("channel_id");
			
			Node source = nodeMap.get(sourceId);
			Node dest = nodeMap.get(destId);
			
			source.addDuplexQueue(sourceInterface, channels.get(channelId));
			dest.addDuplexQueue(destInterface, channels.get(channelId));
		}
		
		return topology;
	}
	
}
