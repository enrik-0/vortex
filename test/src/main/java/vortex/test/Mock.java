package vortex.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.http.ServerHttp;



public final class Mock {

	private static Mock instance;
	
	private Mock() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException {
		ServerHttp.runServer();
	}
	/**
	 * 
	 * @return {@link Mock}
	 * @throws IOException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws UriException 
	 * @throws InitiateServerException 
	 */
	public static Mock getInstance() throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, UriException, InitiateServerException{
		if(instance == null) {
			instance = new Mock();
		}
		return instance;
	}

	
	public static void stop() {
		ServerHttp.stopServer();
    instance = null;
	} 


}
