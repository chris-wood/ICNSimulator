package framework;

public class ComponentConnection {

	protected ComponentInterface head;
	protected ComponentInterface tail;
	
	public void connect(ComponentInterface c1, ComponentInterface c2) {
		this.head = c1;
		this.tail = c2;
	}
	
}
