
package vortex.annotate.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import vortex.annotate.annotations.Autowired;
import vortex.annotate.components.Controller;
import vortex.annotate.components.Service;
import vortex.annotate.constants.HttpMethod;
import vortex.annotate.exceptions.InitiateServerException;
import vortex.annotate.exceptions.UriException;
import vortex.properties.kinds.Server;

/**
 * This storage handles the URIS and the asociated objects
 */
public final class Storage {
    private static Storage STORAGE;
    /**
     * hashmap method -> list(hashmaps)
     * 
     * 
     * hashmaps call -> {@link Method} uri -> String uri
     * 
     */
    private Map<HttpMethod, List<Map<String, Object>>> urls;
    private Map<String, List<Class<?>>> classes;
    private Map<Class<?>, Object> controllers;
    private List<Object> objects;
    private Set<Class<?>> runnable;
    private Map<Class<?>, String> cors;

    private Storage() {
	this.urls = new EnumMap<>(HttpMethod.class);
	this.classes = new HashMap<>();
	this.cors = new HashMap<>();
	this.objects = new ArrayList<>();
	for (HttpMethod method : HttpMethod.values()) {
	    urls.put(method, new ArrayList<>());
	}
	try {
	    fillComponent(Controller.class);
	    fillComponent(Service.class);
	} catch (Exception e) {

	    for (HttpMethod method : HttpMethod.values()) {
		urls.put(method, new ArrayList<>());
	    }

	}
    }

    /**
     * initialize all the data structure of the storage
     * @return {@link Storage}
     */
    public static Storage getInstance() {
	synchronized (Storage.class) {
	    if (STORAGE == null) {
		STORAGE = new Storage();
	    }

	}

	return STORAGE;
    }

    /**
     * 
     * @return map which keys are all the {@link HttpMethod} and a list
     */
    public Map<HttpMethod, List<Map<String, Object>>> getUrls() {
	Map<HttpMethod, List<Map<String, Object>>> buffer = new EnumMap<>(HttpMethod.class);
	urls.forEach(buffer::put);
	return buffer;
    }

    /**
     * add an uri if is well created
     * @param method {@link HttpMethod}
     * @param url map that contains the uri and the {@link Method} that must be called
     * @throws InitiateServerException if there are more than one {@link HttpMethod} in one {@link Method}
     */
    public void addUrl(HttpMethod method, Map<String, Object> url) throws InitiateServerException {
	try {
	    Method call = getMethod(method, (String) url.get("uri"));
	    if (!call.getName().equals(((Method) url.get("call")).getName())) {
		throw new InitiateServerException(String.format(
			"Server won't initiate becauase there are more " + "that one method assign to this uri %s",
			(String) url.get("uri")));
	    }
	} catch (UriException e) {
	    urls.get(method).add(url);
	}
    }

    /**
     * add a type of annotation to the data struccture
     * @param name name of the annotation type
     */
    public void addAnnotationType(String name) {
	classes.put(name, new ArrayList<>());
    }

    /**
     * add given class to the annotation kind
     * @param annotationName name of the annotation
     * @param classToSave the class that will be assigned
     */
    public void addClass(String annotationName, Class<?> classToSave) {
	if (annotationName.equals(Controller.class.getName())) {
	    addCORS(classToSave, "*");
	}
	classes.get(annotationName).add(classToSave);
    }

    /**
     * @param component name of the component 
     * @return list of the classes registered as that component
     */
    public List<Class<?>> getComponent(String component) {
	return classes.get(component);

    }

    /**
     * 
     * @param component class
     * @return list of the classes registered as that component
     */
    public List<Class<?>> getComponent(Class<?> component) {
	return getComponent(component.getName());

    }

    /**
     * @param method {@link HttpMethod} asociated
     * @param uri uri assigned 
     * @return the {@link Method} asociated to the given uri and method
     *@throws UriException if the uri dosent exists
     */
    public Method getMethod(HttpMethod method, String uri) throws UriException {
	try {
	    return (Method) urls.get(method).stream().filter(map -> map.get("uri").equals(uri)).toList().get(0)
		    .get("call");

	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new UriException(String.format("the uri %s dosent exists", uri));
	}
    }

