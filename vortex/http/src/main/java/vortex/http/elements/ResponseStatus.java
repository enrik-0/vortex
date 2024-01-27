package vortex.http.elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.annotations.Nullable;
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
		if(state == null) {
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
		this.body = body;

		List<String> contentHeader = new ArrayList<>();
		String contentType = "Content-type";
		if (!headers.containsKey(contentType) && (body != null)) {
			if (MappingUtils.isPrimitive(body)) {
				contentHeader.add("text/plain");
				setHeader(contentType, contentHeader);
			} else {
				contentHeader.add("application/json");
				setHeader(contentType, contentHeader);
			}
		}
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

	/**
	 * is a {@link ResponseStatus}
	 * 
	 * @param object
	 * @return boolean
	 */
	public static boolean isResponse(Object object) {
		return object.getClass() == ResponseStatus.class;
	}

}
