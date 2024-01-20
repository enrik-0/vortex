package vortex.http.elements;

import java.util.HashMap;
import java.util.Map;

import vortex.annotate.annotations.Nullable;

/**
 * @autor Enrique Javier Villar Cea
 */
public class ResponseStatus<T> implements Response{

	private HttpStatus state;
	private T body;
	private Map<String, Object> headers;
	public ResponseStatus(@Nullable T body) {
		this.body = body;
		headers = new HashMap<>();
	}
	public ResponseStatus(HttpStatus status, @Nullable T body) {
		state = status;
		this.body = body;
		headers = new HashMap<>();
	}
	public HttpStatus getStatus() {
		return state;
	}
	public void setStatus(HttpStatus state) {
		this.state = state;
	}
	public T getBody() {
		return body;
	}
	public void setBody(T body) {
		this.body = body;
	}
	public Map<String, Object> getHeaders() {
		Map<String, Object> map = new HashMap<>();
		map.putAll(headers);
		return map;

	}
	public void setHeader(String name, Object value) {
		this.headers.put(name, value);
	}

	public static boolean isResponse(Object object) {
		return object.getClass() == ResponseStatus.class;
	}
}
