package vortex.http.elements;

import java.util.HashMap;
import java.util.Map;

public class ResponseStatusException extends Exception implements Response {

	private static final long serialVersionUID = 1L;
	private HttpStatus status;
	private String body;
	private Map<String, Object> headers;

	public ResponseStatusException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.body = message;
		this.headers = new HashMap<>();
	}

	public static boolean isResponse(Object object) {
		return object.getClass() == ResponseStatus.class;
	}

	public String getBody() {
		return body;
	}
	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(HttpStatus state) {

	}

	@Override
	public Map<String, Object> getHeaders() {
		return headers;
	}

	@Override
	public void setHeader(String name, Object value) {

	}
}
