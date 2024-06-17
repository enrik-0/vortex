package kik.framework.vortex.databasemanager.exception;

public class RelationTypeException extends Exception {

    public RelationTypeException() {
	super("The relation that is trying to create doesn't exists yet");
    }

    public RelationTypeException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public RelationTypeException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

    public RelationTypeException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public RelationTypeException(String message, Throwable cause, boolean enableSuppression,
	    boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

}
