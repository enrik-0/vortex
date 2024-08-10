package vortex.annotate.exceptions;

/**
 * Exception throwed when an error is caused by an {@link java.net.URI} the
 * exception is also used to indicate if a given uri exists
 */
public class UriException extends Exception {

    /**
     * 
     */
    public UriException() {
    }

    /**
     * 
     * @param message message to show
     */
    public UriException(String message) {
	super(message);
    }

    /**
     * 
     * @param cause see {@link java.lang.Exception}
     */
    public UriException(Throwable cause) {
	super(cause);
    }

    /**
     * 
     * @param message message to show
     * @param cause see {@link java.lang.Exception}
     */
    public UriException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * 
     * @param message message to show
     * @param cause see {@link java.lang.Exception}
     * @param enableSuppression see {@link java.lang.Exception}
     * @param writableStackTrace see {@link java.lang.Exception}
     */
    public UriException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}
