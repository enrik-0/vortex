
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
 * @Author Enrique Javier Villar Cea
 * @Purpose Central storage
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
	}
    }

    public static Storage getInstance() {
	synchronized (Storage.class) {
	    if (STORAGE == null) {
		STORAGE = new Storage();
	    }

	}

	return STORAGE;
    }

    public Map<HttpMethod, List<Map<String, Object>>> getUrls() {
	Map<HttpMethod, List<Map<String, Object>>> buffer = new EnumMap<>(HttpMethod.class);
	urls.forEach(buffer::put);
	return buffer;
    }

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

    public void addAnnotationType(String name) {
	classes.put(name, new ArrayList<>());
    }

    public void addClass(String annotationName, Class<?> classToSave) {
	if(annotationName.equals(Controller.class.getName())) {
	    addCORS(classToSave, "*");
	}
	classes.get(annotationName).add(classToSave);
    }

    public List<Class<?>> getComponent(String component) {
	return classes.get(component);

    }
    public List<Class<?>> getComponent(Class<?> component) {
	return classes.get(component.getName());

    }

    public Method getMethod(HttpMethod method, String uri) throws UriException {
	try {
	    return (Method) urls.get(method).stream().filter(map -> map.get("uri").equals(uri)).toList().get(0)
		    .get("call");

	} catch (ArrayIndexOutOfBoundsException e) {
	    throw new UriException(String.format("the uri %s dosent exists", uri));
	}
    }

    private boolean isMethod(HttpMethod method, String uri) {
	Long count = urls.get(method).stream().filter(m -> m.get("uri").equals(uri)).count();

	return count >= 1;

    }

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

    public Set<Class<?>> getRunnable() {
	return runnable;
    }

    private void fillComponent(Class<?> component) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException {
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
				fieldObject = fieldClass.getConstructor().newInstance(null);
			    }catch(NoSuchMethodException e) {
			    fieldObject = fieldClass.getConstructor().newInstance();
			    }
			    objects.add(fieldObject);
			}
			field.set(object, fieldObject);
			field.setAccessible(false);
		    }
		}

	    }
	    controllers.put(clazz, object);

	}
    }

    public Object checkField(Class<?> fieldClass) {
	Object result;
	try {

	    result = objects.stream().filter(o -> fieldClass.equals(o.getClass())).findFirst().get();
	} catch (NoSuchElementException e) {
	    result = null;
	}

	return result;
    }

    public Object getObjectController(Method method) throws InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	if (controllers == null) {
	    this.controllers = new HashMap<>();
	    fillComponent(Controller.class);
	}
	return controllers.get(method.getDeclaringClass());
    }

    public void setRunnable(Set<Class<?>> runnable) {
	this.runnable = runnable;
    }

    public void addCORS(Class<?> annotatedClass, String value) {
	cors.put(annotatedClass, value);
    }
    public String getCors(Class<?> clazz) {
	return cors.get(clazz);
    }

}
