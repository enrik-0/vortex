package vortex.test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vortex.annotate.annotations.HttpMethod;
import vortex.http.elements.HttpStatus;
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
	private Object body;

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

	public RequestBuilder setBody(Object set) {

		body = set;
		return this;
	}
	/**
	 * As this returns a copy of the headers modifications on it don't have any
	 * result in the builder.
	 * 
	 * @return {@link #clone()}
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
	 * Sets request method to {@link HttpMethod.POST}
	 * 
	 * @param uri
	 */
	public RequestBuilder post(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.POST);
		return this;
	}
	/**
	 * Sets request method to {@link HttpMethod.PUT}
	 * 
	 * @param uri
	 */
	public RequestBuilder put(String uri) throws AmbiguousMethodException {
		setUp(uri, HttpMethod.PUT);
		return this;
	}
	/**
	 * Sets request method to {@link HttpMethod.DELETE}
	 * 
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

	/**
	 * 
	 * @param name
	 * @param value
	 * @return {@link #RequestBuilder}
	 */
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
		connection.setDoOutput(true);
		try {
			connection.setRequestMethod(this.method.name());
			List<String> contentHeaders = new ArrayList<>();
			Asserttions.setContentHeader(contentHeaders, body,
					headers.get("Content-type")).forEach(value -> {
						this.addHeader("Content-type", value);
					});
			setHeaders(connection);
			createBody(connection);
			// connection.setConnectTimeout(this.timeout);

			return createResponse(connection, connection.getInputStream());
		} catch (FileNotFoundException e) {
			return createResponse(connection, connection.getErrorStream());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static Response createResponse(HttpURLConnection connection,
			InputStream input) throws IOException {
		Response response = new ResponseStatus(
				getResponseBody(connection, input));
		response.setStatus(HttpStatus.resolve(connection.getResponseCode()));
		connection.getHeaderFields().forEach(response::setHeader);

		return response;

	}

	private void createBody(HttpURLConnection connection)
			throws JsonProcessingException {
		if (body != null) {
			var bytes = MappingUtils.writeValueAsBytes(body);
			ObjectMapper mapper = new ObjectMapper();

			bytes = mapper.writeValueAsString(body).getBytes();
			bytes = mapper.writeValueAsBytes(body);

			try (OutputStream os = connection.getOutputStream()) {
				os.write(bytes, 0, bytes.length);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Object getResponseBody(HttpURLConnection connection,
			InputStream inputStream) throws IOException {
		try {

			Object mapped = null;
			var byteArrayOutputStream = copyInputStream(inputStream);

			String contentHeader = connection.getHeaderField("Content-type");
			if ("application/json".equals(contentHeader)) {

				try {
					mapped = MappingUtils.map(
							byteArrayOutputStream.toByteArray(), Map.class);
				} catch (Exception e) {
					mapped = MappingUtils.map(
							byteArrayOutputStream.toByteArray(),
							ArrayList.class);
				}
			} else {
				mapped = getResponseBody(byteArrayOutputStream.toByteArray());
			}

			return mapped;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	private static Object getResponseBody(byte[] content) throws IOException {
		Long temp;
		char[] chars;
		Object body = null;
		var builder = new StringBuilder();
		String buffer = MappingUtils
				.getInputContent(new ByteArrayInputStream(content));

		if (buffer.startsWith("\"") && buffer.endsWith("\"")) {
			chars = buffer.toCharArray();
			for (var i = 1; i < chars.length - 1; i++) {
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
		int longitud;
		var buffer = new byte[AMMOUNT_TO_READ];
		var byteArrayOutputStream = new ByteArrayOutputStream();

		while ((longitud = inputStream.read(buffer)) != -1) {
			byteArrayOutputStream.write(buffer, 0, longitud);
		}

		return byteArrayOutputStream;
	}
}
