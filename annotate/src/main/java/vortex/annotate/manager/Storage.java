
package vortex.annotate.manager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import vortex.annotate.components.Controller;
import vortex.annotate.constants.HttpMethod;
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
    private EnumMap<HttpMethod, ArrayList<Map<String, Object>>> urls;
    private HashMap<String, ArrayList<Class<?>>> classes;
    private HashMap<Class<?>, Object> controllers;
    private Set<Class<?>> runnable;

    private Storage() {
	this.urls = new EnumMap<>(HttpMethod.class);
	this.classes = new HashMap<>();
    for(HttpMethod method : HttpMethod.values()){
    urls.put(method, new ArrayList<>());
    }
    try {
    fillControllers();
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

    public EnumMap<HttpMethod, ArrayList<Map<String, Object>>> getUrls() {
	EnumMap<HttpMethod, ArrayList<Map<String, Object>>> buffer = new EnumMap<>(HttpMethod.class);
	urls.forEach(buffer::put);
	return buffer;
    }

    public void addUrl(HttpMethod method, Map<String, Object> url) {
	urls.get(method).add(url);
    }

    public void addAnnotationType(String name) {
	classes.put(name, new ArrayList<>());
    }

    public void addClass(String annotationName, Class<?> classToSave) {
	classes.get(annotationName).add(classToSave);
    }

    public List<Class<?>> getComponent(String component) {
	return classes.get(component);

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

    private void fillControllers() throws InstantiationException, IllegalAccessException, IllegalArgumentException,
	    InvocationTargetException, NoSuchMethodException, SecurityException {
	for (Class<?> clazz : getComponent(Controller.class.getName())) {
	    controllers.put(clazz, clazz.getConstructor().newInstance());
	}
    }

    public Object getObjectController(Method method) throws InstantiationException, IllegalAccessException,
	    IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
	if (controllers == null) {
	    this.controllers = new HashMap<>();
	    fillControllers();
	}
	return controllers.get(method.getDeclaringClass());
    }

    public void setRunnable(Set<Class<?>> runnable) {
	this.runnable = runnable;
    }

}
