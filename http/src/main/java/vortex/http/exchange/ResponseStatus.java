package vortex.http.exchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.bytecode.stackmap.TypeData.UninitThis;
import vortex.annotate.annotations.Nullable;
import vortex.http.status.HttpStatus;
import vortex.http.utils.Asserttions;

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
	public ResponseStatus(HttpStatus status) {
		state = status;
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

	public ResponseStatus<T> setStatus(HttpStatus state) {
		this.state = state;
		return this;
	}

	public T getBody() {
		return body;
	}
	/**
	 * if headers don't have a <b>content-type</b> header <br>
	 * the header is added if <b> body is primitive </b> as <b> text/plain</b>
	 * <br>
	 * if is not <b> primitive</b> is added as <b>application/json</b>
	 * 
	 * @param body
	 *            T
	 */
	public ResponseStatus<T> setResponseBody(T body) {
		var contentType = "Content-type";
		List<String> contentHeader = new ArrayList<>();
		this.body = body;
		Asserttions.setContentHeader(contentHeader, body,
				headers.get(contentType));
		if(!contentHeader.isEmpty()) {
			setHeader(contentType, contentHeader);
			
		}
		return this;
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
	@Override
	public ResponseStatus<T> setHeader(String name, List<String> value) {
		if (name != null) {
			this.headers.put(name, value);
		}
		return this;
	}
	
	@Override
	public ResponseStatus<T> setHeader(String name, String value) {
		var list = new ArrayList<String>();
		list.add(value);
		setHeader(name, list);
		return this;
		
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
