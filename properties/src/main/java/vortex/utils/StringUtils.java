package vortex.utils;

import java.util.Locale;
import java.util.StringJoiner;

public final class StringUtils {
	
	private static Locale locale = Locale.getDefault();

	
	public static String proccessProperty(String separator, Class<?> clazz, String propertyName) {
		var joiner = new StringJoiner(separator);
		String packagez = clazz.getPackageName() + ".";
		String clazzName = clazz.getCanonicalName();

		clazzName = clazzName.replaceAll(packagez, "");

		joiner.add(clazzName.toLowerCase(locale));
		joiner.add(propertyName.toLowerCase(locale));
		return joiner.toString();
	}

}
