
package kik.framework.vortex.manager;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 */
public class Storage {
	private HashMap<String, ArrayList<Map<String, Object>>> urls;
	private HashMap<String, ArrayList<Class<?>>> classes;
	public Storage() {
		this.urls = new HashMap<>();
		this.classes = new HashMap<>();

		urls.put("GET", new ArrayList<>());
		urls.put("POST", new ArrayList<>());
		urls.put("PUT", new ArrayList<>());
		urls.put("DELETE", new ArrayList<>());
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
	
	public ArrayList<Class<?>> getComponent(String component){
		return classes.get(component);
		
	}
}
