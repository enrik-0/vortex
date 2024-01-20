package vortex.http.utils;

import java.util.function.Supplier;

import javax.annotation.Nullable;

public final class Asserttions {

	public static void isTrue(boolean decision, Supplier<String> message) {
		if (!decision) {
			throw new IllegalArgumentException(nullSafeGet(message));
		}
	}

	@Nullable
	private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
		return (messageSupplier != null ? messageSupplier.get() : null);
	}
}
