package framework;

public class Connection {
	
	protected int queueIndex;
	protected Component component;
	
	public Connection(Component component, int queueIndex) {
		this.queueIndex = queueIndex;
		this.component = component;
	}
	
	public int getQueueIndex() {
		return queueIndex;
	}
	
	public Component getComponent() {
		return component;
	}
	
	public void insertMessage(Message msg) {
		component.receiveMessage(queueIndex, msg);
	}

}
