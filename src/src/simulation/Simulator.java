package simulation;

import java.util.ArrayList;
import java.util.List;

import framework.Component;

public class Simulator implements Runnable {
	
	private List<Component> components;
	private Config config;
	
	public Simulator(List<Component> sourceComponents, Config config) {
		this.components = new ArrayList<Component>();
		for (Component component : sourceComponents) {
			this.components.add(component);
		}
		this.config = config;
		Clock.initialize(config.getSimulationTime()); // global truth
	}
	
	public void cycleComponents() {
		for (Component component : components) {
			component.crankOutput(Clock.time);
		}
		for (Component component : components) {
			component.crankInput(Clock.time);
		}
	}

	@Override
	public void run() {
		while (Clock.isTimeLeft()) {
			cycleComponents();
			Clock.tick();
		}
	}

}
