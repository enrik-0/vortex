package vortex.http.status;

/**
 * 
 * @autor Enrique Javier Villar Cea
 */
public enum HttpStatus implements HttpStateCode {

	/**
	 * {@code} 101 SWITCHING_PROTOCOLS The server understands and is willing to
	 * comply with the client's request <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.1.2">RFC </a>
	 */
	SWITCHING_PROTOCOLS(101,
			"The server understands and is willing to comply with the client's request"),
	/**
	 * 
	 * 
	 * {@code} 200 OK Request successful <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.1">RFC </a>
	 * 
	 */
	OK(200, "Request succesfull"),

	/**
	 * {@code} 201 CREATED resource created <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.2">RFC </a>
	 * 
	 */
	CREATED(201, "resource created"),
	/**
	 * {@code} 202 ACCEPTED accepted for processing <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.3">RFC </a>
	 * 
	 */
	ACCEPTED(202, "accepted for processing"),
	/**
	 * {@code} 203 NON_AUTHORITATIVE_INFORMATION The returned metainformation in
	 * the entity-header is not the definitive set as available from the origin
	 * server <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.4">RFC </a>
	 * 
	 */
	NON_AUTHORITATIVE_INFORMATION(203,
			" The returned metainformation in the entity-header is not the"
					+ " definitive set as available from the origin server"),
	/**
	 * {@code} 204 NO_CONTENT The server has fulfilled the request but does not
	 * need to return an entity-body <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.5">RFC </a>
	 * 
	 */
	NO_CONTENT(204,
			"The server has fulfilled the request but does not need to return an entity-body"),

	/**
	 * {@code} 205 RESET_CONTENT The server has fulfilled the request and the
	 * user agent SHOULD reset the document view which caused the request to be
	 * sent <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.6">RFC </a>
	 * 
	 */
	RESET_CONTENT(205,
			"The server has fulfilled the request and the user agent SHOULD reset\r\n"
					+ "   the document view which caused the request to be sent"),

	/**
	 * {@code} 206 PARTIAL_CONTENT The server has fulfilled the partial GET
	 * request for the resource. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.7">RFC </a>
	 * 
	 */
	PARTIAL_CONTENT(206,
			"The server has fulfilled the partial GET request for the resource."),

	/**
	 * {@code} 300 MULTIPLE_CHOICES The requested resource corresponds to any
	 * one of a set of representations <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.1">RFC </a>
	 * 
	 */
	MULTIPLE_CHOICES(300,
			"The requested resource corresponds to any one of a set of"
					+ "   representations"),
	/**
	 * {@code} 301 MOVED_PERMANENTLY The requested resource has been assigned a
	 * new permanent URI and any future references to this resource SHOULD use
	 * one of the returned URIs <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.2">RFC </a>
	 * 
	 */
	MOVED_PERMANENTLY(301,
			"The requested resource has been assigned a new permanent URI and any"
					+ "   future references to this resource SHOULD use one of the returned"
					+ "   URIs"),

	/**
	 * {@code} 302 FOUND The requested resource resides temporarily under a
	 * different URI. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.3">RFC </a>
	 * 
	 */

	FOUND(302,
			" The requested resource resides temporarily under a different URI"),
	/**
	 * {@code} 303 FOUND The response to the request can be found under a
	 * different URI and SHOULD be retrieved using a GET method on that
	 * resource. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.4">RFC </a>
	 * 
	 */
	SEE_OTHER(303,
			"The response to the request can be found under a different URI and"
					+ "   SHOULD be retrieved using a GET method on that resource."),

	/**
	 * {@code} 304 NOT_MODIFIED If the client has performed a conditional GET
	 * request and access is allowed, but the document has not been modified
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.5">RFC </a>
	 * 
	 */
	NOT_MODIFIED(304,
			" If the client has performed a conditional GET request and access is"
					+ "   allowed, but the document has not been modified"),

	/**
	 * {@code} 305 USE_PROXY The requested resource MUST be accessed through the
	 * proxy given by the Location field <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.6">RFC </a>
	 * 
	 */
	USE_PROXY(305,
			" The requested resource MUST be accessed through the proxy given by"
					+ "   the Location field"),

