package framework.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import framework.Component;
import framework.Message;
import framework.MessageQueue;

public class ComponentTest {
	
	public ComponentTest() {
		
		TestComponent component1 = new TestComponent();
		TestComponent component2 = new TestComponent();
		
		List<Component> components = new ArrayList<Component>();
		components.add(component1);
		components.add(component2);
		
		// TODO: create the simulation and then crank five times, see if the message pops out on the other end
		
		// topology: C1 -> C2
//		component1.addOutputConnection(component2, 0);
		MessageQueue queue = new MessageQueue("connection");
		component1.addOutputQueue(queue);
		component2.addInputQueue(queue);
	}

	@Test
	public void testAssertArrayEquals() {
		assertTrue(true);
	}
	
	class TestComponent extends Component {
		
		int cycles = 0;
		int count = 0;
		int modulus = 5; // not important

		public TestComponent() {
			super("TestComponent");
		}

		@Override
		public void handleInputMessage(Message msg) {
			System.out.println("Received message: " + msg.toString());
		}

		@Override
		public void cycle(long currentTime) {
			count++;
			if (count == modulus) {
				cycles++;
				Message msg = new Message("Test Cycle Message " + cycles);
				sendMessage("stack", msg);
			}
			count %= modulus;
		}
		
	}
	
}
