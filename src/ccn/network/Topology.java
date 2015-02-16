package ccn.network;

import java.util.ArrayList;
import java.util.List;

import ccn.entity.Node;

public class Topology {
	
	private List<Node> nodes;
	
	public Topology() {
		this.nodes = new ArrayList<Node>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

}
