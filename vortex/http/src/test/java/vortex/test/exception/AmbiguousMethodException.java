package vortex.test.exception;

public class AmbiguousMethodException extends Exception {

	public AmbiguousMethodException() {
	}

	public AmbiguousMethodException(String message) {
		super(message);
	}

	public AmbiguousMethodException(Throwable cause) {
		super(cause);
	}

	public AmbiguousMethodException(String message, Throwable cause) {
		super(message, cause);
	}

	public AmbiguousMethodException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
