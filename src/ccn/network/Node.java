package ccn.network;

import framework.Component;

public abstract class Node extends Component {
	
	private Point location;

	public Node(String identity, Point location) {
		super(identity);
		this.location = location;
	}
	
	public void addInputInterface(String identity) throws Exception {
		this.addInputQueue(identity);
	}

	public void addOutputInterface(String identity) throws Exception {
		this.addOutputQueue(identity);
	}
}
