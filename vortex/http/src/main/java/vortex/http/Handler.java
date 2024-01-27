package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;


import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.exceptions.UriException;
import vortex.http.elements.ExchangeHttp;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.Request;
import vortex.http.elements.Response;
import vortex.http.elements.ResponseStatus;
import vortex.http.elements.ResponseStatusException;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;
import vortex.http.utils.MappingUtils;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Handler implements HttpHandler {

	private HttpServer server;

	public Handler(HttpServer server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange request) throws IOException {
		ExchangeHttp exchange = new ExchangeHttp();
		Object responseBody = null;
		try {
			checkMethod(request.getRequestMethod());
			exchange = new ExchangeHttp(createRequest(request));
			responseBody = RequestManager.getInstance().handle(exchange);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println(e.getMessage());
			exchange.setResponse(
					(ResponseStatusException) e.getTargetException());
		} catch (NoSuchMethodException | SecurityException | IOException
				| BodyException | ParameterSintaxException
				| RequestFormatException 
				| UriException | URISyntaxException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (exchange.getResponse() == null
				&& ResponseStatus.isResponse(responseBody)) {
			exchange.setResponse((ResponseStatus) responseBody);
		}
		if (exchange.getResponse() == null) {
			exchange.setResponse(
					new ResponseStatus(HttpStatus.OK, responseBody));
		}
		createResponse(request, exchange.getResponse());
		request.close();
	}
	private static void createResponse(HttpExchange request, Response response)
			throws IOException {
		response.getHeaders().forEach((name, value) -> {
			request.getResponseHeaders().add(name, String.valueOf(value));
		});
		request.sendResponseHeaders(response.getStatus().value(), 0);
		request.getResponseBody()
				.write(MappingUtils.writeValueAsBytes(response.getBody()));
	}

	private static Request createRequest(HttpExchange exchange)
			throws URISyntaxException {
		Request request = new Request(exchange.getRequestURI(),
				exchange.getRequestMethod());
		exchange.getRequestHeaders().forEach(request::addHeader);
		request.setBody(exchange.getRequestBody());

		return request;
	}
	private static void checkMethod(String method) throws Exception {
		boolean valid = false;
		for (HttpMethod http : HttpMethod.values()) {
			if (http.name().equals(method)) {
				valid = true;
			}
		}
		if (!valid) {
			throw new Exception(
					String.format("HttpMethod %s not implemented yet", method));
		}
	}
}
