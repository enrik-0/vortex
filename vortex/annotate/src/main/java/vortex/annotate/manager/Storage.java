
package vortex.annotate.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vortex.annotate.annotations.HttpMethod;

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

	private Storage() {
		this.urls = new EnumMap<>(HttpMethod.class);
		this.classes = new HashMap<>();
		urls.put(HttpMethod.GET, new ArrayList<>());
		urls.put(HttpMethod.POST, new ArrayList<>());
		urls.put(HttpMethod.PUT, new ArrayList<>());
		urls.put(HttpMethod.DELETE, new ArrayList<>());
	}

	public static Storage getInstance() {
		if (STORAGE == null) {
			synchronized (Storage.class) {
				STORAGE = new Storage();
			}

		}

		return STORAGE;
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

	public Method getMethod(HttpMethod method, String uri) {

		return (Method) ((HashMap<HttpMethod, Object>) urls.get(method).stream().filter(map -> {
		return map.get("uri").equals(uri);}).toArray()[0]).get("call");
	}

	private boolean isMethod(HttpMethod method, String uri) {
		Long count = urls.get(method).stream().filter(m -> {
			return m.get("uri").equals(uri);
		}).count();

		return count >= 1;

	}
	public HttpMethod[] checkType(String uri) {
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

}
