package framework;

import java.util.ArrayList;
import java.util.List;

public abstract class Component {
	
	protected List<ComponentConnection> inputs;
	protected List<ComponentConnection> outputs;
	
	public Component() {
		this.inputs = new ArrayList<ComponentConnection>();
		this.outputs = new ArrayList<ComponentConnection>();
	}
	
	public void addInput(ComponentConnection conn) {
		inputs.add(conn);
	}
	
	public void addOutput(ComponentConnection conn) {
		outputs.add(conn);
	}

	public abstract void cycle(long currentTime);
	public abstract void handleInputMessage(Message m);
	public abstract void handleOutputMessage(Message m);
	public abstract void handleEvent(Message m);
	
//    .upcallRead    = NULL, /* upcall read */
//    .upcallEvent   = NULL,
//    .downcallRead  = NULL, /* downcall read */
//    .downcallEvent = NULL,
	
}
