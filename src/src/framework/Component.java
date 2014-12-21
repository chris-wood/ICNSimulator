package framework;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {
	
	protected List<ComponentInterface> inputInterfaces;
	protected List<ComponentInterface> outputInterfaces;
	
	public Component(List<ComponentInterface> inputInterfaces, List<ComponentInterface> outputInterfaces) {
		this.inputInterfaces = new ArrayList<ComponentInterface>();
		for (ComponentInterface ci : inputInterfaces) {
			this.inputInterfaces.add(ci);
		}
		
		this.outputInterfaces = new ArrayList<ComponentInterface>();
		for (ComponentInterface ci : outputInterfaces) {
			this.outputInterfaces.add(ci);
		}
	}

	public abstract void cycle(long currentTime);
	public abstract void handleInputMessage(Message m);
	public abstract void handleOutputMessage(Message m);
	public abstract void handleEvent(Message m);
	
//    .init          = NULL, /* init */
//    .open          = NULL, /* open */
//    .upcallRead    = NULL, /* upcall read */
//    .upcallEvent   = NULL,
//    .downcallRead  = NULL, /* downcall read */
//    .downcallEvent = NULL,
//    .close         = NULL, /* closer */
//    .release       = NULL  /* release */
	
}
