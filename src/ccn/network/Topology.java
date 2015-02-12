package ccn.network;

import java.util.ArrayList;
import java.util.List;

public class Topology {
	
	private List<Node> nodes;
	
	public Topology() {
		this.nodes = new ArrayList<Node>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}

}
