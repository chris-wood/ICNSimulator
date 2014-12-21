package framework.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import framework.Component;
import framework.ComponentInterface;
import framework.Message;

public class ComponentTest {
	
	public ComponentTest() {
		ComponentInterface output = new ComponentInterface();
		List<ComponentInterface> outputs = new ArrayList<ComponentInterface>();
		outputs.add(output);
		
		ComponentInterface input = new ComponentInterface();
		List<ComponentInterface> inputs = new ArrayList<ComponentInterface>();
		inputs.add(input);
		
		TestComponent component = new TestComponent(inputs, outputs);
	}

	@Test
	public void testAssertArrayEquals() {
		assertTrue(true);
	}
	
	class TestComponent extends Component {
		
		int cycles = 0;
		int count = 0;
		int modulus = 5;

		public TestComponent(List<ComponentInterface> inputInterfaces,
				List<ComponentInterface> outputInterfaces) {
			super(inputInterfaces, outputInterfaces);
		}
		
		@Override
		public void cycle(long currentTime) {
			count++;
			if (count == modulus) {
				cycles++;
				Message msg = new Message("Test Cycle Message " + cycles);
				
				// this doesn't seem like the right abstraction...
				this.outputInterfaces.get(0).insertOutputMessage(msg);
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
