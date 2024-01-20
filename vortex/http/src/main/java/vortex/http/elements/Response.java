package vortex.http.elements;

import java.util.Map;
/**
 * @autor Enrique Javier Villar Cea
 */
public interface Response{

	HttpStatus getStatus();
	void setStatus(HttpStatus state);
	Map<String, Object> getHeaders();
	void setHeader(String name, Object value);
	Object getBody();
;
}
