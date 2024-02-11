package vortex.http.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import vortex.annotate.annotations.Nullable;

public final class Asserttions {

	public static void isTrue(boolean decision, Supplier<String> message) {
		if (!decision) {
			throw new IllegalArgumentException(nullSafeGet(message));
		}
	}

	@Nullable
	private static String nullSafeGet(
			@Nullable Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}

	public static boolean inrange(long value, long max, long min) {
		return value >= min && value <= max;

	}

	public static List<String> setContentHeader(List<String> contentHeaders,
			Object body, Object contentHeader) {
		if (contentHeader == null && (body != null)) {
			if (MappingUtils.isPrimitive(body)) {
				contentHeaders.add("text/plain");
			} else {
				contentHeaders.add("application/json");
			}
		}
		return contentHeaders;
	}

}
