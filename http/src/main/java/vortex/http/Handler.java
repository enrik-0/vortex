package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;

import vortex.annotate.constants.HttpMethod;
import vortex.annotate.exceptions.UriException;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;
import vortex.http.exchange.ExchangeHttp;
import vortex.http.exchange.Request;
import vortex.http.exchange.Response;
import vortex.http.exchange.ResponseStatus;
import vortex.http.exchange.ResponseStatusException;
import vortex.http.status.HttpStatus;
import vortex.http.utils.Asserttions;
import vortex.http.utils.MappingUtils;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Handler implements HttpHandler {

	@SuppressWarnings("unchecked")
	@Override
	public void handle(HttpExchange request) throws IOException {
		var exchange = new ExchangeHttp();
		Object responseBody = null;
		try {
			Asserttions.checkMethod(request.getRequestMethod(), 
					() -> String.format("HttpMethod %s not implemented yet", request.getRequestMethod()));

			exchange = new ExchangeHttp(createRequest(request));
			responseBody = RequestManager.getInstance().handle(exchange);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println(e.getMessage());
			System.out.println(e.getTargetException());
			exchange.setResponse(
					(ResponseStatusException) e.getTargetException());
		} catch (NoSuchMethodException | SecurityException | IOException
				| BodyException | ParameterSintaxException
				| RequestFormatException | UriException
				| URISyntaxException e) {
			exchange.setResponse(
					new ResponseStatus<String>(HttpStatus.NOT_FOUND, null));
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (exchange.getResponse() == null
				&& ResponseStatus.isResponse(responseBody)) {
			exchange.setResponse((ResponseStatus<Object>) responseBody);
		}
		if (exchange.getResponse() == null) {
			exchange.setResponse(
					new ResponseStatus<Object>(HttpStatus.OK, responseBody));
		}
		createResponse(request, exchange.getResponse());
		request.close();
	}
	private static void createResponse(HttpExchange request, Response response)
			throws IOException {
		response.getHeaders().forEach((String name,List<String> values) -> {
		for (String value : values) {
			request.getResponseHeaders().add(name, value);
			}

		});

		
		request.sendResponseHeaders(response.getStatus().value(), response.getBody() == null?-1:0);
		if (response.getBody() != null) {
			request.getResponseBody()
					.write(MappingUtils.writeValueAsBytes(response.getBody()));
		}
	}

	private static Request createRequest(HttpExchange exchange)
			throws URISyntaxException {
		var request = new Request(exchange.getRequestURI(),
				exchange.getRequestMethod());
		exchange.getRequestHeaders().forEach(request::addHeader);
		request.setBody(exchange.getRequestBody());

		return request;
	}
	
}
