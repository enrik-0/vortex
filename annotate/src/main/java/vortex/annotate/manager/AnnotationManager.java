package vortex.annotate.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.mathcs.backport.java.util.Collections;
import vortex.annotate.components.Controller;
import vortex.annotate.components.Entity;
import vortex.annotate.components.Service;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.controller.RequestMapping;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.annotate.method.mapping.GetMapping;
import vortex.properties.kinds.Application;
import vortex.properties.kinds.Server;
/**
 * @Author: Enrique Javier Villar Cea
 * @Date: 04/01/2024
 * @Purpose: handle the annotations
 */
public final class AnnotationManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationManager.class);
    private static AnnotationManager manager;
    private Storage data = Storage.getInstance();

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
	ArrayList<Class<?>> annotatedClasses = (ArrayList<Class<?>>) data.getComponent(Controller.class.getName());
	for (Class<?> annotatedClass : annotatedClasses) {
	    RequestMapping[] controllers = annotatedClass.getAnnotationsByType(RequestMapping.class);
	    for (Method method : annotatedClass.getDeclaredMethods()) {
		for (Annotation annotation : method.getAnnotations()) {

		    filter(controllers[0], annotation, method);
		}
	    }
	}
    }

    private void filter(RequestMapping mapping, Annotation annotation, Method method) throws UriException, InitiateServerException {
	HashMap<String, Object> map; 
	ArrayList<String> uris = (ArrayList<String>) getAllUris(mapping, annotation);

	for (String uri : uris) {
	map = new HashMap<>(); 
	map.put("uri", uri);
	map.put("call", method);
	methodAssignmet(annotation.annotationType().getSimpleName(), map);
	}
    }
   private List<String >getAllUris(RequestMapping mapping, Annotation annotation) throws UriException {
       byte counter;
       String annotationString = annotation.toString();
       char[] characters = {'{', '}'};
       for (char character : characters) {
	   counter = 0;
       for (char charr : annotationString.toCharArray()) {
	   if ( charr == character) {
	       counter++;
	   }
	   if(counter >= 2) {
	       throw new UriException("uri contains a " + character + " and is not allowed");
	   }
       }
   }

       String contextPath = "/".equals(Server.CONTEXT_PATH.value()) ? "" : (String) Server.CONTEXT_PATH.value();
       ArrayList<String> uris = new ArrayList<>();
       ArrayList<String> mappingUris = new ArrayList<>();
       String[] annotationUris;
       Collections.addAll(mappingUris, mapping.uris());
       if(mappingUris.isEmpty()) {
	   mappingUris.add(mapping.value());
       }
       if(annotationString.contains("uris={}")) {
	   annotationUris = new String[1];
	   annotationUris[0] = annotationString.split("\"")[1].replace("\\", "");
       }else {
	   
       annotationUris = annotationString.split("\\{")[1].split("\\}")[0].split(",");
       }
       for (int i = 0; i < annotationUris.length; i++) {
	   annotationUris[i] = annotationUris[i].replace("\"", " ").trim();
       }
       
       for (String mappingUri : mappingUris) {
	   for(String annotationUri : annotationUris) {
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
	data.addUrl(method, map);
	if ((boolean) Application.DEBUG.value()) {
	LOGGER.debug(String.format(LOG_MESSAGE, method.name(), map.get("uri"),
		((Method) map.get("call")).getName()));
	}
    }

    private void setClasses(Class<? extends Annotation> annotation) {
	var reflections = new Reflections("");
	Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(annotation);
	data.addAnnotationType(annotation.getName());

	for (Class<?> annotatedClass : annotatedClasses) {
	    data.addClass(annotation.getName(), annotatedClass);
	    if (LOGGER.isDebugEnabled()) {
		LOGGER.info(
			String.format("class :%s annotated with %s ", annotatedClass.getName(), annotation.getName()));
	    }
	}

    }

    private void seekClasses() {
	setClasses(Controller.class);
	final var LOG_MESSAGE = "number of %s %d";
	if((boolean) Application.DEBUG.value()){
	    LOGGER.debug(String.format(LOG_MESSAGE, Controller.class.getSimpleName(),
		    data.getComponent(Controller.class.getName()).size()));
	}
	setClasses(Service.class);
	if((boolean) Application.DEBUG.value()){
	LOGGER.debug(String.format(LOG_MESSAGE, Service.class.getSimpleName(),
		data.getComponent(Service.class.getName()).size()));
	}
	setClasses(Entity.class);
	if((boolean) Application.DEBUG.value()){
	LOGGER.debug(String.format(LOG_MESSAGE, Entity.class.getSimpleName(),
		data.getComponent(Entity.class.getName()).size()));
	}
    }

    public static Set<Class<?>> getClassesAnnotated(String search, Class<? extends Annotation> annotation) {
	var reflections = new Reflections(search);
	return reflections.getTypesAnnotatedWith(annotation);

    }

}
