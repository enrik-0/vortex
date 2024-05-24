package vortex.http.exceptions;

/**
 * @autor Enrique Javier Villar Cea
 */
public class BodyException extends Exception {

	/**
	 * 
	 */
	public BodyException() {
	}

	/**
	 * @param message
	 */
	public BodyException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public BodyException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public BodyException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public BodyException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
