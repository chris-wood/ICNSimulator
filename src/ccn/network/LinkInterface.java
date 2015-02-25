package ccn.network;

import ccn.message.Message;
import dispatch.channel.ChannelInterface;
import dispatch.event.Event;

public class LinkInterface extends ChannelInterface {
	
	private int writeBitsPerEpoch;
	
	public LinkInterface(String interfaceIdentity, int interfaceWriteBitsPerEpoch) {
		super(interfaceIdentity);
		writeBitsPerEpoch = interfaceWriteBitsPerEpoch;
	}

	@Override
	public void write(Event event) {
		if (event instanceof Message) {
			Message message = (Message) event;
			int sizeInBits = message.getSizeInBits();
			int numberOfEpochs = ((int) Math.ceil(sizeInBits / writeBitsPerEpoch));
			write(event, numberOfEpochs);
		} else {
			super.write(event);
		}
	}

}
