package vortex.http;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.Storage;
import vortex.annotate.method.parameter.Header;
import vortex.annotate.method.parameter.HttpRequest;
import vortex.annotate.method.parameter.RequestBody;
import vortex.annotate.method.parameter.RequestParam;
import vortex.http.elements.Param;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;
import vortex.http.exchange.ExchangeHttp;
import vortex.http.exchange.Request;
import vortex.properties.exception.FormatPatternException;
import vortex.properties.kinds.Application;
import vortex.utils.MappingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RequestManager {

    private static RequestManager instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestManager.class);

    private RequestManager() {
    }

    public static RequestManager getInstance() {
	synchronized (RequestManager.class) {
	    if (instance == null) {
		instance = new RequestManager();

	    }
	}
	return instance;
    }

    public Object handle(ExchangeHttp request) throws InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException,
	    BodyException, ParameterSintaxException, RequestFormatException, UriException, FormatPatternException {
	if ((boolean) Application.DEBUG.value()) {
	    LOGGER.debug(request.getRequestURI().getPath());
	}

	var method = Storage.getInstance().getMethod(request.getRequestMethod(), request.getRequestURI().getPath());
	return executeMethod(method, request, request.getRequestMethod());
    }

    private static Object executeMethod(Method method, ExchangeHttp request, HttpMethod http)
	    throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
	    NoSuchMethodException, SecurityException, IOException, BodyException, ParameterSintaxException,
	    RequestFormatException, FormatPatternException{
	var launcher = Storage.getInstance().getObjectController(method);
	Object[] parameters = getParameters(method, request, http);
	return method.invoke(launcher, parameters);

    }

    public static Object[] getParameters(Method method, ExchangeHttp request, HttpMethod http) throws IOException,
	    BodyException, ParameterSintaxException, RequestFormatException, FormatPatternException{
	List<Param> parameters = getQueryParameters(request);
	if(method.getParameterCount() < parameters.size()) {
	    throw new RequestFormatException();
	}
	Object body;
	if (request.getRequestHeaders().get("Content-type") != null) {
	    body = MappingUtils.map(request.getRequestBody(), getBodyClass(method, http));
	    parameters.add(new Param(RequestBody.class.getName(), body));

	}
	return setParameters(method,request.getRequest(), parameters);
    }

    private static Object[] setParameters(Method method, Request request, List<Param> parameters) throws ParameterSintaxException{
	Parameter methodParameter;
	List<Object> parametersValues = new ArrayList<>();
	Parameter[] methodParameters = method.getParameters();
	Annotation[][] parameterAnnotations = method.getParameterAnnotations();
	for (int i = 0; i < method.getParameterCount() ; i++) {
	    methodParameter = methodParameters[i];
	    proccessParameters(parameters, methodParameter, parametersValues, methodParameters, parameterAnnotations,
		    i, request);
	}

	return parametersValues.toArray();
    }
    private static void proccessParameters(List<Param> parameters, Parameter methodParameter,
	    List<Object> parametersValues, Parameter[] methodParameters, Annotation[][] parameterAnnotations, int i, Request request)
	    throws ParameterSintaxException{
	Param queryParam;
	Object buffer;
	for (Annotation annotation : methodParameter.getAnnotations()) {
	    if (annotation.annotationType().isAssignableFrom(RequestBody.class)) {
		queryParam = parameters.remove(parameters.size() - 1);
	    } else if(annotation.annotationType().isAssignableFrom(Header.class) || 
		    annotation.annotationType().isAssignableFrom(HttpRequest.class)) {
		queryParam = null;
	    }
	    else {
		queryParam = parameters.remove(0);
	    }
	    mappParameter(methodParameter, queryParam, methodParameters, parametersValues, i, request);

	}
    }
    /**
     * 
     * 
     * @param methodParameter
     * @param queryParam
     * @param methodParameters
     * @param parametersValues
     * @param i
     * @throws ParameterSintaxException, Exception 
     */
    private static void mappParameter(Parameter methodParameter, Param queryParam, Parameter[] methodParameters,

	    List<Object> parametersValues, int i, Request request) {
	Object buffer;
	for (Annotation e : methodParameter.getAnnotations()) {

	    if (e.annotationType().isAssignableFrom(RequestBody.class)) {

		buffer = MappingUtils.map(queryParam.getValue(), methodParameters[i].getType());
		parametersValues.add(buffer);
	    } 
		
	    if(e.annotationType().isAssignableFrom(RequestParam.class)) {
		
		try {
		    
		if (methodParameters[i].getName().equals(queryParam.getName())) {

		    buffer = MappingUtils.map(queryParam.getValue(), methodParameters[i].getType());
		    parametersValues.add(buffer);
		} else {
		    throw new ParameterSintaxException(
			    String.format("query parameter %s must have the same name as the method parameter %s",
				    queryParam.getName(), methodParameter.getName()));
		}
		}catch(ParameterSintaxException paramException) {
		    throw new RuntimeException(paramException);

		    
		}
	    }
	    if(e.annotationType().isAssignableFrom(Header.class)) {
		System.out.println(e.toString());
		String requestedHeader = e.toString().split("\\(")[1].substring(1).substring(0,
			e.toString().split("\\(")[1].substring(1).length() -2 );
		List<String> list = request.getHeader(requestedHeader);
		if(list == null) {
		    buffer = null;
		    parametersValues.add(buffer);
		}else
		if(list.size() == 1) {
		    parametersValues.add(list.get(0));
		}
		else {
		    parametersValues.add(list);
		}
	    }
	    if(e.annotationType().isAssignableFrom(HttpRequest.class)) {

		parametersValues.add(request);
	    }

	}}


    

    private static List<Param> getQueryParameters(ExchangeHttp request) throws FormatPatternException {
	String[] queryParams;
	String[] paramElements;
	Param parameter;
	List<Param> parameters = new ArrayList<>();
	if (request.getRequestURI().getQuery() != null) {

	    queryParams = request.getRequestURI().getQuery().split("&");
	    for (String queryParam : queryParams) {
		paramElements = queryParam.split("=");
		for (var i = 0; i < paramElements.length; i += 2) {
		    parameter = new Param(paramElements[i], paramElements[i + 1]);
		    parameters.add(parameter);
		}
	    }
	}
	return parameters;

    }

    public static Class<?> getBodyClass(Executable method, HttpMethod http)
	    throws BodyException, RequestFormatException {

	if (http == HttpMethod.GET) {
	    throw new RequestFormatException(String.format("%s dont use body", HttpMethod.GET.name()));
	}

	byte count = 0;
	Class<?> clazz = null;
	Annotation[][] parameterAnnotations = method.getParameterAnnotations();

	for (int i = 0; i < parameterAnnotations.length; i++) {
	    for (Annotation annotation : parameterAnnotations[i]) {
		if (annotation.annotationType().equals(RequestBody.class)) {
		    count++;
		    clazz = method.getParameters()[i].getType();
		}

	    }

	}
	if (count > 1) {
	    throw new BodyException("The request must have only 1 parameter with @RequestBody");
	}
	if (count < 1) {
	    throw new BodyException("The request needs an parameter with @RequestBody");
	}

	return clazz;
    }
}
