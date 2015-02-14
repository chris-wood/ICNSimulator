package ccn.network;

import java.util.ArrayList;
import java.util.List;

import framework.Channel;
import framework.Component;

public abstract class Node extends Component {
	
	protected Point location;
	protected List<String> interfaces;

	public Node(String identity, Point location, List<String> interfaces) {
		super(identity);
		this.location = location;
		this.interfaces = new ArrayList<String>();
		this.interfaces.addAll(interfaces);
		for (String interfaceId : interfaces) {
			Channel channel = new Channel(interfaceId);
			this.addChannelInterface(channel.getIdentity(), channel);
		}
	}
}
