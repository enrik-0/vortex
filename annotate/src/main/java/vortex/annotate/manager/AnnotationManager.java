package vortex.annotate.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.Collections;
import vortex.annotate.components.Controller;
import vortex.annotate.components.Entity;
import vortex.annotate.components.Repository;
import vortex.annotate.components.Service;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.controller.CrossOrigin;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.properties.kinds.Application;
import vortex.properties.kinds.Server;
import vortex.utils.MappingUtils;

/**
 * @Author: Enrique Javier Villar Cea
 * @Date: 04/01/2024
 * @Purpose: handle the annotations
 */
public final class AnnotationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationManager.class);
    private static AnnotationManager manager;
    private static Reflections reflections;

    private AnnotationManager() throws UriException, InitiateServerException {

	initialize();
    }

    public static AnnotationManager getInstance() throws UriException, InitiateServerException {

	synchronized (AnnotationManager.class) {
	    if (manager == null) {

		manager = new AnnotationManager();
	    }

	}

	return manager;
    }

    private void initialize() throws UriException, InitiateServerException {
	seekClasses();
	Annotation mapping = null;
	ArrayList<Class<?>> annotatedClasses = (ArrayList<Class<?>>) Storage.getInstance()
		.getComponent(Controller.class.getName());
	for (Class<?> annotatedClass : annotatedClasses) {
	    for (Annotation annotation : annotatedClass.getAnnotations()) {
		if (annotation.annotationType().getSimpleName().equals(RequestMapping.class.getSimpleName())) {
		    mapping = annotation;
		}
	    }

	    for (Method method : annotatedClass.getDeclaredMethods()) {
		for (Annotation annotation : method.getAnnotations()) {

		    filter(mapping, annotation, method);
		}
	    }
	}
    }

    private void filter(Annotation mapping, Annotation annotation, Method method)
	    throws UriException, InitiateServerException {
	HashMap<String, Object> map;
	ArrayList<String> uris = (ArrayList<String>) getAllUris(mapping, annotation);

	for (String uri : uris) {
	    map = new HashMap<>();
	    map.put("uri", uri);
	    map.put("call", method);
	    methodAssignmet(annotation.annotationType().getSimpleName(), map);
	}
    }

    private List<String> getAllUris(Annotation mapping, Annotation annotation) throws UriException {
	byte counter;
	String annotationString = annotation.toString();
	String uri = null;
	String[] auris = null;
	char[] characters = { '{', '}' };
	for (char character : characters) {
	    counter = 0;
	    for (char charr : annotationString.toCharArray()) {
		if (charr == character) {
		    counter++;
		}
		if (counter >= 2) {
		    throw new UriException("uri contains a " + character + " and is not allowed");
		}
	    }
	}
	if (mapping.annotationType().getSimpleName().equals(RequestMapping.class.getSimpleName())) {
	    Method[] methods = mapping.annotationType().getMethods();
	    try {
		for (Method method : methods) {
		    if (method.getName().equals("uris")) {
			auris = (String[]) MappingUtils.map(method.invoke(mapping), String[].class);
		    }
		    if (method.getName().equals("value")) {
			uri = (String) MappingUtils.map(method.invoke(mapping), String.class);
		    }
		}
	    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
	    }

	}

	String contextPath = "/".equals(Server.CONTEXT_PATH.value()) ? "" : (String) Server.CONTEXT_PATH.value();
	ArrayList<String> uris = new ArrayList<>();
	ArrayList<String> mappingUris = new ArrayList<>();
	String[] annotationUris;
	Collections.addAll(mappingUris, auris);
	if (mappingUris.isEmpty()) {
	    mappingUris.add(uri);
	}

	if (annotationString.contains("uris={}")) {
	    annotationUris = new String[1];
	    annotationUris[0] = annotationString.split("\"")[1].replace("\\", "");
	} else {

	    annotationUris = annotationString.split("\\{")[1].split("\\}")[0].split(",");
	}
	for (int i = 0; i < annotationUris.length; i++) {
	    annotationUris[i] = annotationUris[i].replace("\"", " ").trim();
	}

	for (String mappingUri : mappingUris) {
	    for (String annotationUri : annotationUris) {
		var builder = new StringBuilder();
		builder.append(contextPath);
		builder.append(mappingUri);
		builder.append(annotationUri);
		uris.add(builder.toString());
	    }
	}

	return uris;
    }

    private void methodAssignmet(String annotationName, HashMap<String, Object> map) throws InitiateServerException {
	switch (annotationName) {
	case "GetMapping":
	    assignMethod(HttpMethod.GET, map);
	    break;
	case "PutMapping":
	    assignMethod(HttpMethod.PUT, map);
	    break;

	case "DeleteMapping":
	    assignMethod(HttpMethod.DELETE, map);
	    break;
	case "PostMapping":
	    assignMethod(HttpMethod.POST, map);
	    break;
	case "OptionsMapping":
	    assignMethod(HttpMethod.OPTIONS, map);
	    break;
	case "HeadMapping":
	    assignMethod(HttpMethod.HEAD, map);
	    break;
	case "TraceMapping":
	    assignMethod(HttpMethod.TRACE, map);
	    break;
	case "ConnectMapping":
	    assignMethod(HttpMethod.CONNECT, map);
	    break;
	case "PatchMapping":
	    assignMethod(HttpMethod.PATCH, map);
	    break;

	default:
	}
    }

    private void assignMethod(HttpMethod method, HashMap<String, Object> map) throws InitiateServerException {
	final var LOG_MESSAGE = "Found %s with uri %s and method %s";
	Storage.getInstance().addUrl(method, map);
	if ((boolean) Application.DEBUG.value()) {
	    LOGGER.debug(
		    String.format(LOG_MESSAGE, method.name(), map.get("uri"), ((Method) map.get("call")).getName()));
	}
    }

    private static void setClasses(Class<? extends Annotation> annotation) {
	try {

	    var loader = PackageLoader.getInstance().getLoader();
	    if (loader == null) {
		reflections = new Reflections("", new SubTypesScanner(true), new TypeAnnotationsScanner());
	    } else {
		reflections = new Reflections(loader, new SubTypesScanner(false), new TypeAnnotationsScanner(),
			new TypeElementsScanner(), new FieldAnnotationsScanner());
	    }
	} catch (Exception e) {
	}
	Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);
	if (annotation.getSimpleName().equals(CrossOrigin.class.getSimpleName())) {
	    for (Class<?> annotatedClass : annotatedClasses) {
		String value = null;
		var r = annotatedClass.getAnnotation(CrossOrigin.class);
		if (r != null) {
		    value = r.value();
		}
		Storage.getInstance().addCORS(annotatedClass, value);
	    }
	} else {

	    Storage.getInstance().addAnnotationType(annotation.getName());

	    for (Class<?> annotatedClass : annotatedClasses) {
		
		    Storage.getInstance().addClass(annotation.getName(), annotatedClass);
		    if (LOGGER.isDebugEnabled()) {
			LOGGER.info(String.format("class :%s annotated with %s ", annotatedClass.getName(),
				annotation.getName()));
		}
	    }
	}

    }

    private void seekClasses() {
	var out = System.out;
	var err = System.err;
	System.setOut(null);
	System.setErr(null);

	setClasses(Controller.class);
	setClasses(CrossOrigin.class);
	final var LOG_MESSAGE = "number of %s %d";
	if ((boolean) Application.DEBUG.value()) {

	    System.setOut(out);
	    LOGGER.debug(String.format(LOG_MESSAGE, Controller.class.getSimpleName(),
		    Storage.getInstance().getComponent(Controller.class.getName()).size()));
	    System.setOut(null);
	}
	setClasses(Service.class);
	if ((boolean) Application.DEBUG.value()) {

	    System.setOut(out);
	    LOGGER.debug(String.format(LOG_MESSAGE, Service.class.getSimpleName(),
		    Storage.getInstance().getComponent(Service.class.getName()).size()));

	    System.setOut(null);
	}
	setClasses(Entity.class);
	if ((boolean) Application.DEBUG.value()) {

	    System.setOut(out);
	    LOGGER.debug(String.format(LOG_MESSAGE, Entity.class.getSimpleName(),
		    Storage.getInstance().getComponent(Entity.class.getName()).size()));
	    System.setOut(null);
	}
	setClasses(Repository.class);
	if ((boolean) Application.DEBUG.value()) {

	    System.setOut(out);
	    LOGGER.debug(String.format(LOG_MESSAGE, Entity.class.getSimpleName(),
		    Storage.getInstance().getComponent(Entity.class.getName()).size()));
	}
	System.setOut(out);
	System.setErr(err);
    }

    public static Storage get() {

	return Storage.getInstance();
    }

    public static Set<Class<?>> getClassesAnnotated(String search, Class<? extends Annotation> annotation) {

	var reflections = new Reflections(search);
	return reflections.getTypesAnnotatedWith(annotation);

    }

}
