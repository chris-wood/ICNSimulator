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
		JSONObject root = new JSONObject(lines);
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		Map<String, List<String>> nodeInterfaceMap = new HashMap<String, List<String>>();
		
		JSONArray nodeContainer = root.getJSONArray("nodes");
		for (int i = 0; i < nodeContainer.length(); i++) {
			JSONObject nodeObject = nodeContainer.getJSONObject(i);
			
			String nodeId = nodeObject.getString("node_id");
			String nodeType = nodeObject.getString("node_type");
			int xCoordinate = nodeObject.getInt("x-coord");
			int yCoordinate = nodeObject.getInt("y-coord");
			
			nodeInterfaceMap.put(nodeId, new ArrayList<String>());
			
			JSONArray interfaces = nodeObject.getJSONArray("interfaces");
			for (int j = 0; j < interfaces.length(); j++) {
				JSONObject interfaceObject = interfaces.getJSONObject(j);
				String interfaceId = interfaceObject.getString("interface_id");
				nodeInterfaceMap.get(nodeId).add(interfaceId);
			}
			
			Point location = new Point(xCoordinate, yCoordinate);
			Node node = null;
			if (nodeType.equals("consumer")) {
				node = new Consumer(nodeId, location); 
			} else if (nodeType.equals("producer")) {
				node = new Producer(nodeId, location);
			} else if (nodeType.equals("router")) {
				node = new Router(nodeId, location);
			} else {
				throw new Exception("Invalid node type: " + nodeType + ", expecting `consumer`, `router`, or `producer`");
			}
			
			nodeList.add(node);
		}
		
		JSONArray connectionContainer = root.getJSONArray("connections");
		for (int i = 0; i < connectionContainer.length(); i++) {
			JSONObject containerObject = connectionContainer.getJSONObject(i);
			String sourceId = containerObject.getString("source_id");
			String sourceInterface = containerObject.getString("source_interface");
			String destId = containerObject.getString("destination_id");
			String destInterface = containerObject.getString("destination_interface");
			
//			Node source = nodeInterfaceMap.get(key)
		}
		
		return null;
	}
	
}
