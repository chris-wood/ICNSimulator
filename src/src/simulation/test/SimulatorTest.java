package simulation.test;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import simulation.Config;
import simulation.Simulator;
import framework.Component;
import framework.Message;
import framework.MessageQueue;

public class SimulatorTest {
	
	public SimulatorTest() {
		
		// 1. Create all of the components
		TestComponent component1 = new TestComponent(1);
		TestComponent component2 = new TestComponent(2);
		
		List<Component> components = new ArrayList<Component>();
		components.add(component1);
		components.add(component2);
		
		// 2. Create the topology
		// topology: C1 -> C2
		MessageQueue queue = new MessageQueue("connection");
		try {
			component1.connect(component2, queue);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// 3. Create a config object for the test
		Config config = new Config();
		config.minimumEpochLength = 1;
		config.simulationTime = 6;
		
		// 4. Create the simulation
		Simulator sim = new Simulator(components, config);
		
		sim.run(); // GO!
	}
	
	@Test
	public void testComponentMessageFlow() {
		// TODO: pump the message through the components, see if it gets displayed
		
		assertTrue(true);
	}
	
	class TestComponent extends Component {
		
		int cycles = 0;
		int count = 0;
		int modulus = 5; // not important

		public TestComponent(int id) {
			super("TestComponent-" + id);
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
				sendMessage("connection", msg);
			}
			count %= modulus;
		}
		
	}

}
