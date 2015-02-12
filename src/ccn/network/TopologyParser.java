package ccn.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import json.JSONArray;
import json.JSONObject;

public class TopologyParser {
	
	private String lines;

	private TopologyParser(String lines) {
		this.lines = lines;
	}
	
	public TopologyParser getParserForFile(String fileName) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(fileName));
		
		StringBuilder lines = new StringBuilder();
		while (scanner.hasNextLine()) {
			lines.append(scanner.nextLine());
		}
		scanner.close();
		
		TopologyParser parser = new TopologyParser(lines.toString());
		return parser;
	}
	
	public Topology parse() {
		JSONObject root = new JSONObject(lines);
		
		ArrayList<Node> nodeList = new ArrayList<Node>();
		JSONArray nodeContainer = root.getJSONArray("nodes");
		for (int i = 0; i < nodeContainer.length(); i++) {
			JSONObject nodeObject = nodeContainer.getJSONObject(i);
			
			String nodeId = nodeObject.getString("node_id");
			String nodeType = nodeObject.getString("node_type");
			int xCoordinate = nodeObject.getInt("x-coord");
			int yCoordinate = nodeObject.getInt("y-coord");
			
			JSONArray interfaces = nodeObject.getJSONArray("interfaces");
			for (int j = 0; j < interfaces.length(); j++) {
				JSONObject interfaceObject = interfaces.getJSONObject(j);
				String interfaceId = interfaceObject.getString("interface_id");
				String interfaceType = interfaceObject.getString("interface_type");
				
				
			}
		}
		
		JSONArray connectionContainer = root.getJSONArray("connections");
		
		return null;
	}
	
}
