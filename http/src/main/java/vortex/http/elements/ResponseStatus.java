package vortex.http.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.annotations.Nullable;
import vortex.http.utils.Asserttions;
import vortex.http.utils.MappingUtils;

/**
 * @autor Enrique Javier Villar Cea
 */
public class ResponseStatus<T> implements Response {

	private HttpStatus state;
	private T body;
	private Map<String, List<String>> headers;

	public ResponseStatus() {
		headers = new HashMap<>();
	}
	/**
	 * 
	 * @param body
	 *            <b>T</b>
	 */
	public ResponseStatus(@Nullable T body) {
		headers = new HashMap<>();
		setResponseBody(body);
	}

	/**
	 * 
	 * @param status
	 *            {@linkplain HttpStatus}
	 * @param body
	 *            <b>T</b>
	 */
	public ResponseStatus(HttpStatus status, @Nullable T body) {
		state = status;
		headers = new HashMap<>();
		setResponseBody(body);
	}

	public HttpStatus getStatus() {
		if (state == null) {
			state = HttpStatus.OK;
		}
		return state;
	}

	public void setStatus(HttpStatus state) {
		this.state = state;
	}

	public T getBody() {
		return body;
	}
	/**
	 * if headers dont have a <b>content-type</b> header <br>
	 * the header is added if <b> body is primitive </b> as <b> text/plain</b>
	 * <br>
	 * if is not <b> primitive</b> is added as <b>application/json</b>
	 * 
	 * @param body
	 *            T
	 */
	public void setResponseBody(T body) {
		var contentType = "Content-type";
		List<String> contentHeader = new ArrayList<>();
		this.body = body;
		Asserttions.setContentHeader(contentHeader, body,
				headers.get(contentType));
		setHeader(contentType, contentHeader);
	}

	/**
	 * 
	 * @return Map(String, List(String)
	 */
	public Map<String, List<String>> getHeaders() {
		Map<String, List<String>> map = new HashMap<>();
		map.putAll(headers);
		return map;

	}
	/**
	 * @param name
	 *            String
	 * @param value
	 *            List(String)
	 */
	public void setHeader(String name, List<String> value) {
		if (name != null) {
			this.headers.put(name, value);
		}
	}
	
	public void setHeader(String name, String value) {
		var list = new ArrayList<String>();
		list.add(value);
		setHeader(name, list);
		
	}

	/**
	 * is a {@link ResponseStatus}
	 * 
	 * @param object
	 * @return boolean
	 */
	public static boolean isResponse(Object object) {
		return object instanceof ResponseStatus<?>;
	}

}
