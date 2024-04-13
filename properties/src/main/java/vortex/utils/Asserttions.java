package vortex.utils;

import java.util.List;
import java.util.function.Supplier;
public final class Asserttions {

	private Asserttions() {

	}
	public static void isTrue(boolean decision, Supplier<String> message) {
		if (!decision) {
			throw new IllegalArgumentException(nullSafeGet(message));
		}
	}
	public static void isFalse(boolean decision, Supplier<String> message) {
		if (decision) {
			throw new IllegalArgumentException(nullSafeGet(message));
		}
	}

	public static String nullSafeGet(
			Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

	public static boolean inRange(long value, long max, long min) {
		return value >= min && value <= max;

	}

	public static boolean inRange(int value, int max, int min) {
      return inRange((long) value, (long) max, (long) min);
}

	public static boolean inRange(byte value, byte max, byte min) {
      return inRange((long) value, (long) max, (long) min);

}		public static boolean isPrimitive(Object body) {
		return isPrimitive(body.getClass(), Integer.class, Double.class,
				Boolean.class, Character.class, Byte.class, Short.class,
				Long.class, Float.class, String.class)
				|| body.getClass().isPrimitive();
	}
	private static boolean isPrimitive(Class<?> bodyClass,
			Class<?>... classes) {
		for (Class<?> clazz : classes) {
			if (bodyClass == clazz) {
				return true;
			}
		}
		return false;
	}
	public static List<String> setContentHeader(List<String> contentHeaders,
			Object body, Object contentHeader) {
		if (contentHeader == null && (body != null)) {
			if (isPrimitive(body)) {
				contentHeaders.add("text/plain");
			} else {
				contentHeaders.add("application/json");
			}
		}
		return contentHeaders;
	}

}
