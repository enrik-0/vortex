package vortex.annotate.exceptions;

public class InitiateServerException extends Exception {

    public InitiateServerException() {
    }

    public InitiateServerException(String message) {
	super(message);
    }

    public InitiateServerException(Throwable cause) {
	super(cause);
    }

    public InitiateServerException(String message, Throwable cause) {
	super(message, cause);
    }

    public InitiateServerException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
    }

}
