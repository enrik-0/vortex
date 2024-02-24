package vortex.http.exchange;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.constants.HttpMethod;

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

	private void initialize(String uri, String method,
			Map<String, List<String>> headers) throws URISyntaxException {
		initialize(new URI(uri), method, headers);
	}
	private void initialize(URI uri, String method,
			Map<String, List<String>> headers) {
		this.uri = uri;
		this.method = HttpMethod.valueOf(method);
		this.headers = new HashMap<>();
		headers.forEach(this::addHeader);

	}
	public Request addHeader(String name, List<String> value) {
		headers.put(name, value);
		return this;

	}
	public InputStream getBody() {
		return body;

	}
	public Request setBody(InputStream body) {
		this.body = body;
		return this;

	}
	public URI getUri() {
		return uri;
	}
	public Request setUri(String uri) throws URISyntaxException {
		return setUri(new URI(uri));
	}
	public Request setUri(URI uri) {
		this.uri = uri;
		return this;
	}
	public Map<String, List<String>> getHeaders() {
		var copy = new HashMap<String, List<String>>();
		headers.forEach(copy::put);
		return copy;
	}
	public Request setHeaders(Map<String, List<String>> headers) {
		headers.forEach(this.headers::put);
		return this;
	}
	public HttpMethod getMethod() {
		return method;
	}
	public Request setMethod(String method) {
		
		return setMethod(HttpMethod.valueOf(method));
	}
	public Request setMethod(HttpMethod method) {
		this.method = method;
		return this;
	}

}
