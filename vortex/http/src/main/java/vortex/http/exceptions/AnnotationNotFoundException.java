package vortex.http.exceptions;


/**
 * @author
 */
public class AnnotationNotFoundException extends Exception {

	/**
	 * 
	 */
	public AnnotationNotFoundException() {
	}

	/**
	 * @param message
	 */
	public AnnotationNotFoundException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AnnotationNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AnnotationNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AnnotationNotFoundException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
