package net.schedge.cards;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Logger {
	
	public static final int FATAL = 0;
	public static final int ERROR = 1;
	public static final int EHROR = 2;
	public static final int WARN = 3;
	public static final int INFO = 4;
	public static final int DEBUG = 5;
	
	private static final String[] CODES = {"FATAL", "ERROR", "EHROR", "WARN", "INFO", "DEBUG"};
	
	public static int errLevel = 2;
	public static int outLevel = 4;
	
	public static boolean onlyErrors = true;
	
	public static void msg(int level, String message) {
		if(level <= errLevel) {
			System.err.println("[" + Instant.now().truncatedTo(ChronoUnit.SECONDS) + " " + CODES[level] + "] " + message);
		} else if(!onlyErrors && level <= outLevel) {
			System.out.println("[" + Instant.now() + " " + CODES[level] + "] " + message);
		}
	}
	
	public static void fatal(String message) {
		msg(FATAL, message);
	}
	
	public static void error(String message) {
		msg(ERROR, message);
	}
	
	public static void ehror(String message) {
		msg(EHROR, message);
	}
	
	public static void warn(String message) {
		msg(WARN, message);
	}
	
	public static void info(String message) {
		msg(INFO, message);
	}
	
	public static void debug(String message) {
		msg(DEBUG, message);
	}

}
