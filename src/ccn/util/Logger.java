package ccn.util;

public abstract class Logger {
	
	protected Logger nextLogger;
	protected String className;
	
	protected abstract void handleLogMessage(LogLevel level, long time, String message);

	public void log(LogLevel level, long time, String message) {
		handleLogMessage(level, time, message);
		if (nextLogger != null) {
			nextLogger.log(level, time, message);
		}
	}
	
	public static Logger getFullLogger(String className) {
		ConsoleLogger clogger = new ConsoleLogger(className);
		FileLogger flogger = new FileLogger(className);
		clogger.nextLogger = flogger;
		return clogger;
	}
	
	public static Logger getConsoleLogger(String className) {
		ConsoleLogger clogger = new ConsoleLogger(className);
		return clogger;
	}
}
