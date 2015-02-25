package ccn.network;

import dispatch.channel.Channel;

public class Link extends Channel {
	
	public int bitsPerEpoch;

	public Link(String identity, int linkBitsPerEpoch) {
		super(identity);
		bitsPerEpoch = linkBitsPerEpoch;
	}

}
