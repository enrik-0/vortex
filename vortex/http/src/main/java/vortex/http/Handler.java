package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.NotImplementedException;

import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.exceptions.UriException;
import vortex.http.elements.HttpStatus;
import vortex.http.elements.Response;
import vortex.http.elements.ResponseStatus;
import vortex.http.elements.ResponseStatusException;
import vortex.http.exceptions.AnnotationNotFoundException;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;

import com.sun.net.httpserver.HttpHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class Handler implements HttpHandler {
	private HttpServer server;

	public Handler(HttpServer server) {
		this.server = server;
	}

	@Override
	public void handle(HttpExchange request) throws IOException {
		Response response = null;
		Object responseBody = null;
		try {

			checkMethod(request.getRequestMethod());
			responseBody = RequestManager.getInstance().handle(request);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println(e.getMessage());
			response = (ResponseStatusException) e.getTargetException();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BodyException e) {
			e.printStackTrace();
		} catch (AnnotationNotFoundException e) {
			e.printStackTrace();
		} catch (ParameterSintaxException e) {
			e.printStackTrace();
		} catch (RequestFormatException e) {
			e.printStackTrace();
		} catch (NotImplementedException e) {
			e.printStackTrace();
		} catch (UriException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (response == null && ResponseStatus.isResponse(responseBody)) {
			response = (ResponseStatus) responseBody;
		}
		if (response == null) {
			response = new ResponseStatus(HttpStatus.OK, responseBody);
		}
		createResponse(request, response);
		request.close();
	}
	private static void createResponse(HttpExchange request, Response response)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		response.getHeaders().forEach((name, value) -> {
			request.getResponseHeaders().add((String) name,
					String.valueOf(value));
		});
		request.sendResponseHeaders(response.getStatus().value(), 0);
		request.getResponseBody()
				.write(mapper.writeValueAsBytes(response.getBody()));
	}
	private static void checkMethod(String method) {
		boolean valid = false;
		for (HttpMethod http : HttpMethod.values()) {
			if (http.name().equals(method)) {
				valid = true;
			}
		}
		if (!valid) {
			throw new NotImplementedException(
					String.format("HttpMethod %s not implemented yet", method));
		}
	}
}