	/**
	 * {@code} 307 TEMPORARY_REDIRECT The requested resource resides temporarily
	 * under a different URI. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.3.8">RFC </a>
	 * 
	 */
	TEMPORARY_REDIRECT(307,
			" The requested resource resides temporarily under a different URI"),
	/**
	 * 
	 * {@code} 400 BAD_REQUEST syntax error <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.1">RFC </a>
	 * 
	 */
	BAD_REQUEST(400, "sintax error"),
	/**
	 * {@code} 401 UNATHORIZED need authentication <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.2">RFC </a>
	 * 
	 */
	UNAUTHORIZED(401, "need authentication"),
	/**
	 * {@code} 402 PAYMENT_REQUIRED Payment Required <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.3">RFC </a>
	 * 
	 */
	PAYMENT_REQUIRED(402, "Payment Required"),
	/**
	 * {@code} 403 FORBIDDEN forbidden access for the resource <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.4">RFC </a>
	 * 
	 */
	FORBIDDEN(403, "forbidden access for the resource"),
	/**
	 * {@code} 404 NOT_FOUND resource not found <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.5">RFC </a>
	 * 
	 */
	NOT_FOUND(404, "resource not found"),
	/**
	 * {@code} 405 METHOD_NOT_ALLOWED The method specified in the Request-Line
	 * is not allowed <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.6">RFC </a>
	 * 
	 */
	METHOD_NOT_ALLOWED(405,
			"The method specified in the Request-Line is not allowed"),
	/**
	 * {@code} 406 NOT_ACCEPTABLE he resource identified by the request is only
	 * capable of generating response entities <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.7">RFC </a>
	 * 
	 */
	NOT_ACCEPTABLE(406,
			"the resource identified by the request is only capable of generating"
					+ "   response entities"),

	/**
	 * {@code} 407 PROXY_AUTHENTICATION_REQUIRED the client must first
	 * authenticate itself with the proxy <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.8">RFC </a>
	 * 
	 */
	PROXY_AUTHENTICATION_REQUIRED(407,
			"the client must first authenticate itself with the proxy"),
	/**
	 * {@code} 408 REQUEST_TIMEOUT The client did not produce a request within
	 * the time that the server was prepared to wait <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.9">RFC </a>
	 * 
	 */
	REQUEST_TIMEOUT(408,
			"The client did not produce a request within the time that the server"
					+ "   was prepared to wait"),
	/**
	 * {@code} 409 CONFLICT The request could not be completed due to a conflict
	 * with the current state of the resource was prepared to wait <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.10">RFC </a>
	 * 
	 */
	CONFLICT(409,
			"The request could not be completed due to a conflict with the current"
					+ "   state of the resource"),
	/**
	 * {@code} 410 GONE The requested resource is no longer available at the
	 * server and no forwarding address is known <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.11">RFC </a>
	 * 
	 */
	GONE(410,
			" The requested resource is no longer available at the server and no"
					+ "   forwarding address is known"),
	/**
	 * {@code} 411 LENGTH_REQUIRED The server refuses to accept the request
	 * without a defined Content- Length. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.12">RFC </a>
	 * 
	 */
	LENGTH_REQUIRED(411,
			"The server refuses to accept the request without a defined Content-Length"
					+ "   Length"),
	/**
	 * {@code} 412 PRECONDITION_FAILED the precondition given in one or more of
	 * the request-header fields evaluated to false when it was tested on the
	 * server. <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.13">RFC </a>
	 */
	PRECONDITION_FAILED(412,
			"he precondition given in one or more of the request-header fields"
					+ "   evaluated to false when it was tested on the server."),

	/**
	 * {@code} 413 REQUEST_ENTITY_TOO_LARGE The server is refusing to process a
	 * request because the request entity is larger than the server is willing
	 * or able to process <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.14">RFC </a>
	 * 
	 */
	REQUEST_ENTITY_TOO_LARGE(413,
			" The server is refusing to process a request because the request"
					+ "   entity is larger than the server is willing or able to process"),

