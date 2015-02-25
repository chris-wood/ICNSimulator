package ccn.network;

import ccn.message.Message;
import dispatch.channel.Channel;
import dispatch.event.Event;

public class Link extends Channel {
	
	public int bitsPerSecond;

	public Link(String identity, int linkBitsPerSecond) {
		super(identity);
		bitsPerSecond = linkBitsPerSecond;
	}
	
//	@Override
//	public void write(String source, Event event) {
//		if (event instanceof Message) {
//			Message message = (Message) event;
//			int size = message.getSize();
//		}
//	}

}
