package vortex.http.elements;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.HttpMethod;

public class Request {

	private URI uri;
	private Map<String, List<String>> headers;
	private HttpMethod method;
	private InputStream body;

	public Request(URI uri, String method) throws URISyntaxException {
		initialize(uri, method);
	}
	public Request(String uri, String method, Map<String, List<String>> headers)
			throws URISyntaxException {
		initialize(uri, method, headers);
	}
	public Request(String uri, String method) throws URISyntaxException {
		initialize(uri, method);
	}
	public Request() {

		this.headers = new HashMap<>();
	}
	private void initialize(String uri, String method)
			throws URISyntaxException {
		initialize(uri, method, new HashMap<>());
	}
	private void initialize(URI uri, String method) throws URISyntaxException {
		initialize(uri, method, new HashMap<>());
	}
	public void addHeader(String name, List<String> value) {
		headers.put(name, value);

	}
	private void initialize(String uri, String method,
			Map<String, List<String>> headers) throws URISyntaxException {
		this.uri = new URI(uri);
		this.method = HttpMethod.valueOf(method);
		this.headers = new HashMap<>();
		headers.forEach(this::addHeader);

	}
	private void initialize(URI uri, String method, Map<String, List<String>> headers)
			throws URISyntaxException {
		this.uri = uri;
		this.method = HttpMethod.valueOf(method);
		this.headers = new HashMap<>();
		headers.forEach(this::addHeader);

	}
	
	public InputStream getBody() {
		return body;
	}
	public void setBody(InputStream body) {
		this.body = body;
		
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	public Map<String, List<String>> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}
	public HttpMethod getMethod() {
		return method;
	}
	public void setMethod(HttpMethod method) {
		this.method = method;
	}

}
