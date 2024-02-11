package vortex.http.elements;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.HttpMethod;
public class ExchangeHttp {
	
	private Request request;
	private Response response;

	public ExchangeHttp() {
		
	}
	public ExchangeHttp(Request request) {
		this.request = request;
	}
	public ExchangeHttp(Request request, Response response) {
		this.request = request;
		this.response = response;
	}
	public URI getRequestURI() {
		return request.getUri();
	}
	public HttpMethod getRequestMethod() {
		return request.getMethod();
	}
	public Map<String, List<String>> getRequestHeaders(){
		return request.getHeaders();
	}
	
	public InputStream getRequestBody() {
		return request.getBody();
	}
	public Response getResponse() {
		return response;
	}
	public void setResponse(Response response) {
		this.response = response;
	}

}