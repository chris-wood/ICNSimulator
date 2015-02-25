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
	
	@Override
	public void write(String source, Event event) {
		if (event instanceof Message) {
			Message message = (Message) event;
			int sizeInBits = message.getSizeInBits();
			int numberOfEpochs = ((int) Math.ceil(sizeInBits / bitsPerEpoch));
			write(source, event, numberOfEpochs);
		} else {
			super.write(source, event);
		}
	}

}
