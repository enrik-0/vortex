package vortex.http.elements;

import java.util.List;
import java.util.Map;
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
	void setStatus(HttpStatus state);
	/**
	 * @return Map(String, List(String))
	 */
	Map<String, List<String>> getHeaders();
	void setHeader(String name, List<String> value);
	/**
	 * @return T or null 
	 */
	Object getBody();

}
