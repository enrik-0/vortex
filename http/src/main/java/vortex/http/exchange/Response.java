package vortex.http.exchange;

import java.util.List;
import java.util.Map;

import vortex.http.status.HttpStatus;
/**
 * @autor Enrique Javier Villar Cea
 */
public interface Response{

	/**
	 * @return {@linkplain HttpStatus}
	 */
	HttpStatus getStatus();
	/**
	 * 
	 * @param state {@link HttpStatus}
	 */
	Response setStatus(HttpStatus state);
	/**
	 * @return Map(String, List(String))
	 */
	Map<String, List<String>> getHeaders();
	Response setHeader(String name, List<String> value);
	Response setHeader(String name, String value);
	/**
	 * @return T or null 
	 */
	Object getBody();
}
