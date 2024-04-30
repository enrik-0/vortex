package vortex.http.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.http.status.HttpStatus;


public class ResponseStatusException extends Exception implements Response{

	/**
	 * 
	 */
	private HttpStatus status;
	private String body;
	private Map<String, List<String>> headers;

	public ResponseStatusException(HttpStatus status) {
		super();
		this.status = status;
		this.headers = new HashMap<>();


		
	}
	public ResponseStatusException(HttpStatus status, String message) {
		super(message);
		this.status = status;
		this.body = message;
		this.headers = new HashMap<>();
	}

	public static boolean isResponse(Object object) {
		return object.getClass() == ResponseStatusException.class;
	}

	public String getBody() {
		return body;
	}
	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public ResponseStatusException setStatus(HttpStatus state) {
		this.status = state;
		return this;

	}

	@Override
	public Map<String, List<String>> getHeaders() {
		Map<String, List<String>> map = new HashMap<>();
		map.putAll(headers);
		return map;
	}

	@Override
	public ResponseStatusException setHeader(String name, List<String> value) {
		if (name != null) {
			this.headers.put(name, value);
		}
		return this;
	}
	

	public ResponseStatusException setBody(String body) {
		this.body = body;
		return this;
	}

	@Override
	public ResponseStatusException setHeader(String name, String value) {
		var list = new ArrayList<String>();
		list.add(value);
		setHeader(name, list);
		return this;
		
	}
}
