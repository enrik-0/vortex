package vortex.http;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import vortex.properties.exception.FormatPatternException;
import vortex.utils.Asserttions;
import vortex.utils.MappingUtils;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class Handler implements HttpHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

    @Override
    public void handle(HttpExchange request) throws IOException {
	var exchange = new ExchangeHttp();
	Object responseBody = null;
	try {
	    checkMethod(request.getRequestMethod(),
		    () -> String.format("HttpMethod %s not implemented yet", request.getRequestMethod()));
	    exchange = new ExchangeHttp(createRequest(request));
	    responseBody = RequestManager.getInstance().handle(exchange);
	} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
	    System.out.println(e.getMessage());
	    exchange.setResponse(new ResponseStatusException(HttpStatus.BAD_REQUEST));
	} catch (InvocationTargetException e) {
	    if(e.getTargetException() instanceof ResponseStatusException) {
		exchange.setResponse((ResponseStatusException) e.getTargetException());
	    }else {
		exchange.setResponse(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
	    }
	} catch (NoSuchMethodException | SecurityException | IOException | ParameterSintaxException
		| RequestFormatException | UriException | URISyntaxException e) {
	    ResponseStatus<String> response = new ResponseStatus<>(HttpStatus.NOT_FOUND, null);
	    exchange.setResponse(response);
	} catch (BodyException | RuntimeException e) {
	    ResponseStatus<String> response = new ResponseStatus<>(HttpStatus.BAD_REQUEST, null);
	    exchange.setResponse(response);
	} catch (

	FormatPatternException e) {
	    LOGGER.error(e.getMessage());
	    ResponseStatus<String> response = new ResponseStatus<>(HttpStatus.BAD_REQUEST, null);
	    exchange.setResponse(response);
	} catch (ResponseStatusException e) {
	    exchange.setResponse(e);
	    LOGGER.error(String.format("Request denied by CORS POLICY %s", request.getRequestURI().toString()));
	}
	if (exchange.getResponse() == null && ResponseStatus.isResponse(responseBody)) {

	    exchange.setResponse((ResponseStatus<Object>) responseBody);

	}
	if (exchange.getResponse() == null) {
	    ResponseStatus<Object> response = new ResponseStatus<>(HttpStatus.OK, responseBody);
	    exchange.setResponse(response);
	}

	createResponse(request, exchange.getResponse());
	request.close();
    }

    private static void createResponse(HttpExchange request, Response response) throws IOException {
	response.getHeaders().forEach((String name, List<String> values) -> {
	    for (String value : values) {
		request.getResponseHeaders().add(name, value);
	    }

	});

	request.sendResponseHeaders(response.getStatus().value(), response.getBody() == null ? -1 : 0);
	if (response.getBody() != null) {
	    Object body = response.getBody();
	
	    if(!Asserttions.isPrimitive(body) && !Asserttions.isMap(body)) {
		if(Asserttions.isList(body)) {
			
		    if(((List) body).isEmpty()) {
			body = new ArrayList<>(); 
		    }else {
			if(!Asserttions.isPrimitive(((List) body).get(0))){
			    body = MappingUtils.mapListObjects((List<Object>) body, new HashMap<Object, Map<String, Object>>());
			}
		    }
		    
		}else {
		    body = MappingUtils.mapObject(body, new HashMap<Object, Map<String, Object>>());
		}
	    }
	    request.getResponseBody().write(MappingUtils.writeValueAsBytes(body));
	}
    }

    private static Request createRequest(HttpExchange exchange) throws URISyntaxException {
	var request = new Request(exchange.getRequestURI(), exchange.getRequestMethod());
	exchange.getRequestHeaders().forEach(request::addHeader);
	request.setBody(exchange.getRequestBody());

	return request;
    }

    public static void checkMethod(String method, Supplier<String> message) throws NoSuchMethodException {
	var valid = false;
	for (HttpMethod http : HttpMethod.values()) {
	    if (http.name().equals(method)) {
		valid = true;
	    }
	}
	if (!valid) {
	    throw new NoSuchMethodException(Asserttions.nullSafeGet(message));
	}
    }
}
