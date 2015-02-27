package ccn.util;

public class FileLogger extends Logger {
	
	public FileLogger(String name) {
		className = name;
	}

	@Override
	protected void handleLogMessage(LogLevel level, long time, String message) {
		// pass
	}

}
