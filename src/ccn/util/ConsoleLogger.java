package ccn.util;

public class ConsoleLogger extends Logger {
	
	public ConsoleLogger(String name) {
		className = name;
	}

	@Override
	protected void handleLogMessage(LogLevel level, long time, String message) {
		switch (level) {
		case LogLevel_INFO:
			System.out.println(className + "[" + time + "]: " + message);
			break;
		case LogLevel_DEBUG:
		case LogLevel_WARNING:
			System.err.println(className + "[" + time + "]: " + message);
			break;
		default:
			// pass
		}
	}
	
}
