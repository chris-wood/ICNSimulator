package ccn.util;

public abstract class Logger {
	
	protected Logger nextLogger;
	
	protected abstract void handleLogMessage(LogLevel level, String message);

	public void log(LogLevel level, String message) {
		handleLogMessage(level, message);
		if (nextLogger != null) {
			nextLogger.log(level, message);
		}
	}
	
}
