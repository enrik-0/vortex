package vortex.http.utils;

import java.util.regex.Pattern;

public final class Regex {
	private  static final Pattern REAL = Pattern.compile("-?\\d+");
	private static  final Pattern FLOATING = Pattern.compile("-?\\d+\\.?\\d*");
	private Regex() {
	}
	public static boolean isBoolean(String value) {
		return "true".compareToIgnoreCase(value) == 0|| "false".compareToIgnoreCase(value) == 0;
	}
	public static boolean isIntegerOrLong(String value) {
		return REAL.matcher(value).matches();
	}
	public static boolean isFloating(String value) {
		return FLOATING.matcher(value).matches();
	}
}