    /**
     * checks if given uri exists for the given method
     * @param method {@link HttpMethod}
     * @param uri uri checking if exists
     * @return if the uri is assign at that {@link HttpMethod}
     */
    
    
    private boolean isMethod(HttpMethod method, String uri) {
	Long count = urls.get(method).stream().filter(m -> m.get("uri").equals(uri)).count();

	return count >= 1;

    }

    /**
     * returns an array of the {@link HttpMethod} defined for the given uri
     * @param uri uri to check 
     * @return array of the {@link HttpMethod} defined for that uri
     */
    public HttpMethod[] checkType(String uri) {
	uri = (Server.CONTEXT_PATH.value().equals("/") ? "" : Server.CONTEXT_PATH) + uri;
	ArrayList<HttpMethod> type = new ArrayList<>();
	if (isMethod(HttpMethod.GET, uri)) {
	    type.add(HttpMethod.GET);
	}
	if (isMethod(HttpMethod.POST, uri)) {
	    type.add(HttpMethod.POST);
	}

	if (isMethod(HttpMethod.DELETE, uri)) {
	    type.add(HttpMethod.DELETE);
	}
	if (isMethod(HttpMethod.PUT, uri)) {
	    type.add(HttpMethod.PUT);
	}

	return type.toArray(new HttpMethod[type.size()]);
    }

    /**
     * 
     * @return {@link Set} of runnable classes
     */
    public Set<Class<?>> getRunnable() {
	return runnable;
    }

    private void fillComponent(Class<?> component) throws InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	for (Class<?> clazz : getComponent(component.getName())) {
	    Object object = clazz.getConstructor().newInstance();
	    for (Field field : object.getClass().getDeclaredFields()) {
		for (Annotation annotation : field.getAnnotations()) {
		    if (annotation.annotationType().getSimpleName().equals(Autowired.class.getSimpleName())) {
			Class<?> fieldClass = field.getType();
			Object fieldObject = checkField(fieldClass);
			field.setAccessible(true);
			if (fieldObject == null) {
			    try {
				System.err.println();
				for (Annotation a : fieldClass.getDeclaredAnnotations()) {
				    if (annotation.annotationType().getSimpleName()
					    .equals(Service.class.getSimpleName())) {
					fillComponent(fieldClass);
					fieldObject = checkField(fieldClass);
				    }
				}
				if(fieldObject == null)
				fieldObject = fieldClass.getConstructor().newInstance(null);
			    } catch (NoSuchMethodException e) {
				fieldObject = fieldClass.getConstructor().newInstance();
			    }
			    objects.add(fieldObject);
			}
			field.set(object, fieldObject);
			field.setAccessible(false);
		    }
		}

	    }
	    objects.add(object);
	    if(component.equals(Controller.class))
	    controllers.put(clazz, object);

	}
    }

    /**
     * checks and returns if the given class is in our data structure
     * @param fieldClass class to check
     * @return if the given class is in our data structure
     */
    public Object checkField(Class<?> fieldClass) {
	Object result;
	try {

	    result = objects.stream().filter(o -> fieldClass.equals(o.getClass())).findFirst().get();
	} catch (NoSuchElementException e) {
	    result = null;
	}

	return result;
    }

    /**
     * 
     * @param method {@link Method}
     * @return An instance of the controller asociated to the given method
     * @throws InstantiationException instantiation error
     * @throws IllegalAccessException illegal access to propertites
     * @throws IllegalArgumentException wrong number of parameters
     * @throws InvocationTargetException calling a function of a class with another
     * @throws NoSuchMethodException method not exists
     * @throws SecurityException Java exception
     */
    public Object getObjectController(Method method) throws InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	if (controllers == null) {
	    this.controllers = new HashMap<>();
	    fillComponent(Service.class);
	    fillComponent(Controller.class);
	}
	return controllers.get(method.getDeclaringClass());
    }

    /**
     * gives the CORS to a controller
     * @param annotatedClass a {@link Controller } class
     * @param value origin whoose conections will be accepted
     */
    public void addCORS(Class<?> annotatedClass, String value) {
	cors.put(annotatedClass, value);
    }

    /**
     * 
     * @param clazz a {@link Controller} class
     * @return CORS defined for the given class
     */
    public String getCors(Class<?> clazz) {
	return cors.get(clazz);
    }

}
