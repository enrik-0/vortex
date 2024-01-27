package vortex.test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import static java.util.Locale.ROOT;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import vortex.annotate.annotations.HttpMethod;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.Request;
import vortex.http.elements.Response;
import vortex.http.elements.ResponseStatus;
import vortex.http.utils.Asserttions;
import vortex.http.utils.MappingUtils;
import vortex.http.utils.Regex;
import vortex.test.exception.AmbiguousMethodException;

public class RequestBuilder {

	private static final int AMMOUNT_TO_READ = 1024;
	private static final int SECOND_10 = 10000;
	private String uri;
	private HttpMethod method;
	private int timeout = SECOND_10;
	private Map<String, String> headers;
	public RequestBuilder() {
		headers = new HashMap<>();
	}

	public RequestBuilder(Map<String, String> headers) {
		this.headers = new HashMap<>();
		headers.forEach(this::addHeader);
	}

	public String getRequestUri() {
		return uri;
	}

	public HttpMethod getRequestMethod() {
		return method;
	}

	public int getTimeout() {
		return timeout;
	}

	/**
	 * As this returns a copy of the headers modifications on it don't have any
	 * result in the builder.
	 * 
	 * @return a copy of the headers
	 */
	public Map<String, String> getRequestHeaders() {
		Map<String, String> copy = new HashMap<>();
		headers.forEach(copy::put);
		return copy;

	}
	private void setUp(String uri, HttpMethod method)
			throws AmbiguousMethodException {
		if (this.method == null) {
			this.uri = uri;
			this.method = method;
		} else {
			throw new AmbiguousMethodException(String.format(
					"%s %s %s %s %s %s",
					"Creating a request using 2 methods at the same time is not allowed",
					"\n", "request already have ", this.method.name(),
					"and trying to put", method.name()));
		}

	}
	/**
	 * Sets request method to {@link HttpMethod.GET}
	 * 
	 * @param uri
	 */
	public RequestBuilder get(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.GET);
		return this;
	}
	/**
	 * @param uri
	 */
	public RequestBuilder post(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.POST);
		return this;
	}
	/**
	 * @param uri
	 */
	public RequestBuilder put(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.PUT);
		return this;
	}
	/**
	 * @param uri
	 */
	public RequestBuilder delete(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.DELETE);
		return this;
	}

	/**
	 * Sets a specified timeout value, in milliseconds, to be used when opening
	 * a communications link to the resource referenced by this uri. If the
	 * timeout expires before the connection can be established, a
	 * {@link java.net.SocketTimeoutException} is raised. A timeout of zero is
	 * interpreted as an infinite timeout.
	 * 
	 * @param timeout
	 */
	public RequestBuilder setTimeout(int timeout) {
		this.timeout = timeout;
		return this;
	}

	public RequestBuilder addHeader(String name, String value) {
		headers.put(name, value);
		return this;

	}
	private RequestBuilder setHeaders(HttpURLConnection connection) {
		headers.forEach(connection::setRequestProperty);
		return this;

	}
	/**
	 * Sends the request and return the response
	 * 
	 * @return {@link Response}
	 * @throws MalformedURLException
	 * @throws SocketException
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public Response perform() throws IOException {
		URL url = new URL(uri);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(this.method.name());
		connection.setConnectTimeout(this.timeout);
		setHeaders(connection);
		return createResponse(connection);
	}

	private static Response createResponse(HttpURLConnection connection)
			throws IOException {
		Response response = new ResponseStatus(getResponseBody(connection));
		response.setStatus(HttpStatus
				.valueOf(connection.getResponseMessage().toUpperCase(ROOT)));
		connection.getHeaderFields().forEach(response::setHeader);
		response.getHeaders().forEach(RequestBuilder::print);

		return response;

	}

	private static Object getResponseBody(HttpURLConnection connection)
			throws IOException {
		Object mapped = null;
		ObjectMapper mapper = new ObjectMapper();
		InputStream input = connection.getInputStream();
		String content;
		ByteArrayOutputStream byteArrayOutputStream = copyInputStream(input);

		String contentHeader = connection.getHeaderField("Content-type");
		if ("[application/json]".equals(contentHeader)) {

			try {
				mapped = mapper.readValue(byteArrayOutputStream.toByteArray(),
						Map.class);
			} catch (Exception e) {
				mapped = MappingUtils.map(byteArrayOutputStream.toByteArray(),
						ArrayList.class);
			}
		} else {
			mapped = getResponseBody(byteArrayOutputStream.toByteArray());
		}

		return mapped;

	}
	private static Object getResponseBody(byte[] content) throws IOException {
		Long temp;
		char[] chars;
		Object body = null;
		StringBuilder builder = new StringBuilder();
		String buffer = MappingUtils
				.getInputContent(new ByteArrayInputStream(content));

		if (buffer.startsWith("\"") && buffer.endsWith("\"")) {
			chars = buffer.toCharArray();
			for (int i = 1; i < chars.length - 1; i++) {
				builder.append(chars[i]);
			}
			buffer = builder.toString();
		}
		body = buffer;
		if (Regex.isBoolean(buffer)) {
			body = MappingUtils.map(content, Boolean.class);
		} else if (Regex.isIntegerOrLong(buffer)) {
			temp = (Long) MappingUtils.map(content, Long.class);
			if (Asserttions.inrange(temp, Byte.MAX_VALUE, Byte.MIN_VALUE)) {
				body = MappingUtils.map(content, byte.class);

			} else if (Asserttions.inrange(temp, Integer.MAX_VALUE,
					Integer.MIN_VALUE)) {
				body = MappingUtils.map(buffer, Integer.class);
			} else {
				body = temp;
			}
		} else {
			if (Regex.isFLOATING(buffer)) {
				body = MappingUtils.map(content, Double.class);
			}
		}

		return body;
	}

	private static ByteArrayOutputStream copyInputStream(
			InputStream inputStream) throws IOException {
		byte[] buffer = new byte[AMMOUNT_TO_READ];
		int longitud;
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		while ((longitud = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, longitud);
		}

		return byteArrayOutputStream;
	}
	private static void print(String key, Object value) {
		System.out.println(String.format("nombre %s valor %s", key, value));
	}

}
