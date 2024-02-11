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

import vortex.annotate.annotations.HttpMethod;
import vortex.annotate.annotations.RequestBody;
import vortex.annotate.annotations.RequestParam;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.manager.Storage;
import vortex.http.elements.ExchangeHttp;
import vortex.http.elements.Param;
import vortex.http.exceptions.BodyException;
import vortex.http.exceptions.ParameterSintaxException;
import vortex.http.exceptions.RequestFormatException;
import vortex.http.utils.MappingUtils;

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

	public Object handle(ExchangeHttp request) throws InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException,
			IOException, BodyException, ParameterSintaxException,
			RequestFormatException, UriException {
		LOGGER.debug(request.getRequestURI().getPath());

		var method = Storage.getInstance().getMethod(
				request.getRequestMethod(), request.getRequestURI().getPath());
		method.getAnnotatedExceptionTypes();

		return executeMethod(method, request, request.getRequestMethod());
		// TODO: THREAD PÔOL
	}

	private static Object executeMethod(Method method, ExchangeHttp request,
			HttpMethod http)
			throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException, IOException,
			BodyException, ParameterSintaxException, RequestFormatException {
		var launcher = Storage.getInstance().getObjectController(method);
		Object[] parameters = getParameters(method, request, http);
		LOGGER.debug(String.format("number of parameters found %d", parameters.length));
		return method.invoke(launcher, parameters);

	}

	public static Object[] getParameters(Method method, ExchangeHttp request,
			HttpMethod http) throws IOException, BodyException,
			ParameterSintaxException, RequestFormatException {
		List<Param> parameters = getQueryParameters(request);
		Object body;
		if (request.getRequestHeaders().get("Content-type") != null) {
			body = MappingUtils.map(request.getRequestBody(),
					getBodyClass(method, http));
			parameters.add(new Param(RequestBody.class.getName(), body));

		}
		return setParameters(method, parameters);
	}
	private static Object[] setParameters(Method method, List<Param> parameters)
			throws ParameterSintaxException {
		Parameter methodParameter;
		List<Object> parametersValues = new ArrayList<>();
		Parameter[] methodParameters = method.getParameters();
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		for (int i = 0; i < method.getParameterCount(); i++) {
			if (parameters.isEmpty()) {
				continue;
			}
			methodParameter = methodParameters[i];
			proccessParameters(parameters, methodParameter, parametersValues,
					methodParameters, parameterAnnotations, i);
		}

		return parametersValues.toArray();
	}
	private static void proccessParameters(List<Param> parameters,
			Parameter methodParameter, List<Object> parametersValues,
			Parameter[] methodParameters, Annotation[][] parameterAnnotations,
			int i) throws ParameterSintaxException {
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
					buffer = MappingUtils.map(queryParam.getValue(),
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
		for (Annotation e : methodParameter.getAnnotations()) {
			if (e.annotationType().isAssignableFrom(RequestBody.class)) {
				buffer = MappingUtils.map(queryParam.getValue(),
						methodParameters[i].getType());
				parametersValues.add(buffer);
			}

		}

	}
	private static List<Param> getQueryParameters(ExchangeHttp request) {
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
					LOGGER.info(String.format("query parameter %s", parameter));
				}
			}
		}
		return parameters;

	}
	public static Class<?> getBodyClass(Method method, HttpMethod http)
			throws BodyException, RequestFormatException {

		if (http == HttpMethod.GET) {
			throw new RequestFormatException(
					String.format("%s dont use body", HttpMethod.GET.name()));
		}

		byte count = 0;
		Class<?> clazz = null;
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();

		for (int i = 0; i < parameterAnnotations.length; i++) {
			for (Annotation annotation : parameterAnnotations[i]) {
				if (annotation.annotationType().equals(RequestBody.class)) {
					count++;
					clazz = method.getParameters()[i].getType();
					LOGGER.debug(String.format("body class is %s", clazz.getSimpleName()));
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
}
