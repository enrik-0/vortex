package vortex.annotate.exceptions;

public class UriException extends Exception {

	public UriException() {
	}

	public UriException(String message) {
		super(message);
	}

	public UriException(Throwable cause) {
		super(cause);
	}

	public UriException(String message, Throwable cause) {
		super(message, cause);
	}

	public UriException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
