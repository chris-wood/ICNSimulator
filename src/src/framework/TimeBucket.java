package framework;

import java.util.List;

public class TimeBucket {

	private long eventTime;
	private List<Event> events;
	
	public long getEventTime() {
		return eventTime;
	}
	
	public boolean hasNext() {
		return !events.isEmpty();
	}
	
	public Event pop() {
		Event e = events.get(0);
		events.remove(0);
		return e;
	}
	
	public Event next() {
		return events.get(0);
	}
	
	public void remove() {
		events.remove(0);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("TimeBucket[" + eventTime + "] = [");
		if (!events.isEmpty()) {
			for (int i = 0; i < events.size() - 1; i++) {
				builder.append(events.get(i) + ",");
			}
			builder.append(events.get(events.size() - 1));
		}
		builder.append("]");
		
		return builder.toString();
	}
	
}
