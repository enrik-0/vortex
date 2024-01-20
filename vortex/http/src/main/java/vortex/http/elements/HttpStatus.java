package vortex.http.elements;

/**
 * 
 * @autor Enrique Javier Villar Cea
 */
public enum HttpStatus implements HttpStateCode {

	/**
	 * {@code} 200 OK Request successful
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.1">RFC </a>
	 * 
	 */
	OK(200, "Request succesfull"),
	
	
	/**
	 * {@code} 201 CREATED resource created
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.2">RFC </a>
	 * 
	 */
	CREATED(201, "resource created"),
	/**
	 * {@code} 202 ACCEPTED accepted for processing
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.2.3">RFC </a>
	 * 
	 */
	ACCEPTED(202, "accepted for processing"),
	
	/**
	 * {@code} 400 BAD_REQUEST  sintax error
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.1">RFC </a>
	 * 
	 */
	BAD_REQUEST(400, "sintax error"),
	/**
	 * {@code} 401 UNATHORIZED  need authentication
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.2">RFC </a>
	 * 
	 */	
	UNAUTHORIZED(401, "need authentication"),
	/**
	 * {@code} 403 FORBIDDEN  forbidden access for the resource
	 * <a href=
	 * "https://datatracker.ietf.org/doc/html/rfc2616#section-10.4.3">RFC </a>
	 * 
	 */	
	FORBIDDEN(403, "forbidden access for the resource");

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
