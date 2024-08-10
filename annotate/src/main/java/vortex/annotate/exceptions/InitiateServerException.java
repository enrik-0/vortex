package vortex.annotate.exceptions;

/**
 * Throwed if there an error initiating the server
 */
public class InitiateServerException extends Exception {

    /**
     * 
     */
    public InitiateServerException() {
    }

    /**
     * 
     * @param message message to show
     */
    public InitiateServerException(String message) {
	super(message);
    }

    /**
     * 
     * @param cause see {@link java.lang.Exception}
     */
    public InitiateServerException(Throwable cause) {
	super(cause);
    }

    /**
     * 
     * @param message message to show
     * @param cause see {@link java.lang.Exception}
     */
    public InitiateServerException(String message, Throwable cause) {
	super(message, cause);
    }

    /**
     * 
     * @param message message to show
     * @param cause see {@link java.lang.Exception}
     * @param enableSuppression see {@link java.lang.Exception}
     * @param writableStackTrace see {@link java.lang.Exception}
     */
    public InitiateServerException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}
