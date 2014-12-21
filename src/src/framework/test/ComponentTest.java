package framework.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import framework.Component;
import framework.ComponentConnection;
import framework.Message;

public class ComponentTest {
	
	public ComponentTest() {
		ComponentConnection conn = new ComponentConnection();
		
		TestComponent component1 = new TestComponent();
		TestComponent component2 = new TestComponent();
		
		// topology: C1 -> C2
		conn.connect(component1, component2);
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
			super();
		}
		
		@Override
		public void cycle(long currentTime) {
			count++;
			if (count == modulus) {
				cycles++;
				Message msg = new Message("Test Cycle Message " + cycles);
				
				// this doesn't seem like the right abstraction...
				// outputInterfaces[key].put(message)
				this.outputs.get(0).putMessage(msg);
			}
			count %= modulus;
		}
		
		@Override
		public void handleInputMessage(Message m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void handleOutputMessage(Message m) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void handleEvent(Message m) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
