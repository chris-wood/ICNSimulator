package ccn.network;

import java.util.ArrayList;
import java.util.List;

import ccn.entity.Node;

public class Topology {
	
	private List<Node> nodes;
	private List<Link> links;
	
	public Topology() {
		this.nodes = new ArrayList<Node>();
		this.links = new ArrayList<Link>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void addLink(Link link) {
		links.add(link);
	}
	
	public List<Node> getNodes() {
		return nodes;
	}
	
	public List<Link> getLinks() {
		return links;
	}

}