	/**
	 * {@code} 414 REQUEST_URI_TOO_LONG The server is refusing to service the
	 * request because the Request-URI is longer than the server is willing to
	 * interpret <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.15">RFC </a>
	 * 
	 */
	REQUEST_URI_TOO_LONG(414,
			" The server is refusing to service the request because the Request-URI"
					+ "   is longer than the server is willing to interpret"),

	/**
	 * {@code} 415 UNSUPPORTED_MEDIA_TYPE format not supported 
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.16">RFC </a>
	 * 
	 */
	UNSUPPORTED_MEDIA_TYPE(415, "format not supported"),
	/**
	 * {@code} 416  AREQUEST_RANGE_NOT_SATISFIABLE A server SHOULD return a response with this status code if a request
   included a Range request-header field 
   <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.17">RFC </a>
	 * 
	 */
	REQUEST_RANGE_NOT_SATISFIABLE(416, "A server SHOULD return a response with this status code if a request"
			+ "   included a Range request-header field"),
	
	/**
	 * {@code} 417 EXPECTATION_FAILED The expectation given in an Expect request-header field could not be melt
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.18">RFC </a>
	 * 
	 */
	EXPECTATION_FAILED(417, "The expectation given in an Expect request-header field could not be melt"),
	/**
	 * {@code} 500 INTERNAL_SERVER_ERROR The server encountered an unexpected condition which prevented it
   from fulfilling the request.
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.5.1">RFC </a>
	 * 
	 */
INTERNAL_SERVER_ERROR(500, " The server encountered an unexpected condition which prevented it"
		+ "   from fulfilling the request."),
/**
 * {@code} 501 NOT_IMPLEMENTED  The server does not support the functionality required to fulfill the
   request
 * <a href=
 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.5.2">RFC </a>
 * 
 */	
NOT_IMPLEMENTED(501, " The server does not support the functionality required to fulfill the"
		+ "   request"),
	/**
	 * {@code} 502 BAD_GATEWAY  The server, while acting as a gateway or proxy, received an invalid
   response
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.5.3">RFC </a>
	 * 
	 */	
	BAD_GATEWAY(502, "The server, while acting as a gateway or proxy, received an invalid"
			+ "   response"),
	
	
	/**
	 * {@code} 501 SERVICE_UNAVAILABLE  The server is currently unable to handle the request
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.5.4">RFC </a>
	 * 
	 */
	SERVICE_UNAVAILABLE(503, "The server is currently unable to handle the request"),
	
	/**
	 * {@code} 504 GATEWAY_TIMEOUT   The server, while acting as a gateway or proxy, did not receive a
   timely response from the upstream server specified by the URI
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.5.5">RFC </a>
	 * 
	 */
GATEWAY_TIMEOUT(504," The server, while acting as a gateway or proxy, did not receive a"
		+ "   timely response from the upstream server specified by the URI");

	private final int state;
	private final Series series;
	private final String meaning;

	HttpStatus(int state, String meaning) {
		this.state = state;
		this.series = Series.valueOf(state);
		this.meaning = meaning;
	}

	@Override
	public int value() {
		return state;
	}

	public String getMeaning() {
		return meaning;
	}

	@Override
	public boolean is1xxInformational() {
		return Series.INFORMATIONAL == series;
	}

	@Override
	public boolean is2xxSuccessful() {
		return Series.SUCCESSFUL == series;
	}

	@Override
	public boolean is3xxRedirection() {
		return Series.REDIRECTION == series;
	}

	@Override
	public boolean is4xxClientError() {
		return Series.CLIENT_ERROR == series;
	}

	@Override
	public boolean is5xxServerError() {
		return Series.SERVER_ERROR == series;
	}

	@Override
	public boolean isError() {
		return is5xxServerError() || is4xxClientError();
	}

	public static HttpStatus resolve(int code) {
		for (HttpStatus status : HttpStatus.values()) {
			if (status.value() == code) {
				return status;
			}
		}
		return null;
	}

}
