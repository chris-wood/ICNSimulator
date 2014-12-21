package simulation;

import java.util.List;

import framework.Component;
import framework.TimeBucket;

public class Simulator implements Runnable {
	
	private List<Component> components;
	private Config config;

	@Override
	public void run() {
		long currentTime = 0L;
		long endTime = config.getSimulationTime();
		
		while (currentTime < endTime) {
			if (!components.isEmpty()) {
				Component component = components.get(0);
				component.cycle(currentTime);
			}
			currentTime++; // time is measured in epochs 
		}
	}

}
