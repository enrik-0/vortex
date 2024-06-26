package vortex.properties.kinds;
import vortex.properties.filemanager.PropertyParser;
import vortex.utils.StringUtils;
public enum Server implements Family {

	/**
	 * <h1>Defines the listening port of the application
	 * default port is <bold > 80</bold></h1>
	 */
	PORT, 
	/**
	 *<h1> Defines the base URL for the application 
	 *default path is <bold> / </bold> </h1>
	 * 
	 */
	CONTEXT_PATH,
	
	
	THREAD_NUMBER;
	private Object value;

	Server() {

		value = PropertyParser.getInstance().get(StringUtils
				.proccessProperty(".", this.getClass(), this.name()));
	}
	@Override
	public Object value() {
		return value;
	}
}
