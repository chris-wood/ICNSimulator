package ccn.util;

public class ConsoleLogger extends Logger {

	@Override
	protected void handleLogMessage(LogLevel level, String message) {
		switch (level) {
		case LogLevel_INFO:
			System.out.println(message);
			break;
		case LogLevel_DEBUG:
		case LogLevel_WARNING:
			System.err.println(message);
			break;
		default:
			// pass
		}
	}
	
}
