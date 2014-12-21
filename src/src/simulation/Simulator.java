package simulation;

import java.util.List;

public class Simulator implements Runnable {
	
	private List<TimeBucket> timeBuckets;
	private Config config;

	@Override
	public void run() {
		long currentTime = 0L;
		long endTime = config.getSimulationTime();
		long epoch = config.getMinimumEpochLength();
		
		while (currentTime < endTime) {
			
			if (!timeBuckets.isEmpty()) {
				TimeBucket bucket = timeBuckets.get(0);
				if (bucket.getEventTime() == currentTime) {
					while (bucket.hasNext()) {
						Event event = bucket.pop();
						
						// TODO: process the event here
					}
				}
			}
			
			currentTime += epoch; 
		}
	}

}
