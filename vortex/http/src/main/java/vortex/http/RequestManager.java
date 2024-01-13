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
import vortex.annotate.manager.Storage;
import vortex.http.elements.Param;
import vortex.http.exceptions.BodyException;

public class RequestManager {

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

	public void getHandle(HttpExchange request) {
		LOGGER.debug("get" + request.getRequestURI().getRawPath());
	}

	public void postHandle(HttpExchange request) {
		LOGGER.debug(request.getRequestURI().getPath());

		Method method = Storage.getInstance().getMethod(
				HttpMethod.valueOf(request.getRequestMethod()),
				request.getRequestURI().getPath());
		method.getAnnotatedExceptionTypes();

		if (request.getRequestHeaders().get("content-type") != null) {

			try {
				executeMethod(method, request);
			} catch (Exception e) {
				// TODOe: handle exception
				e.getStackTrace();
			}
		}

	}

	private Object executeMethod(Method method, HttpExchange request) {
		Object launcher, response;
		Object[] parameters;
		response = null;
		try {
			launcher = Storage.getInstance().getObjectController(method);
			parameters = getParameters(method, request);
			response = method.invoke(launcher, parameters);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BodyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;

	}

	private Object[] getParameters(Method method, HttpExchange request)
			throws IOException, BodyException {
		List<Param> parameters = getQueryParameters(request);
		Object body;
		if (request.getRequestHeaders().get("content-type") != null) {
			try {
				body = getBody(request, getBodyClass(method));
				parameters.add(new Param(RequestBody.class.getName(), body));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return setParameters(method, parameters);

	}
	private Object[] setParameters(Method method, List<Param> parameters) {
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
			for (Annotation annotation : parameterAnnotations[i]) {
				if (annotation.annotationType().getName()
						.equals(RequestBody.class.getName())) {
					queryParam = parameters.remove(parameters.size() - 1);
				} else {
					queryParam = parameters.remove(0);
				}
				if (annotation.annotationType().getName()
						.equals(RequestParam.class.getName())) {
				}
				if (methodParameters[i].getName()
						.equals(queryParam.getName())) {
					buffer =  mapper.convertValue(queryParam.getValue(),
					methodParameters[i].getType());
					parametersValues.add(buffer);
				}
				for (Annotation e : methodParameter.getAnnotations()) {
					if (e.annotationType().getName()
							.equals(RequestBody.class.getName())) {
		buffer =  mapper.convertValue(queryParam.getValue(),
					methodParameters[i].getType());
						parametersValues.add(buffer);
					}

				}

			}

		}

		return parametersValues.toArray();
	}

	private List<Param> getQueryParameters(HttpExchange request) {
		String[] queryParams;
		String[] paramElements;
		Param parameter;
		List<Param> parameters = new ArrayList<>();
		queryParams = request.getRequestURI().getQuery().split("&");
		for (String queryParam : queryParams) {
			paramElements = queryParam.split("=");
			for (int i = 0; i < paramElements.length; i += 2) {
				parameter = new Param(paramElements[i], paramElements[i + 1]);
				parameters.add(parameter);
			}
		}
		return parameters;

	}
	private Class<?> getBodyClass(Method method) throws BodyException {
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
