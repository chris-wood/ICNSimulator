package ccn.network;

import framework.Component;

public abstract class Node extends Component {
	
	private Point location;

	public Node(String identity, Point location) {
		super(identity);
		this.location = location;
	}
}
