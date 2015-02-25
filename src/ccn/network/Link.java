package ccn.network;

import ccn.message.Message;
import dispatch.channel.Channel;
import dispatch.event.Event;

public class Link extends Channel {
	
	public int bitsPerEpoch;

	public Link(String identity, int linkBitsPerEpoch) {
		super(identity);
		bitsPerEpoch = linkBitsPerEpoch;
	}

}
