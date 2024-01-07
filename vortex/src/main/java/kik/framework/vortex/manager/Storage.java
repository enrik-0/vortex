
package kik.framework.vortex.manager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private HashMap<String, ArrayList<Map<String, Object>>> urls;
	private HashMap<String, ArrayList<Class<?>>> classes;

	private Storage() {
		this.urls = new HashMap<>();
		this.classes = new HashMap<>();

		urls.put("GET", new ArrayList<>());
		urls.put("POST", new ArrayList<>());
		urls.put("PUT", new ArrayList<>());
		urls.put("DELETE", new ArrayList<>());
	}

	public static Storage getInstance() {
		if (STORAGE == null) {
			synchronized (Storage.class) {
				STORAGE = new Storage();
			}

		}

		return STORAGE;
	}

	public void addUrl(String httpMethod, Map<String, Object> url) {
		urls.get(httpMethod).add(url);
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

	public Method getMethod(String method, String uri) {

		return (Method) ((HashMap<String, Object>) urls.get(method).stream().filter(map -> {
		return map.get("uri").equals(uri);}).toArray()[0]).get("call");
	}

	private boolean isMethod(String method, String uri) {
		Long count = urls.get(method).stream().filter(m -> {
			return m.get("uri").equals(uri);
		}).count();

		return count >= 1;

	}
	public String[] checkType(String uri) {
		ArrayList<String> type = new ArrayList<>();
		if (isMethod("GET", uri)) {
			type.add("GET");
		}
		if (isMethod("POST", uri)) {
			type.add("POST");
		}

		if (isMethod("DELETE", uri)) {
			type.add("DELETE");
		}
		if (isMethod("PUT", uri)) {
			type.add("PUT");
		}

		return type.toArray(new String[type.size()]);
	}

}
