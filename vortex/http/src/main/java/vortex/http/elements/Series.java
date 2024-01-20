package vortex.http.elements;
import vortex.annotate.annotations.Nullable;
/**
 * Enumeration of HTTP status series.
 * <p>
 * Retrievable via {@link HttpStatus#series()}.
 */
public enum Series {

	INFORMATIONAL(1),
	SUCCESSFUL(2), 
	REDIRECTION(3), 
	CLIENT_ERROR(4), 
	SERVER_ERROR(5);

	private final int value;

	Series(int value) {
		this.value = value;
	}

	/**
	 * Return the integer value of this status series. Ranges from 1 to 5.
	 */
	public int value() {
		return this.value;
	}


	/**
	 * Return the {@code Series} enum constant for the supplied status code.
	 * 
	 * @param statusCode
	 *            the HTTP status code (potentially non-standard)
	 * @return the {@code Series} enum constant for the supplied status code
	 * @throws IllegalArgumentException
	 *             if this enum has no corresponding constant
	 */
	public static Series valueOf(int statusCode) {
		Series series = resolve(statusCode);
		if (series == null) {
			throw new IllegalArgumentException(
					"No matching constant for [" + statusCode + "]");
		}
		return series;
	}

	/**
	 * Resolve the given status code to an {@code HttpStatus.Series}, if
	 * possible.
	 * 
	 * @param statusCode
	 *            the HTTP status code (potentially non-standard)
	 * @return the corresponding {@code Series}, or {@code null} if not found
	 */
	@Nullable
	public static Series resolve(int statusCode) {
		int seriesCode = statusCode / HttpStateCode.MINIMUN_STATE;
		for (Series series : values()) {
			if (series.value == seriesCode) {
				return series;
			}
		}
		return null;
	}
}
