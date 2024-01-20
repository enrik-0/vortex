package vortex.http;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;

import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestParam;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.Storage;
import vortex.http.elements.Param;
import vortex.http.exceptions.AnnotationNotFoundException;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;

public final class RequestManager {

	private static RequestManager instance;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(RequestManager.class);

	private RequestManager() {
	}
	public static RequestManager getInstance() {
		if (instance == null) {
			instance = new RequestManager();
		}
		return instance;
	}

	public Object handle(HttpExchange request) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException,
			IOException, BodyException, AnnotationNotFoundException,
			ParameterSintaxException, RequestFormatException, UriException {
		LOGGER.debug(request.getRequestURI().getPath());

		Method method = Storage.getInstance().getMethod(
				HttpMethod.valueOf(request.getRequestMethod()),
				request.getRequestURI().getPath());
		method.getAnnotatedExceptionTypes();

		return executeMethod(method, request,
				HttpMethod.valueOf(request.getRequestMethod()));

	}

	private Object executeMethod(Method method, HttpExchange request,
			HttpMethod http) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException,
			IOException, BodyException, AnnotationNotFoundException,
			ParameterSintaxException, RequestFormatException {
		Object launcher;
		Object[] parameters;
		launcher = Storage.getInstance().getObjectController(method);
		parameters = getParameters(method, request, http);
		return method.invoke(launcher, parameters);

	}

	private Object[] getParameters(Method method, HttpExchange request,
			HttpMethod http)
			throws IOException, BodyException, AnnotationNotFoundException,
			ParameterSintaxException, RequestFormatException {
		List<Param> parameters = getQueryParameters(request);
		Object body;
		if (request.getRequestHeaders().get("content-type") != null) {
			body = getBody(request, getBodyClass(method, http));
			parameters.add(new Param(RequestBody.class.getName(), body));

		}
		return setParameters(method, parameters);
	}
	private Object[] setParameters(Method method, List<Param> parameters)
			throws AnnotationNotFoundException, ParameterSintaxException {
		Parameter methodParameter;
		Param queryParam;
		Object buffer;
		ObjectMapper mapper = new ObjectMapper();
		List<Object> parametersValues = new ArrayList<>();
		Parameter[] methodParameters = method.getParameters();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < method.getParameterCount(); i++) {
			if (parameters.isEmpty()) {
				continue;
			}
			methodParameter = methodParameters[i];
			proccessParameters(parameters, methodParameter, mapper,
					parametersValues, methodParameters, parameterAnnotations,
					i);
		}

		return parametersValues.toArray();
	}
	private void proccessParameters(List<Param> parameters,
			Parameter methodParameter, ObjectMapper mapper,
			List<Object> parametersValues, Parameter[] methodParameters,
			Annotation[][] parameterAnnotations, int i)
			throws ParameterSintaxException, AnnotationNotFoundException {
		Param queryParam;
		Object buffer;
		for (Annotation annotation : parameterAnnotations[i]) {
			if (annotation.annotationType()
					.isAssignableFrom(RequestBody.class)) {
				queryParam = parameters.remove(parameters.size() - 1);
			} else {
				queryParam = parameters.remove(0);
			}
			if (annotation.annotationType()
					.isAssignableFrom(RequestParam.class)) {
				if (methodParameters[i].getName()
						.equals(queryParam.getName())) {
					buffer = mapper.convertValue(queryParam.getValue(),
							methodParameters[i].getType());
					parametersValues.add(buffer);
				} else {
					throw new ParameterSintaxException(String.format(
							"query parameter %s must have the same name as the method parameter %s",
							queryParam.getName(), methodParameter.getName()));
				}
			}

			mappParameter(methodParameter, queryParam, methodParameters,
					parametersValues, i);

		}
	}

	private static void mappParameter(Parameter methodParameter,
			Param queryParam, Parameter[] methodParameters,
			List<Object> parametersValues, int i) {
		Object buffer;
		ObjectMapper mapper = new ObjectMapper();
		for (Annotation e : methodParameter.getAnnotations()) {
			if (e.annotationType().isAssignableFrom(RequestBody.class)) {
				buffer = mapper.convertValue(queryParam.getValue(),
						methodParameters[i].getType());
				parametersValues.add(buffer);
			}

		}

	}
	private List<Param> getQueryParameters(HttpExchange request) {
		String[] queryParams;
		String[] paramElements;
		Param parameter;
		List<Param> parameters = new ArrayList<>();
		if (request.getRequestURI().getQuery() != null) {

			queryParams = request.getRequestURI().getQuery().split("&");
			for (String queryParam : queryParams) {
				paramElements = queryParam.split("=");
				for (int i = 0; i < paramElements.length; i += 2) {
					parameter = new Param(paramElements[i],
							paramElements[i + 1]);
					parameters.add(parameter);
				}
			}
		}
		return parameters;

	}
	private Class<?> getBodyClass(Method method, HttpMethod http)
			throws BodyException, RequestFormatException {
		byte count = 0;
		Class<?> clazz = null;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		if (http.equals(HttpMethod.GET)) {
			throw new RequestFormatException(
					String.format("%s dont use body", HttpMethod.GET.name()));
		}

		for (int i = 0; i < parameterAnnotations.length; i++) {
			for (Annotation annotation : parameterAnnotations[i]) {
				if (annotation.annotationType().equals(RequestBody.class)) {
					count++;
					clazz = method.getParameters()[i].getType();
				}

			}

		}
		if (count > 1) {
			throw new BodyException(
					"The request must have only 1 parameter with @RequestBody");
		}
		if (count < 1) {
			throw new BodyException(
					"The request needs an parameter with @RequestBody");
		}

		return clazz;
	}

	private static Object getBody(HttpExchange request, Class<?> expected)
			throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(request.getRequestBody(), expected);
	}

}
